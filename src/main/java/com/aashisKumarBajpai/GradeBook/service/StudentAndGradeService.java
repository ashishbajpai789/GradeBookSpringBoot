package com.aashisKumarBajpai.GradeBook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.aashisKumarBajpai.GradeBook.models.CollegeStudent;
import com.aashisKumarBajpai.GradeBook.models.Grade;
import com.aashisKumarBajpai.GradeBook.models.GradebookCollegeStudent;
import com.aashisKumarBajpai.GradeBook.models.HistoryGrade;
import com.aashisKumarBajpai.GradeBook.models.MathGrade;
import com.aashisKumarBajpai.GradeBook.models.ScienceGrade;
import com.aashisKumarBajpai.GradeBook.models.StudentGrades;
import com.aashisKumarBajpai.GradeBook.repository.HistoryGradeDao;
import com.aashisKumarBajpai.GradeBook.repository.MathGradeDao;
import com.aashisKumarBajpai.GradeBook.repository.ScienceGradeDao;
import com.aashisKumarBajpai.GradeBook.repository.StudentDao;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class StudentAndGradeService {

	@Autowired
	private StudentDao studentDao;
	@Autowired
	@Qualifier("mathGrades")
	private MathGrade mathGrade;
	@Autowired
	@Qualifier("scienceGrades")
	private ScienceGrade scienceGrade;
	@Autowired
	@Qualifier("historyGrades")
	private HistoryGrade historyGrade;
	@Autowired
	private MathGradeDao mathGradeDao;
	@Autowired
	private ScienceGradeDao scienceGradeDao;
	@Autowired
	private HistoryGradeDao historyGradeDao;
	@Autowired
	private StudentGrades studentGrades;

	public void createStudent(String fname, String lname, String emailId) {
		CollegeStudent student = new CollegeStudent(fname, lname, emailId);
		student.setId(0);
		studentDao.save(student);
	}

	public boolean checkStudentIsNull(int id) {

		Optional<CollegeStudent> student = studentDao.findById(id);
		if (student.isPresent()) {
			return true;
		}
		return false;
	}

	public void deleteStudent(CollegeStudent studentToBeDeletedOptional) {
		// TODO Auto-generated method stub
		studentDao.delete(studentToBeDeletedOptional);

	}

	public void deleteById(int id) {
		if (checkStudentIsNull(id)) {
			studentDao.deleteById(id);
			mathGradeDao.deleteByStudentId(id);

			historyGradeDao.deleteByStudentId(id);
			scienceGradeDao.deleteByStudentId(id);
		}
	}

	public Iterable<CollegeStudent> getGradebook() {
		// TODO Auto-generated method stub
		Iterable<CollegeStudent> iterableCollegeStudentesIterable = studentDao.findAll();
		return iterableCollegeStudentesIterable;
	}

	public boolean createGrade(double grade, int studentId, String gradeType) {
		if (!checkStudentIsNull(studentId))
			return false;
		if (grade > 0 && grade <= 100) {
			if (gradeType.equalsIgnoreCase("math")) {
				mathGrade.setStudentId(studentId);
				mathGrade.setGrade(grade);
				mathGrade.setId(0);
				mathGradeDao.save(mathGrade);
				return true;
			}

			if (gradeType.equalsIgnoreCase("science")) {
				scienceGrade.setStudentId(studentId);
				scienceGrade.setGrade(grade);
				scienceGrade.setId(0);
				scienceGradeDao.save(scienceGrade);
				return true;
			}
			if (gradeType.equalsIgnoreCase("history")) {
				historyGrade.setStudentId(studentId);
				historyGrade.setGrade(grade);
				historyGrade.setId(0);
				historyGradeDao.save(historyGrade);
				return true;
			}

		}

		return false;
	}

	public boolean deleteGrade(int studentId, String gradeType) {
		// TODO Auto-generated method stub
		if (checkStudentIsNull(studentId)) {
			if (gradeType.equalsIgnoreCase("math")) {
				Iterable<MathGrade> mathGrades = mathGradeDao.findByStudentId(studentId);
				mathGradeDao.deleteAll(mathGrades);
				return true;
			}
			if (gradeType.equalsIgnoreCase("science")) {
				Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findByStudentId(studentId);
				scienceGradeDao.deleteAll(scienceGrades);
				return true;
			}
			if (gradeType.equalsIgnoreCase("history")) {
				Iterable<HistoryGrade> historyGrades = historyGradeDao.findByStudentId(studentId);
				historyGradeDao.deleteAll(historyGrades);
				return true;
			}
		}
		return false;
	}

	public int deleteSingleGrade(int gradeId, String gradeType) {
		int studentId = 0;
		if (gradeType.equalsIgnoreCase("math")) {
			Optional<MathGrade> mathOptional = mathGradeDao.findById(gradeId);
			if (mathOptional.isPresent()) {
				studentId = mathOptional.get().getStudentId();
				mathGradeDao.deleteById(gradeId);

			}

		}
		if (gradeType.equalsIgnoreCase("science")) {
			Optional<ScienceGrade> scienceOptional = scienceGradeDao.findById(gradeId);
			if (scienceOptional.isPresent()) {
				studentId = scienceOptional.get().getStudentId();
				scienceGradeDao.deleteById(gradeId);

			}
		}
		if (gradeType.equalsIgnoreCase("history")) {
			Optional<HistoryGrade> historyOptional = historyGradeDao.findById(gradeId);
			if (historyOptional.isPresent()) {
				studentId = historyOptional.get().getStudentId();
				historyGradeDao.deleteById(gradeId);
				return studentId;
			}
		}
		return studentId;
	}

	public GradebookCollegeStudent findStudentInformation(int id) {
		// TODO Auto-generated method stub
		if (!checkStudentIsNull(id))
			return null;
		else {
			Optional<CollegeStudent> student = studentDao.findById(id);

			Iterable<MathGrade> mathGrades = mathGradeDao.findByStudentId(id);
			List<Grade> mathGradesList = new ArrayList<>();
			mathGrades.forEach(mathGradesList::add);

			Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findByStudentId(id);
			List<Grade> scienceGradesList = new ArrayList<>();
			scienceGrades.forEach(scienceGradesList::add);

			Iterable<HistoryGrade> historyGrades = historyGradeDao.findByStudentId(id);
			List<Grade> historyGradeList = new ArrayList<>();
			historyGrades.forEach(historyGradeList::add);

			studentGrades.setMathGradeResults(mathGradesList);
			studentGrades.setHistoryGradeResults(historyGradeList);
			studentGrades.setScienceGradeResults(scienceGradesList);
			GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(student.get().getId(),
					student.get().getFirstname(), student.get().getLastname(), student.get().getEmailAddress(),
					studentGrades);
			return gradebookCollegeStudent;
		}

	}

	public void configureStudentDetails(int studentId, Model m) {
		GradebookCollegeStudent student = findStudentInformation(studentId);
		m.addAttribute("student", student);
		if (student.getStudentGrades().getMathGradeResults().size() > 0) {
			m.addAttribute("MathAverage",
					student.getStudentGrades().findGradePointAverage(student.getStudentGrades().getMathGradeResults()));
		} else {
			m.addAttribute("MathAverage", "N/A");
		}
		if (student.getStudentGrades().getScienceGradeResults().size() > 0) {
			m.addAttribute("ScienceAverage", student.getStudentGrades()
					.findGradePointAverage(student.getStudentGrades().getScienceGradeResults()));
		} else {
			m.addAttribute("ScienceAverage", "N/A");
		}
		if (student.getStudentGrades().getHistoryGradeResults().size() > 0) {
			m.addAttribute("HistoryAverage", student.getStudentGrades()
					.findGradePointAverage(student.getStudentGrades().getHistoryGradeResults()));
		} else {
			m.addAttribute("HistoryAverage", "N/A");
		}
	}

}
