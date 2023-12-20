package com.example.questionnaire.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "num")
	private int num;
	
	
	@Column(name = "name")
	private String name;
	
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "age")
	private int age;
	
	@Column(name = "qn_id")
	private int qnId;
	
	@Column(name = "qu_id")
	private int quId;
	
	@Column(name = "ans")
	private String ans;
	
	@Column(name = "date_time")
	private LocalDateTime dateTime = LocalDateTime.now();
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}


	public User(String name, String phoneNumber, String email, int age, int qnId, int quId, String ans
			) {
		super();
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.age = age;
		this.qnId = qnId;
		this.quId = quId;
		this.ans = ans;
		
	}


	public int getNum() {
		return num;
	}


	public void setNum(int num) {
		this.num = num;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public int getQnId() {
		return qnId;
	}


	public void setQnId(int qnId) {
		this.qnId = qnId;
	}


	public int getQuId() {
		return quId;
	}


	public void setQuId(int quId) {
		this.quId = quId;
	}


	public String getAns() {
		return ans;
	}


	public void setAns(String ans) {
		this.ans = ans;
	}


	public LocalDateTime getDateTime() {
		return dateTime;
	}


	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	
	
	
	
	
	

}
