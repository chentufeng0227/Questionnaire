package com.example.questionnaire;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.repository.QuestionDao;
import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QnQuVo;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizVo;

@SpringBootTest
public class QuizTest {
	@Autowired
	QuizService quizService;
	
	@Autowired
	private QuestionnaireDao qnDao;
	
	@Autowired
	private QuestionDao quDao;




	@Test  
	public void createTest() {
		//	1.�إ߶}�l�P�����ɶ�
		//	parse �r���ন LocalDate
		LocalDate date1 = LocalDate.parse("2023-06-29");
		LocalDate date2 = LocalDate.parse("2023-11-29");
		//	2.new �ݨ����� 
		//	���Questionnaire �w���إ߫غc��k
		Questionnaire questionnaire = new Questionnaire("�x�n�H�̷R���ƱƦ�]","50�P",true,date1,date2);
		//	3.���إ߰ݨ��y����
		int naireId = 1;
		//	4.new ���D���� 
		Question question1 = new Question(1, naireId,"���հ��D1", "���", true, "�@�ӿﶵ");
		//5.Arrays.asList�NArray�}�C���A���ন�uList�v�A���ݩ�ustatic�v��k�C
		//https://stackoverflow.com/questions/5134466/how-to-cast-arraylist-from-list
		List<Question> questions = new ArrayList<>(Arrays.asList(question1));
		//	5.�@�Ӱݨ��i�H���ܦh�Ӱ��D,�ҥH�ϥΰ}�C,�å�add�s�W���D
		//	6.�إ߷s���D2
		Question question2 = new Question(2, naireId,"���հ��D2", "�h��", true, "�|�ӿﶵ");
		//7.�N���D2�[�J�}�C
		questions.add(question2);
		//8.
		QuizReq quizReq = new QuizReq(questionnaire, questions);
		QuizRes result = quizService.create(quizReq);

	}
	
	@Test 
	public void insertTest() {
		int res = qnDao.insertDate("qa_01", "qa_01 test", false, LocalDate.of(2023, 11, 23),LocalDate.of(2023, 12, 01));
		System.out.println(res);
	}
	@Test 
	public void insertTest2() {
		int res = qnDao.insertDate("qa_01", "qa_01 test", false, LocalDate.of(2023, 11, 24),LocalDate.of(2023, 12, 02));
		System.out.println(res);
	}
	
	//����k������,�]����pk
//	@Test 
//	public void insertTest3() {
//		int res = quDao.insert(1,2,"qa_01 test","single",false,"abc");
//		System.out.println(res);
//	}
	@Test 
	public void updateTest() {
		int res = qnDao.update(2,"qn_02","qa_02 test");
		System.out.println(res);
	}
	@Test 
	public void updateDateTest() {
		int res = qnDao.updateDate(2,"qn_002","qa_02 test",LocalDate.of(2023, 11, 20));
		System.out.println(res);
	}
	@Test 
	public void selectTest1() {
		//List<Questionnaire> res = qnDao.findByStartDate(LocalDate.of(2023, 11, 20));
		//List<Questionnaire> res = qnDao.findByStartDate1(LocalDate.of(2023, 11, 20));
		//List<Questionnaire> res = qnDao.findByStartDate3(LocalDate.of(2023, 11, 20),true);
		List<Questionnaire> res = qnDao.findByStartDate5(LocalDate.of(2023, 11, 20),true,2);
		System.out.println(res.size());
	}
	@Test 
	public void limitTest() {
		List<Questionnaire> res = qnDao.findWithLimitAndStartIndex(1,1);
		for(Questionnaire item:res) {
			System.out.println(item.getId());
		}
	}
	//title
	@Test 
	public void likeTest() {
		List<Questionnaire> res = qnDao.searchTitleLike("test");
		for(Questionnaire item:res) {
			System.out.println(item.getTitle());
		}
	}
	@Test 
	public void regexpTest() {
		List<Questionnaire> res = qnDao.searchDescriptionContaining("qa","qn");
		for(Questionnaire item:res) {
			System.out.println(item.getDescription());
		}
	}
	@Test 
	public void joinTest() {
		List<QnQuVo> res = qnDao.selectJoinQnQu();
		for(QnQuVo item:res) {
			System.out.printf("id: %d, title: %s,qu_id: %d\n",
			item.getId(),item.getDescription(),item.getQuId());
		}
	}
	//�|���� NULL@Test 
//	public void selectFuzzyTest() {
//		QuizRes res = quizService.selectFuzzy("test", LocalDate.of(1979, 11, 1),LocalDate.of(2099, 12, 1));
//		System.out.println(res.getQuizVoList().size());
//
//	}
	
}
