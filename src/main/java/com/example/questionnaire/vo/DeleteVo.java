package com.example.questionnaire.vo;

import java.util.List;

public class DeleteVo {
	//哪一張問卷
	private int qnId;
	//問卷裡的題目
	private List<Integer> quIdList;

	public DeleteVo() {
		super();
		// TODO Auto-generated constructor stub
		
	}
	//刪除問卷裡的問題
	public DeleteVo(int qnId, List<Integer> quIdList) {
		super();
		this.qnId = qnId;
		this.quIdList = quIdList;
	}
	//刪除問題
	public DeleteVo(int qnId) {
		super();
		this.qnId = qnId;
	}

	public int getQnId() {
		return qnId;
	}

	public void setQnId(int qnId) {
		this.qnId = qnId;
	}

	public List<Integer> getQuIdList() {
		return quIdList;
	}

	public void setQuIdList(List<Integer> quIdList) {
		this.quIdList = quIdList;
	}
	
	
}
