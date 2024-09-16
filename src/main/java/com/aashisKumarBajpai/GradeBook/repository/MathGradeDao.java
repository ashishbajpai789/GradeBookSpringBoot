package com.aashisKumarBajpai.GradeBook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aashisKumarBajpai.GradeBook.models.MathGrade;



@Repository
public interface MathGradeDao extends CrudRepository<MathGrade, Integer> {
	 public Iterable<MathGrade> findByStudentId(int studentId);

	public void deleteByStudentId(int id);
	 
}
