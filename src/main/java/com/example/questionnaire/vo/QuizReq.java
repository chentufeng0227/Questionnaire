package com.example.questionnaire.vo;

import java.util.ArrayList;
import java.util.List;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;

//�ШD
//�]��k����QuizVo�@�� �����ϥ�QuizVo�Y�i
public class QuizReq extends QuizVo{
	
	Questionnaire questionnaire= new Questionnaire();
	List<Question> questionList= new ArrayList<>();
	
	public QuizReq() {
		super();
	}

	public QuizReq(Questionnaire questionnaire, List<Question> questionList) {
		super();
		this.questionnaire = questionnaire;
		this.questionList = questionList;
	}

	


	
	
	
	
	
	

}
