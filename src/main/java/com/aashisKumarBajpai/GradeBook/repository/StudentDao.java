package com.aashisKumarBajpai.GradeBook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aashisKumarBajpai.GradeBook.models.CollegeStudent;



@Repository
public interface StudentDao extends CrudRepository<CollegeStudent, Integer> {

	CollegeStudent findByEmailAddress(String string);

}
