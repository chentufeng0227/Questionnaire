package com.example.questionnaire.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.User;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.DeleteVo;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizSearchReq;
import com.example.questionnaire.vo.UserRes;


//����RESTful API 
//�ݭn�̿�~��import implementation 'org.springframework.boot:spring-boot-starter-web'
@RestController
@CrossOrigin
public class QuizController {
	
	@Autowired//�PService���걵
	private QuizService service;
	//����HTTP METHOD post(�i�s�W�R���ק�귽,�o��ηN:�s�W�귽)
	//value ���ۭq�q
	
	
	//��k�Ӧ�QuizServiceImpl
	//@RequestBody :�N�~������key&value(JSON�榡)�s���i��
	@PostMapping(value = "/api/quiz/create")
	public QuizRes create(@RequestBody QuizReq req) {
		return service.create(req);
	}
	
	@GetMapping(value = "/api/quiz/search1")
	public QuizRes search(@RequestBody QuizSearchReq req) {
		//���䬰�P�_��? :�k�䬰value �Y���F��,��^title �S���F���^�Ŧr��
		String title = StringUtils.hasText(req.getTitle())?req.getTitle():"";
		LocalDate startDate =req.getStartDate()!=null?req.getStartDate():LocalDate.of(1971, 1, 1);
		LocalDate endDate = req.getEndDate()!=null?req.getEndDate():LocalDate.of(2099,12,13);
		return service.search(title,startDate,endDate);
	
		}

	@GetMapping(value = "api/quiz/search")               
	public QuizRes search(
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
		   	@RequestParam(value = "startDate", required = false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
		    @RequestParam(value = "endDate", required = false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate) {
		title = StringUtils.hasText(title) ? title : "";
		startDate = startDate != null ? startDate : LocalDate.of(1971,01,01);
		endDate = endDate != null ? endDate : LocalDate.of(2099,01,01);
		return service.search(title, startDate,endDate);
				}

	
		
	
	@PostMapping(value = "/api/quiz/update")
	public QuizRes update(@RequestBody QuizReq req) {
		QuizRes res = service.update(req);
		return  new QuizRes (res.getQuizVoList(),res.getRtnCode());
		//mapping �ݩ�,JSON�榡�n�ܦ��r��"title":"t",
		}
	@PostMapping(value = "/api/quiz/deleteQuestion")
	public QuizRes deleteQuestion(@RequestBody DeleteVo delReq) {
		QuizRes delres = service.deleteQuestion(delReq.getQnId(),delReq.getQuIdList()); 
		return new QuizRes(delres.getRtnCode()) ;
		}
	@PostMapping(value = "/api/quiz/deleteQuestionnaire")
	public QuizRes deleteQuestionnaire(@RequestParam List<Integer> qnIdList) {
		QuizRes del = service.deleteQuestionnaire(qnIdList);
		return new QuizRes(del.getRtnCode());
		}
	@GetMapping(value = "api/quiz/searchAllId") 
	public QuizRes searchAllId(@RequestParam int id){
		return service.searchAllId(id);
			}

	@GetMapping(value = "api/quiz/frontSearchList")               
	public QuizRes frontSearchList(
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
		   	@RequestParam(value = "startDate", required = false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
		    @RequestParam(value = "endDate", required = false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate) {
		title = StringUtils.hasText(title) ? title : "";
		startDate = startDate != null ? startDate : LocalDate.of(1971,01,01);
		endDate = endDate != null ? endDate : LocalDate.of(2099,01,01);
			return service.frontSearchList(title, startDate,endDate);
				}
	
	@PostMapping(value = "api/user/adduser")
	public UserRes addUser(@RequestBody User user) {
		
		
		return service.addUser(user);
		
	}
	
	@GetMapping(value = "api/user/searchAnsPeople") 
	public List<User> searchAnsPeople(
			@RequestParam(value = "qnId", required = true)int qnId){
		return service.searchAnsPeople(qnId);
	}
	
	
	

}