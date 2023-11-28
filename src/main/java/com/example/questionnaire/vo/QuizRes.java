package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.constants.RtnCode;

public class QuizRes {
	
	//�@�i�ݨ��h���D�� �D�إ[�Wlist ���򤣥u�@�i�ݨ�,�ݨ��ϥ�list
	private List<QuizVo> QuizVoList;
	
	private List<QnQuVo> qnQuVoList;
	
	private RtnCode rtnCode ;
	

	public QuizRes(List<QuizVo> quizVoList) {
		super();
		QuizVoList = quizVoList;
	}


	public QuizRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public QuizRes(RtnCode rtnCode) {
		super();
		this.rtnCode = rtnCode;
	}

	public QuizRes(List<QuizVo> quizVoList, RtnCode rtnCode) {
		super();
		QuizVoList = quizVoList;
		this.rtnCode = rtnCode;
	}
	
	public QuizRes(List<QuizVo> quizVoList, List<QnQuVo> qnQuVoList, RtnCode rtnCode) {
		super();
		QuizVoList = quizVoList;
		this.qnQuVoList = qnQuVoList;
		this.rtnCode = rtnCode;
	}

	public List<QuizVo> getQuizVoList() {
		return QuizVoList;
	}


	public void setQuizVoList(List<QuizVo> quizVoList) {
		QuizVoList = quizVoList;
	}


	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}


	public List<QnQuVo> getQnQuVoList() {
		return qnQuVoList;
	}


	public void setQnQuVoList(List<QnQuVo> qnQuVoList) {
		this.qnQuVoList = qnQuVoList;
	}
	
	

}
