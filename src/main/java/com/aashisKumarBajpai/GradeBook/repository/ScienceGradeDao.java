package com.aashisKumarBajpai.GradeBook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aashisKumarBajpai.GradeBook.models.ScienceGrade;



@Repository
public interface ScienceGradeDao extends CrudRepository<ScienceGrade, Integer> {
	 public Iterable<ScienceGrade> findByStudentId(int studentId);
	 public void deleteByStudentId(int id);
}
