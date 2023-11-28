package com.example.questionnaire.constants;

public enum RtnCode {
	
	SUCCESSFUL(200,"Success"),//
	QUESTION_PARAM_ERROR(400,"Question Param Error"),//
	QUESTIONNAIRE_PARAM_ERROR(400,"Questionnaire Param Error"),//
	QUESTIONNAIRE_ID_PARAM_ERROR(400,"Questionnaire Param Error"),//
	//���gNOT FOUND�O�]���`�Φb��Ʈw��������
	QUESTIONNAIRE_ID_NOT_FOUND(404,"Questionnaire Is Not Found"),//
	UPDATE_ERROR(400,"Question Param Error"),//
	;
	
	private int code;
	private String message;
	//���Ϋغc��k
	private RtnCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	//����setName
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	
	
	
	
	
	
	
}
