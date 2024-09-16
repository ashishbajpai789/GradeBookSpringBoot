package com.aashisKumarBajpai.GradeBook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import com.aashisKumarBajpai.GradeBook.models.CollegeStudent;
import com.aashisKumarBajpai.GradeBook.models.GradebookCollegeStudent;
import com.aashisKumarBajpai.GradeBook.models.HistoryGrade;
import com.aashisKumarBajpai.GradeBook.models.MathGrade;
import com.aashisKumarBajpai.GradeBook.models.ScienceGrade;
import com.aashisKumarBajpai.GradeBook.repository.HistoryGradeDao;
import com.aashisKumarBajpai.GradeBook.repository.MathGradeDao;
import com.aashisKumarBajpai.GradeBook.repository.ScienceGradeDao;
import com.aashisKumarBajpai.GradeBook.repository.StudentDao;
import com.aashisKumarBajpai.GradeBook.service.StudentAndGradeService;


@TestPropertySource("/application-test.properties")
@SpringBootTest(classes =GradeBookApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentAndGradeServiceTest {

	@Autowired
	private StudentAndGradeService studentService;
	@Autowired
	private StudentDao studentDao;
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	MathGradeDao mathGradeDao;
	@Autowired
	ScienceGradeDao scienceGradeDao;
	@Autowired
	HistoryGradeDao historyGradeDao;
	
	@Value("${sql.script.create.student}")
	private String createStudentString;
	
	@Value("${sql.script.create.math.grade}")
	private String createMathGradeString;
	@Value("${sql.script.create.science.grade}")
	private String createScienceGradeString;
	@Value("${sql.script.create.history.grade}")
	private String createHistoryGradeString;
	@Value("${sql.script.delete.student}")
	private String deleteStudentString;
	
	@Value("${sql.script.delete.math.grade}")
	private String deleteMathGradeString;
	@Value("${sql.script.delete.science.grade}")
	private String deleteScienceGradeString;
	@Value("${sql.script.delete.history.grade}")
	private String deleteHistoryGradeString;
	
	

	@DisplayName("StudentGradeServiceTest_TDD")
	@Test
	public void checkStudentService() {

		studentService.createStudent("Aashish", "Kumar", "ashishbajpai789@live.com");

		CollegeStudent student = studentDao.findByEmailAddress("ashishbajpai789@live.com");

		assertEquals("ashishbajpai789@live.com", student.getEmailAddress());
	}

	@BeforeEach
	public void setUpDatabase() {
//		jdbc.execute("insert into student(id, firstname, lastname, email_address) "
//				+ "values (1,'Neha', 'Pandita', 'npandita001@gmail.com')");
//		jdbc.execute("insert into math_grade(id,student_id, grade) " + "values(1,1,99)");
//		jdbc.execute("insert into science_grade(id,student_id, grade) " + "values(1,1,78)");
//		jdbc.execute("insert into history_grade(id,student_id, grade) " + "values(1,1,70)");
		jdbc.execute(createStudentString);
		jdbc.execute(createHistoryGradeString);
		jdbc.execute(createMathGradeString);
		jdbc.execute(createScienceGradeString);
	}

	@Test
	@DisplayName("Check for Null Student")
	public void checkIfStudentIsNull() {
		assertTrue(studentService.checkStudentIsNull(1));
		assertFalse(studentService.checkStudentIsNull(5));
	}

	@Test
	@DisplayName("DeleteStudentCheck")
	@Order(1)
	public void checkForDeletedStudent() {
		Optional<CollegeStudent> studentToBeDeleted = studentDao.findById(1);
		CollegeStudent student = studentToBeDeleted.get();
		
		Optional<MathGrade> mathGrade = mathGradeDao.findById(1);

		MathGrade mathGradeToBeDeleted = mathGrade.get();
		assertTrue(mathGrade.isPresent(), "No Grades Found");
		
		Optional<ScienceGrade> scienceGrade = scienceGradeDao.findById(1);
		ScienceGrade scienceGradeToBeDeleted = scienceGrade.get();
		assertTrue(scienceGrade.isPresent());
		
		Optional<HistoryGrade> historyGrade = historyGradeDao.findById(1);
		HistoryGrade historyGradeToBeDeleted = historyGrade.get();
		assertTrue(historyGrade.isPresent());
		
		assertTrue(studentToBeDeleted.isPresent());
		
		studentService.deleteById(1);
		mathGrade = mathGradeDao.findById(1);
		scienceGrade = scienceGradeDao.findById(1);
		historyGrade= historyGradeDao.findById(1);
		assertFalse(mathGrade.isPresent());
		assertFalse(scienceGrade.isPresent());
		assertFalse(historyGrade.isPresent());
		
		Optional<CollegeStudent> foundStudentOptional = studentDao.findById(1);
		assertFalse(foundStudentOptional.isPresent());

	}

	@Test
	@DisplayName("checkForGradeBook")
	@Order(-1)
	public void checkGradebook() {
		Iterable<CollegeStudent> iterableCollegeStudents = studentService.getGradebook();

		List<CollegeStudent> collegeStudents = new ArrayList<>();
		for (CollegeStudent student : iterableCollegeStudents) {
			collegeStudents.add(student);
		}

		assertEquals(1, collegeStudents.size());
	}

	@AfterEach
	public void cleanUpData() {
//		jdbc.execute("DELETE from student");
//		jdbc.execute("DELETE from math_grade");
//		jdbc.execute("DELETE from science_grade");
//		jdbc.execute("DELETE from history_grade");
		jdbc.execute(deleteStudentString);
		jdbc.execute(deleteMathGradeString);
		jdbc.execute(deleteHistoryGradeString);
		jdbc.execute(deleteScienceGradeString);
	}

	@DisplayName("GradeService")
	@Test
	public void checkGradeService() {
		// step 1 is to create the grade
		assertTrue(studentService.createGrade(81.5, 1, "Math"), "Grade not created");
		assertTrue(studentService.createGrade(92.5, 1, "science"));
		assertTrue(studentService.createGrade(92.5, 1, "history"));

		// step 2 is to return the grades for a particular student
		Iterable<MathGrade> mathgrades = mathGradeDao.findByStudentId(1);
		Iterable<ScienceGrade> sciencegrades = scienceGradeDao.findByStudentId(1);
		Iterable<HistoryGrade> historygrades = historyGradeDao.findByStudentId(1);
		// step 3 is to check if the returned grades exists or not
//		assertTrue(mathgrades.iterator().hasNext(), "Grade is empty");
//		assertTrue(sciencegrades.iterator().hasNext(), "Grade is empty");
//		assertTrue(historygrades.iterator().hasNext(), "Grade is empty");
		assertEquals(2, ((Collection<MathGrade>) mathgrades).size());
		assertEquals(2, ((Collection<ScienceGrade>) sciencegrades).size());
		assertEquals(2, ((Collection<HistoryGrade>) historygrades).size());
	}

	@DisplayName("checkValidGrades")
	@Test
	public void checkValidGrades() {
		assertFalse(studentService.createGrade(105, 1, "Math"));
		assertFalse(studentService.createGrade(91, 2, "Math"));
		assertFalse(studentService.createGrade(90, 1, "English"));
		assertFalse(studentService.createGrade(-12, 1, "Math"));
	}
	@DisplayName("deleteGrades")
	@Test
	public void deleteGrades() {
		assertTrue(studentService.deleteGrade(1,"Math"));
		assertTrue(studentService.deleteGrade(1,"Science"));
		assertTrue(studentService.deleteGrade(1,"History"));
		assertFalse(studentService.deleteGrade(0, "Math"));
		assertFalse(studentService.deleteGrade(0, "English"));
		Iterable<MathGrade> mathGradesIterable  = mathGradeDao.findByStudentId(1);
		assertEquals(0, ((Collection<MathGrade>)mathGradesIterable).size());
	}
	@DisplayName("Retreive Student")
	@Test
	public void displayStudent() {
		GradebookCollegeStudent collegeStudent = studentService.findStudentInformation(1);
		assertNotNull(collegeStudent);
		assertEquals(1, collegeStudent.getId());
		assertEquals("Neha", collegeStudent.getFirstname());
		assertEquals("Pandita", collegeStudent.getLastname());
		assertEquals(1, collegeStudent.getStudentGrades().getMathGradeResults().size());
		assertEquals(1, collegeStudent.getStudentGrades().getHistoryGradeResults().size());
		assertEquals(1,  collegeStudent.getStudentGrades().getScienceGradeResults().size());
	}
	@DisplayName("Return Null Student")
	@Test
	public void studentServiceReuturnsNull() {
		GradebookCollegeStudent collegeStudent = studentService.findStudentInformation(0);
		assertNull(collegeStudent);
	}
	
}
