package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Question;

public class QuestionRes {
	
	private List<Question> questionList;

	private RtnCode rtncode;

	public QuestionRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuestionRes(List<Question> questionList, RtnCode rtncode) {
		super();
		this.questionList = questionList;
		this.rtncode = rtncode;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}

	public RtnCode getRtncode() {
		return rtncode;
	}

	public void setRtncode(RtnCode rtncode) {
		this.rtncode = rtncode;
	}
	
	

}
