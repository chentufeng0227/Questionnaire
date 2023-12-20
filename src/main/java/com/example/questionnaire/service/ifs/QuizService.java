package com.example.questionnaire.service.ifs;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.questionnaire.entity.User;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.UserRes;

@Service
public interface QuizService {
	
	public QuizRes create(QuizReq req);

	public QuizRes update(QuizReq req);
	
	public QuizRes deleteQuestionnaire(List<Integer> qnIdList);
	
	//�R���P�@�i�ݨ����@�Φh���D��
	public QuizRes deleteQuestion(int qnId,List<Integer> quIdList);
	
	public QuizRes search(String title,LocalDate startDate,LocalDate endDate);
	
	public QuizRes frontSearchList(String title,LocalDate startDate,LocalDate endDate);
	
	//����i�ݨ����Ҧ����D
	public QuestionRes searchQuestionList(int qnId);
	
	public QuizRes selectFuzzy(String title,LocalDate startDate,LocalDate endDate);
	
	public QuizRes searchAllId(int qnId);
	
	public UserRes addUser(User user);
	
	public List<User> searchAnsPeople(int qnId);
	
	
	
}
