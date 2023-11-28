package com.example.questionnaire.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="questionnaire")
public class Questionnaire {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "is_publish")
	private boolean publish;
	
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@Column(name = "end_date")
	private LocalDate endDate;

	
	

	public Questionnaire() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Questionnaire(int id, String title, String description, boolean publish, LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated constructor stub
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.publish = publish;
		this.startDate = startDate;
		this.endDate = endDate;
	}
 
	public Questionnaire(String title, String description, boolean publish, LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated constructor stub
		super();
		this.title = title;
		this.description = description;
		this.publish = publish;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}
	
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	
	

}
