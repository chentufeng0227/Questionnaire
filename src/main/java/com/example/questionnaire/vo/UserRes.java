package com.example.questionnaire.vo;

import com.example.questionnaire.constants.RtnCode;

public class UserRes {

	private RtnCode rtncode;


	public RtnCode getRtncode() {
		return rtncode;
	}

	public void setRtncode(RtnCode rtncode) {
		this.rtncode = rtncode;
	}

	public UserRes(RtnCode rtncode) {
		super();
		this.rtncode = rtncode;
	}
	
	
}
