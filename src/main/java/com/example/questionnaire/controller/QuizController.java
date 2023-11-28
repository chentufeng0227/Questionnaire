package com.example.questionnaire.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.DeleteVo;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizSearchReq;


//提供RESTful API 
//需要依賴才能import implementation 'org.springframework.boot:spring-boot-starter-web'
@RestController
@CrossOrigin
public class QuizController {
	
	@Autowired//與Service做串接
	private QuizService service;
	//提供HTTP METHOD post(可新增刪除修改資源,這邊用意:新增資源)
	//value 為自訂義
	
	
	//方法來自QuizServiceImpl
	//@RequestBody :將外部給予key&value(JSON格式)連結進來
	@PostMapping(value = "/api/quiz/create")
	public QuizRes create(@RequestBody QuizReq req) {
		return service.create(req);
	}
	
	@GetMapping(value = "/api/quiz/search1")
	public QuizRes search(@RequestBody QuizSearchReq req) {
		//左邊為判斷式? :右邊為value 若有東西,返回title 沒有東西返回空字串
		String title = StringUtils.hasText(req.getTitle())?req.getTitle():"";
		LocalDate startDate =req.getStartDate()!=null?req.getStartDate():LocalDate.of(1971, 1, 1);
		LocalDate endDate = req.getEndDate()!=null?req.getEndDate():LocalDate.of(2099,12,13);
		return service.search(title,startDate,endDate);
	
		}
//	@GetMapping(value = "/api/quiz/search")
//	public QuizRes search(
//			@RequestParam( value = "title",required = false,defaultValue = "")String title,
//			@RequestParam(value = "start_date",required = false)@DaterTimeFormat(iso=DateTimeFormat.ISO.DATE)
//		//左邊為判斷式? :右邊為value 若有東西,返回title 沒有東西返回空字串
//		String title = StringUtils.hasText(req.getTitle())?req.getTitle():"";
//		LocalDate startDate =req.getStartDate()!=null?req.getStartDate():LocalDate.of(1971, 1, 1);
//		LocalDate endDate = req.getEndDate()!=null?req.getEndDate():LocalDate.of(2099,12,13);
//		return service.search(title,startDate,endDate);
	
		
	
	@PostMapping(value = "/api/quiz/update")
	public QuizRes update(@RequestBody QuizReq req) {
		QuizRes res = service.update(req);
		res = new QuizRes (res.getQuizVoList(),RtnCode.SUCCESSFUL);
		return service.update(req);
		//mapping 屬性,JSON格式要變成字串"title":"t",
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
	
	
	
	
	

}