package com.example.questionnaire.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.QuestionId;

@Repository
public interface QuestionDao extends JpaRepository<Question,QuestionId>{
	//deleteByQnId表單一個 deleteByQnId表多個
	public void deleteAllByQnIdIn(List<Integer> qnIdList);
	
	public List<Question> deleteAllByQnIdAndQuIdIn(int qnId, List<Integer> quIdList);
	
	public List<Question> findByQuIdInAndQnId(List<Integer> idList,int qnId);
	
	public List<Question> findAllByQnIdIn(List<Integer> qnIdList);
	
	public List<Question> findByQnId(int qnId);
	
//此方法做測試會報錯,因為有pk	
//	@Query(value = "insert into questionnaire(id,qn_id,q_title,option_type,is_necessary,q_option)"
//			+ "values(?1,?2,?3,?4,?5,?6)",nativeQuery = true)
//	public int insert(int id, int qnId, String qTitle, String optionType, boolean isNecessary, String qOption);
	


}
