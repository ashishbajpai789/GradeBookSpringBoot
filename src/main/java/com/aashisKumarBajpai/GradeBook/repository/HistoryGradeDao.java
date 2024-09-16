package com.aashisKumarBajpai.GradeBook.repository;

import org.springframework.data.repository.CrudRepository;

import com.aashisKumarBajpai.GradeBook.models.HistoryGrade;



public interface HistoryGradeDao extends CrudRepository<HistoryGrade, Integer> {

	Iterable<HistoryGrade> findByStudentId(int i);
	public void deleteByStudentId(int id);

}
