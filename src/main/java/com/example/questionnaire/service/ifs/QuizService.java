package com.example.questionnaire.service.ifs;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;

@Service
public interface QuizService {
	
	public QuizRes create(QuizReq req);

	public QuizRes update(QuizReq req);
	
	public QuizRes deleteQuestionnaire(List<Integer> qnIdList);
	
	//刪除同一張問卷的一或多個題目
	public QuizRes deleteQuestion(int qnId,List<Integer> quIdList);
	
	public QuizRes search(String title,LocalDate startDate,LocalDate endDate);
	
	public QuestionnaireRes searchQuestionnaireList(String title,LocalDate startDate,LocalDate endDate,boolean isPublish);
	
	//撈單張問卷的所有問題
	public QuestionRes searchQuestionList(int qnId);
	
	public QuizRes selectFuzzy(String title,LocalDate startDate,LocalDate endDate);
	
	
}
