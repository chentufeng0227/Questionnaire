package com.example.questionnaire.vo;

import java.util.List;

public class DeleteVo {
	//���@�i�ݨ�
	private int qnId;
	//�ݨ��̪��D��
	private List<Integer> quIdList;

	public DeleteVo() {
		super();
		// TODO Auto-generated constructor stub
		
	}
	//�R���ݨ��̪����D
	public DeleteVo(int qnId, List<Integer> quIdList) {
		super();
		this.qnId = qnId;
		this.quIdList = quIdList;
	}
	//�R�����D
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
