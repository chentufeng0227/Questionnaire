package com.example.questionnaire.vo;

import java.util.ArrayList;
import java.util.List;


import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.entity.User;

public class AnsVo {

private Questionnaire questionnaire= new Questionnaire();; 
	
	private List<User> questionList= new ArrayList<>();

	public AnsVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AnsVo(Questionnaire questionnaire, List<User> questionList) {
		super();
		this.questionnaire = questionnaire;
		this.questionList = questionList;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	public List<User> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<User> questionList) {
		this.questionList = questionList;
	}
	
	
}
