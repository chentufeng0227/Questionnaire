package com.example.questionnaire.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.vo.QnQuVo;
import com.example.questionnaire.vo.QuizRes;

@Repository
public interface QuestionnaireDao extends JpaRepository <Questionnaire, Integer>{

	
	public List<Questionnaire> findByIdIn(List<Integer> idList);

	public List<Questionnaire> findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(String title,LocalDate startDate,LocalDate endDate);
	
	public List<Questionnaire> findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishTrue(String title,LocalDate startDate,LocalDate endDate);
	
	
	//squl insert語法 
	//字串裡要包含字串使用''
	//每次呼叫都為同筆資料@Query("insert into questionnaire values('qu_01','qa',true,'2023-11-24','2024-01-01')")
	//@Modifying
	//@Transactional
	//@Query(value = "insert into questionnaire(title,description,is_publish,start_date,end_date)"//括號內為欄為名稱
			//+ "values(:title,:desp,:isPublish,:startDate,:endDate)",nativeQuery = true)//VALUE括號內對應的是@Param的參數!
	
	//public int insertDate(@Param("title")String title, //
					//@Param("desp")String description, //
					//@Param("isPublish")boolean ispublish, //
					//@Param("startDate")LocalDate startDate, //
					//@Param("endDate")LocalDate endDate);
	//寫法2:簡易寫法
	@Modifying
	@Transactional
	@Query(value = "insert into questionnaire(title,description,is_publish,start_date,end_date)"//括號內為欄為名稱
			+ "values(?1,?2,?3,?4,?5)",nativeQuery = true)//VALUE括號內對應的是"insert into questionnaire"的位置!
	
	public int insertDate(
			String title, //
			String description, //
			boolean ispublish, //
			LocalDate startDate, //
			LocalDate endDate);
	//-------------------------------------------------------------
	//update
	
	@Modifying
	@Transactional
	@Query(value ="update questionnaire set title = :title,description = :desp where id = :id" ,nativeQuery = true)
	public int update(
		@Param("id")int id,//
		@Param("title")String title,//
		@Param("desp")String description);
	/*不寫nativeQuery等同於nativeQuery=false \N
	 * 語法中的表的名稱要變成ENTITY 的CLASS名稱:欄位名稱要變成屬性名稱
	 * clearAutomatically 清除暫存資料
	 * */
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value ="update Questionnaire set title = :title,description = :desp,startDate = :startDate"
			+ " where id = :id" )
	public int updateDate(
		@Param("id")int id,//
		@Param("title")String title,//
		@Param("desp")String description,//
		@Param("startDate")LocalDate startDate);
	
	//select
	
	@Query(value ="select * from questionnaire where start_date > :startDate",nativeQuery = true)
	public List<Questionnaire>findByStartDate(
			@Param("startDate")LocalDate startDate);
	@Query(value ="select new Questionnaire (id,title,description,"
			+ "publish,startDate,endDate) from Questionnaire where startDate > :startDate")
	public List<Questionnaire>findByStartDate1(@Param("startDate")LocalDate startDate);
	
	//使用別名.語法AS別名
	@Query(value ="select qu from Questionnaire as qu where startDate > :startDate or publish = :ispublish")
	public List<Questionnaire>findByStartDate3(@Param("startDate")LocalDate startDate,//
			@Param("ispublish")boolean ispublish);
	
	//order by
	@Query(value ="select qu from Questionnaire as qu where startDate > :startDate or publish = :ispublish order by id desc")
	public List<Questionnaire>findByStartDate4(
			@Param("startDate")LocalDate startDate,//
			@Param("ispublish")boolean ispublish);
	
	//order by +limit 
	//1.limit 語法只能用在nativeQuery = true
	//2.limit要用在語法的最後
	@Query(value ="select * from questionnaire as qu where start_date > :startDate or is_publish = :ispublish order by id desc limit :num",nativeQuery = true)
	public List<Questionnaire>findByStartDate5(
				@Param("startDate")LocalDate startDate,//
				@Param("ispublish")boolean ispublish,//
				@Param("num") int limtnum);
	
	@Query(value = "select * from questionnaire limit :startIndex,:limitNum",nativeQuery = true)
	public List<Questionnaire> findWithLimitAndStartIndex(
				@Param("startIndex")int startIndex,//
				@Param("limitNum")int limitNum);
	
	
	//like 不用%可以改用regexp(等同模糊搜尋)
	//regexp
	@Query(value = "select * from questionnaire where title regexp :title",nativeQuery = true)	
	public List<Questionnaire> searchTitleLike(@Param("title")String title);
	
	@Query(value = "select * from questionnaire where description regexp :keyword1|:keyword2",nativeQuery = true)
	//兩種方法都可以
	//@Query(value = "select * from questionnaire where description regexp concat(:keyword1,'|',:keyword2)",nativeQuery = true)
	public List<Questionnaire> searchDescriptionContaining(
			@Param("keyword1")String keyword1,//
			@Param("keyword2")String keyword2);
	//-------------------------------------------------------------
	//join
	
	//
	//因無spring託管 須給路徑
	@Query("select new com.example.questionnaire.vo.QnQuVo("
			+ "qn.id,qn.title,qn.description,qn.publish,qn.startDate,qn.endDate,"
			+ "q.quId,q.qTitle,q.optionType,q.necessary,q.option) "
			+ "from Questionnaire as qn join Question as q on qn.id = q.qnId")
	public List<QnQuVo> selectJoinQnQu();
	
	@Query("select new com.example.questionnaire.vo.QnQuVo("
			+ "qn.id,qn.title,qn.description,qn.publish,qn.startDate,qn.endDate,"
			+ "q.quId,q.qTitle,q.optionType,q.necessary,q.option) "
			+ "from Questionnaire as qn join Question as q on qn.id = q.qnId "
			+ "where qn.title like %:title% and qn.startDate >= :startDate and qn.endDate <=:endDate")
	public List<QnQuVo> selectFuzzy(
			@Param("title")String title,//
			@Param("startDate")LocalDate startDate,//
			@Param("endDate")LocalDate endDate);
	
}
