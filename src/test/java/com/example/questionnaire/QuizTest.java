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
		//	1.建立開始與結束時間
		//	parse 字串轉成 LocalDate
		LocalDate date1 = LocalDate.parse("2023-06-29");
		LocalDate date2 = LocalDate.parse("2023-11-29");
		//	2.new 問卷物件 
		//	表單Questionnaire 已有建立建構方法
		Questionnaire questionnaire = new Questionnaire("台南人最愛飲料排行榜","50嵐",true,date1,date2);
		//	3.先建立問卷流水號
		int naireId = 1;
		//	4.new 問題物件 
		Question question1 = new Question(1, naireId,"測試問題1", "單選", true, "一個選項");
		//5.Arrays.asList將Array陣列型態轉轉成「List」，其屬於「static」方法。
		//https://stackoverflow.com/questions/5134466/how-to-cast-arraylist-from-list
		List<Question> questions = new ArrayList<>(Arrays.asList(question1));
		//	5.一個問卷可以有很多個問題,所以使用陣列,並用add新增問題
		//	6.建立新問題2
		Question question2 = new Question(2, naireId,"測試問題2", "多選", true, "四個選項");
		//7.將問題2加入陣列
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
	
	//此方法做測試,因為有pk
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
	//會報錯 NULL@Test 
//	public void selectFuzzyTest() {
//		QuizRes res = quizService.selectFuzzy("test", LocalDate.of(1979, 11, 1),LocalDate.of(2099, 12, 1));
//		System.out.println(res.getQuizVoList().size());
//
//	}
	
}
