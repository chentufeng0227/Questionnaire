package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Questionnaire;

public class QuestionnaireRes {
	
	private List<Questionnaire> QuestionnaireList;

	private RtnCode rtnCode ;

	public QuestionnaireRes() {
		super();
	
	}

	public QuestionnaireRes(List<Questionnaire> questionnaireList, RtnCode rtnCode) {
		super();
		QuestionnaireList = questionnaireList;
		this.rtnCode = rtnCode;
	}

	public List<Questionnaire> getQuestionnaireList() {
		return QuestionnaireList;
	}

	public void setQuestionnaireList(List<Questionnaire> questionnaireList) {
		QuestionnaireList = questionnaireList;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}
	
	
}
