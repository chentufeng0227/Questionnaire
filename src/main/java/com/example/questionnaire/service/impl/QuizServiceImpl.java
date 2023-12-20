package com.example.questionnaire.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.entity.User;
import com.example.questionnaire.repository.QuestionDao;
import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.repository.UserDao;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QnQuVo;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizVo;
import com.example.questionnaire.vo.UserRes;

@EnableScheduling
@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private QuestionnaireDao qnDao;

	@Autowired
	private QuestionDao quDao;

	// Transactional用再多張表格,不能加在私有方法上
	@Transactional
	@Override
	public QuizRes create(QuizReq req) {
		// 呼叫方法 檢查參數 一個是null=成功,另一個為res
		QuizRes checkReult = checkParam(req);
		if (checkReult != null) {
			return checkReult;
		}
		int qnId = qnDao.save(req.getQuestionnaire()).getId();
		List<Question> quList = req.getQuestionList();
		if (quList.isEmpty()) {
			return new QuizRes(RtnCode.QUESTION_PARAM_ERROR);
		}
		for (Question qu : quList) {
			qu.setQnId(qnId); 
		}
		quDao.saveAll(quList);
		return new QuizRes(RtnCode.SUCCESSFUL);

	}

	// 參數檢查 把邏輯變成私有方法,讓主程式程式碼比較簡潔
	// qn.getStartDate().isAfter(qn.getEndDate())開始時間在結束時間之後為false 回傳錯誤
	private QuizRes checkParam(QuizReq req) {
		Questionnaire qn = req.getQuestionnaire();
		if(!StringUtils.hasText(qn.getTitle())||!StringUtils.hasText(qn.getDescription())
			||qn.getStartDate() == null ||qn.getEndDate() == null||qn.getStartDate().isAfter(qn.getEndDate())){
			return new QuizRes(RtnCode.QUESTIONNAIRE_PARAM_ERROR);
		}
		
		//qu.qnId一開始沒有所以不需要
		//原始表單每次只能編輯一次,改用list後可以一次編輯多張,再使用foreach進行便歷
		List<Question> quList = req.getQuestionList();
		for(Question qu:quList) {
		if(qu.getQuId() <=0 ||!StringUtils.hasText(qn.getTitle())||
				!StringUtils.hasText(qu.getOptionType())||!StringUtils.hasText(qu.getOption())) {
			return new QuizRes(RtnCode.QUESTION_PARAM_ERROR);
		}
	}
		
		return null;
	}

	// 問卷已存在
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
		//可修改時間
		//1.尚未發布:is-published ==false
		//2.已發布但尚未開始進行:is-published == true 當前時間必須小於stare_date
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
			if (qu.getQnId() != req.getQuestionnaire().getId()) {
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
			//刪除問卷
			qnDao.deleteAllById(idList);
			//刪問卷題目
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
	
//	@Cacheable(cacheNames = "search",
//			//key = #title_#startDate_#endDate
//			//key="test_2023-11-10_2023-11-30"
//			key = "#title.concat('_').concat(#startDate.toString()).concat('_').concat(#endDate.toString())",
//			unless = "#result.rtnCode.code !=200")
	@Override
	public QuizRes search(String title, LocalDate startDate, LocalDate endDate) {
		//左邊為判斷式? :右邊為value 若有東西,返回title 沒有東西返回空字串
		title = StringUtils.hasText(title)?title:"";
		startDate = startDate != null?startDate:LocalDate.of(1971, 1, 1);
		endDate = endDate != null?endDate:LocalDate.of(2099, 12, 31);
	    //找問卷列表
		List<Questionnaire> qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
		//找出問卷題目
		List<Integer>qnIds = new ArrayList<>();
		for(Questionnaire qu:qnList) {
			qnIds.add(qu.getId());			
		}
		//qnIds為空時會報錯
//		List<Question> quList = quDao.findAllByQnIdIn(qnIds);
		List<Question> quList = new ArrayList<>();
		if (!qnIds.isEmpty()) {
			quList = quDao.findAllByQnIdIn(qnIds);
		}
		 
		List<QuizVo>quizVoList = new ArrayList<>();
		//有兩個列表,先找qn(先有問卷名稱才有問卷題目)
		for(Questionnaire qn:qnList) {
			QuizVo vo = new QuizVo();
			//裝:問卷名稱
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
	public QuizRes frontSearchList(String title, LocalDate startDate, LocalDate endDate) {
		// 左邊為判斷式? :右邊為value 若有東西,返回title 沒有東西返回空字串
		title = StringUtils.hasText(title) ? title : "";
		startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
		endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
		// 找出已開放的問卷列表,若問卷題目為空,回傳沒有此問卷
		List<Questionnaire> qnList = qnDao
				.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishTrue(title, startDate, endDate);
		if(qnList.isEmpty()) {
			return new QuizRes(RtnCode.QUESTIONNAIRE_ID_NOT_FOUND);
		}
		// 找出問卷題目
		List<Integer> qnIds = new ArrayList<>();
		for (Questionnaire qu : qnList) {
			qnIds.add(qu.getId());
		}
		List<Question> quList = new ArrayList<>();
		if (!qnIds.isEmpty()) {
			quList = quDao.findAllByQnIdIn(qnIds);
		}
		List<QuizVo> quizVoList = new ArrayList<>();
		// 有兩個列表,先找qn(先有問卷名稱才有問卷題目)
		for (Questionnaire qn : qnList) {
			QuizVo vo = new QuizVo();
			// 裝:問卷名稱
			vo.setQuestionnaire(qn);
			List<Question> questionList = new ArrayList<>();
			for (Question qu : quList) {
				if (qu.getQnId() == qn.getId()) {
					questionList.add(qu);
				}
			}
			vo.setQuestionList(questionList);
			quizVoList.add(vo);
		}

		return new QuizRes(quizVoList, RtnCode.SUCCESSFUL);

	}
	//只找問題
	@Override
	public QuestionRes searchQuestionList(int qnId) {
		if (qnId <= 0) {
			return new QuestionRes(null, RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
		}
		List<Question> quList = quDao.findAllByQnIdIn(Arrays.asList(qnId));
		return new QuestionRes(quList, RtnCode.SUCCESSFUL);
	}
	
	@Override
	public QuizRes selectFuzzy(String title, LocalDate startDate, LocalDate endDate) {
		List<QnQuVo> res = qnDao.selectFuzzy(title, startDate,endDate) ;
		return new QuizRes(null,res,RtnCode.SUCCESSFUL);
		
	}

//	@Override
//	public QuizRes searchAllId(int qnId) {
//		//找qn問卷的id
//		 Optional<Questionnaire> qnop = qnDao.findById(qnId);
//		//找qu問題的qnid
//		 List<Question> quList = quDao.findByQnId(qnId);
//		 //將qn問卷的id拿出來
//		 Questionnaire qn = qnop.get();
//		 QuizVo vo = new QuizVo();
//		 vo.setQuestionList(quList);
//		 vo.setQuestionnaire(qn);
//		 /////////////
//		 List<QuizVo> voList = new ArrayList<>();
//		voList.add(vo);
//		return new QuizRes(voList,RtnCode.SUCCESSFUL);
//		 
//		
//	}
	@Override
	public QuizRes searchAllId(int qnId) {
	    Optional<Questionnaire> qnop = qnDao.findById(qnId);
	    List<Question> quList = quDao.findByQnId(qnId);
	    List<QuizVo> voList = new ArrayList<>();

	    for (Question question : quList) {
	        // 每一個問題都建立一個對應的QuizVo
	        Questionnaire qn = qnop.get(); // 使用同一份問卷資訊
	        QuizVo vo = new QuizVo(qn, quList);
	        voList.add(vo);
	    }

	    // 返回QuizRes
	    return new QuizRes(voList, RtnCode.SUCCESSFUL);
	    }

	

	

//	@Override
//	public QuestionnaireRes searchQuestionnaireList(String title, LocalDate startDate, LocalDate endDate,boolean isAll) {
//		title = StringUtils.hasText(title)?title:"";
//		startDate = startDate != null?startDate:LocalDate.of(1971, 1, 1);
//		endDate = endDate != null?endDate:LocalDate.of(2099, 12, 31);
//	    找問卷列表
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
////                        秒	分 時 日 月 周
//		@Scheduled(cron = "* * 14 * * * ")
//		public void schedule() {
//			System.out.println(LocalDateTime.now());
//		}

		@Override
		public UserRes addUser(User user) {
			if(!StringUtils.hasText(user.getName())||user.getAge()<0){
				return new UserRes(RtnCode.UPDATE_ERROR);
			}
			userDao.save(user);
			return new UserRes(RtnCode.SUCCESSFUL);
		}

		@Override
		public List<User> searchAnsPeople(int qnId) {
			
			 
			
			return userDao.findByQnId(qnId);
		}

	
		
	}
	
	
	


