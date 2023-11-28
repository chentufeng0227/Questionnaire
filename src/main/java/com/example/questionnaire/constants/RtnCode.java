package com.example.questionnaire.constants;

public enum RtnCode {
	
	SUCCESSFUL(200,"Success"),//
	QUESTION_PARAM_ERROR(400,"Question Param Error"),//
	QUESTIONNAIRE_PARAM_ERROR(400,"Questionnaire Param Error"),//
	QUESTIONNAIRE_ID_PARAM_ERROR(400,"Questionnaire Param Error"),//
	//不寫NOT FOUND是因為常用在資料庫撈不到資料
	QUESTIONNAIRE_ID_NOT_FOUND(404,"Questionnaire Is Not Found"),//
	UPDATE_ERROR(400,"Question Param Error"),//
	;
	
	private int code;
	private String message;
	//不用建構方法
	private RtnCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	//不用setName
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	
	
	
	
	
	
	
}
