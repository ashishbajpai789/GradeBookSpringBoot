package com.aashisKumarBajpai.GradeBook;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.aashisKumarBajpai.GradeBook.models.CollegeStudent;
import com.aashisKumarBajpai.GradeBook.models.Grade;
import com.aashisKumarBajpai.GradeBook.models.HistoryGrade;
import com.aashisKumarBajpai.GradeBook.models.MathGrade;
import com.aashisKumarBajpai.GradeBook.models.ScienceGrade;

@SpringBootApplication
public class GradeBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(GradeBookApplication.class, args);
	}

	@Bean
	@Scope(value = "prototype")
	CollegeStudent getCollegeStudent() {
		return new CollegeStudent();
	}

	@Bean
	@Scope(value = "prototype")
	Grade getMathGrade(double grade) {
		return new MathGrade(grade);
	}

	@Bean
	@Scope(value = "prototype")
	@Qualifier("mathGrades")
	MathGrade getGrade() {
		return new MathGrade();
	}

	@Bean
	@Scope(value = "prototype")
	@Qualifier("scienceGrades")
	ScienceGrade getScienceGrade() {
		return new ScienceGrade();
	}

	@Bean
	@Scope(value = "prototype")
	@Qualifier("historyGrades")
	HistoryGrade getHistoryGrade() {
		return new HistoryGrade();
	}
}
