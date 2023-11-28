package com.example.questionnaire.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.repository.QuestionDao;
import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QnQuVo;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizVo;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuestionnaireDao qnDao;

	@Autowired
	private QuestionDao quDao;

	// Transactional�ΦA�h�i���,����[�b�p����k�W
	@Transactional
	@Override
	public QuizRes create(QuizReq req) {
		// �I�s��k �ˬd�Ѽ� �@�ӬOnull=���\,�t�@�Ӭ�res
		QuizRes checkReult = checkParam(req);
		if (checkReult != null) {
			return checkReult;
		}
		int quId = qnDao.save(req.getQuestionnaire()).getId();
		List<Question> quList = req.getQuestionList();
		if (quList.isEmpty()) {
			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		for (Question qu : quList) {
			qu.setQuId(quId); 
		}
		quDao.saveAll(quList);
		return new QuizRes(RtnCode.SUCCESSFUL);

	}

	// �Ѽ��ˬd ���޿��ܦ��p����k,���D�{���{���X���²��
	// qn.getStartDate().isAfter(qn.getEndDate())�}�l�ɶ��b�����ɶ����ᬰfalse �^�ǿ��~
	private QuizRes checkParam(QuizReq req) {
		Questionnaire qn = req.getQuestionnaire();
		if(!StringUtils.hasText(qn.getTitle())||!StringUtils.hasText(qn.getDescription())
			||qn.getStartDate() == null ||qn.getEndDate() == null||qn.getStartDate().isAfter(qn.getEndDate())){
			return new QuizRes(RtnCode.QUESTIONNAIRE_PARAM_ERROR);
		}
		
		//qu.qnId�@�}�l�S���ҥH���ݭn
		//��l���C���u��s��@��,���list��i�H�@���s��h�i,�A�ϥ�foreach�i��K��
		List<Question> quList = req.getQuestionList();
		for(Question qu:quList) {
		if(qu.getQuId() <=0 ||!StringUtils.hasText(qn.getTitle())||
				!StringUtils.hasText(qu.getOptionType())||!StringUtils.hasText(qu.getOption())) {
			return new QuizRes(RtnCode.QUESTION_PARAM_ERROR);
		}
	}
		
		return null;
	}

	// �ݨ��w�s�b
	@Override
	@Transactional
	public QuizRes update(QuizReq req) {
		QuizRes checkReult = checkParam(req);
		if (checkReult != null) {
			return checkReult;
		}
		checkReult = checkQuestionnaireId(req);
		if (checkReult != null) {
			return checkReult;
		}
		Optional<Questionnaire>qnOp = qnDao.findById(req.getQuestionnaire().getId());
		if(qnOp.isEmpty()) {
			return new QuizRes(RtnCode.QUESTIONNAIRE_ID_NOT_FOUND);
		}
		Questionnaire qn = qnOp.get();
		//�i�ק�ɶ�
		//1.�|���o��:is-published ==false
		//2.�w�o�����|���}�l�i��:is-published == true ��e�ɶ������p��stare_date
		if(!qn.isPublish()||qn.isPublish()&&LocalDate.now().isBefore(qn.getStartDate())) {
			qnDao.save(req.getQuestionnaire());
			quDao.saveAll(req.getQuestionList());
			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		
		return new QuizRes(RtnCode.UPDATE_ERROR);
	}

	private QuizRes checkQuestionnaireId(QuizReq req) {
		if (req.getQuestionnaire().getId() <= 0) {
			return new QuizRes(RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
		}
		List<Question> quList = req.getQuestionList();
		for (Question qu : quList) {
			if (qu.getQuId() != req.getQuestionnaire().getId()) {
				return new QuizRes(RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
			}
		}

		return null;
	}

	@Override
	@Transactional
	public QuizRes deleteQuestionnaire(List<Integer> qnIdList) {
		List<Questionnaire>qnList=qnDao.findByIdIn(qnIdList); 
		List<Integer> idList = new ArrayList<>();
		for(Questionnaire qn:qnList) {
			if(!qn.isPublish()||qn.isPublish()&&LocalDate.now().isBefore(qn.getStartDate())){
				//qnDao.deleteById(qn.getId());
				idList.add(qn.getId());
			}
		}
		if(!idList.isEmpty()) {
			//�R���ݨ�
			qnDao.deleteAllById(idList);
			//�R�ݨ��D��
			quDao.deleteAllByQnIdIn(idList);;
		}
		
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	@Transactional
	public QuizRes deleteQuestion(int qnId, List<Integer> quIdList) {
		Optional<Questionnaire> qnOp = qnDao.findById(qnId);
		if(qnOp.isEmpty()) {
			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		Questionnaire qn = qnOp.get();
		if(!qn.isPublish()||qn.isPublish()&&LocalDate.now().isBefore(qn.getStartDate())) {
			quDao.deleteAllByQnIdAndQuIdIn(qnId,quIdList);
		}
		return new QuizRes(RtnCode.SUCCESSFUL);
		
	}
	
	@Cacheable(cacheNames = "search",
			//key = #title_#startDate_#endDate
			//key="test_2023-11-10_2023-11-30"
			key = "#title.concat('_').concat(#startDate.toString()).concat('_').concat(#endDate.toString())",
			unless = "#result.rtnCode.code !=200")
	@Override
	public QuizRes search(String title, LocalDate startDate, LocalDate endDate) {
		//���䬰�P�_��? :�k�䬰value �Y���F��,��^title �S���F���^�Ŧr��
		title = StringUtils.hasText(title)?title:"";
		startDate = startDate != null?startDate:LocalDate.of(1971, 1, 1);
		endDate = endDate != null?endDate:LocalDate.of(2099, 12, 31);
	    //��ݨ��C��
		List<Questionnaire> qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
		//��X�ݨ��D��
		List<Integer>qnIds = new ArrayList<>();
		for(Questionnaire qu:qnList) {
			qnIds.add(qu.getId());			
		}
		//qnIds���Ůɷ|����
//		List<Question> quList = quDao.findAllByQnIdIn(qnIds);
		List<Question> quList = new ArrayList<>();
		if (!qnIds.isEmpty()) {
			quList = quDao.findAllByQnIdIn(qnIds);
		}
		 
		List<QuizVo>quizVoList = new ArrayList<>();
		//����ӦC��,����qn(�����ݨ��W�٤~���ݨ��D��)
		for(Questionnaire qn:qnList) {
			QuizVo vo = new QuizVo();
			//��:�ݨ��W��
			vo.setQuestionnaire(qn);
			List<Question> questionList= new ArrayList<>();
			for(Question qu:quList) {
				if(qu.getQnId()==qn.getId()) {
					questionList.add(qu);
				}
			}
			vo.setQuestionList(questionList);
			quizVoList.add(vo);
		}
				
//		if(!StringUtils.hasText(title)) {
//			title="";
//		}
//		if(startDate == null) {
//			startDate = LocalDate.of(1971, 1, 1);
//		}if(endDate == null) {
//		    endDate = LocalDate.of(2099, 12, 31);
//		}
		return new QuizRes(quizVoList ,RtnCode.SUCCESSFUL);
	}

	@Override
	public QuestionnaireRes searchQuestionnaireList(String title, LocalDate startDate, LocalDate endDate,
			boolean isPublish) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuestionRes searchQuestionList(int qnId) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public QuizRes selectFuzzy(String title, LocalDate startDate, LocalDate endDate) {
		List<QnQuVo> res = qnDao.selectFuzzy(title, startDate,endDate) ;
		return new QuizRes(null,res,RtnCode.SUCCESSFUL);
	}

	

//	@Override
//	public QuestionnaireRes searchQuestionnaireList(String title, LocalDate startDate, LocalDate endDate,boolean isAll) {
//		title = StringUtils.hasText(title)?title:"";
//		startDate = startDate != null?startDate:LocalDate.of(1971, 1, 1);
//		endDate = endDate != null?endDate:LocalDate.of(2099, 12, 31);
	    //��ݨ��C��
//		List<Questionnaire> qnList = new ArrayList<>();
//		if(isPublish) {
//			qnList = qnDao.findByTitleContainingAndStarDateGreaterThanEqualAndEndDateLessThanEquaAndPublishTrue(title, startDate, endDate);
//		}else {
//			qnList = qnDao.findByTitleContainingAndStarDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
//		}
//
//	
//		
//		return new QuestionRes(quList ,RtnCode.SUCCESSFUL);
//	
//	}
//
//	@Override
//	public QuestionRes searchQuestionList(int qnId) {
//		if(qnId<=0) {
//			return new QuestionRes(null ,RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
//		}
//		List<Question> quList = quDao.findAllByQnIdIn(Arrays.asList(qnId));
//		return new QuestionRes(quList ,RtnCode.SUCCESSFUL);
//	}
	}
	
	
	


