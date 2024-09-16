package com.aashisKumarBajpai.GradeBook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;

import com.aashisKumarBajpai.GradeBook.models.CollegeStudent;
import com.aashisKumarBajpai.GradeBook.models.GradebookCollegeStudent;
import com.aashisKumarBajpai.GradeBook.repository.MathGradeDao;
import com.aashisKumarBajpai.GradeBook.repository.StudentDao;
import com.aashisKumarBajpai.GradeBook.service.StudentAndGradeService;


@TestPropertySource("/application-test.properties")
@SpringBootTest(classes = GradeBookApplication.class)
@AutoConfigureMockMvc
public class GradeControllerTest {

	static public MockHttpServletRequest request;

	@Autowired // here we inject the mockmvc
	private MockMvc mockMvc;

	@Autowired
	StudentAndGradeService studentService;

	@Mock
	private StudentAndGradeService StudentCreateServiceMock;

	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private StudentDao studentDao;
	@Autowired
	private MathGradeDao mathGradeDao;
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

	@BeforeAll
	public static void setUpMockRequest() {
		request = new MockHttpServletRequest();
		request.setParameter("firstname", "Justin");
		request.setParameter("lastname", "mahar");
		request.setParameter("emailaddress", "justin_mahar@gmail.com");
	}

	@BeforeEach
	public void setUpDatabase() {
		jdbc.execute(createStudentString);
		jdbc.execute(createHistoryGradeString);
		jdbc.execute(createMathGradeString);
		jdbc.execute(createScienceGradeString);
	}

	@AfterEach
	public void cleanUpData() {
		jdbc.execute(deleteStudentString);
		jdbc.execute(deleteMathGradeString);
		jdbc.execute(deleteHistoryGradeString);
		jdbc.execute(deleteScienceGradeString);
	}

	@Test
	@DisplayName("check the GET mapping")
	public void getStudentsHttpRequest() throws Exception {
		CollegeStudent studentone = new GradebookCollegeStudent("Aashish", "Kumar", "ashishbajpai789@live.com");
		CollegeStudent studenttwo = new GradebookCollegeStudent("Deepak", "Rawat", "dr98@gmail.com");

		List<CollegeStudent> students = new ArrayList<>();
		students.add(studentone);
		students.add(studenttwo);

		when(StudentCreateServiceMock.getGradebook()).thenReturn( students);

		assertIterableEquals(students, StudentCreateServiceMock.getGradebook());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		ModelAndView modelAndView = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(modelAndView, "index");
	}

	@Test
	@DisplayName("Create a Student")
	public void checkCreationStudent() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post("/").contentType(MediaType.APPLICATION_JSON)
						.param("firstname", request.getParameterValues("firstname"))
						.param("lastname", request.getParameterValues("lastname"))
						.param("emailaddress", request.getParameterValues("emailaddress")))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		ModelAndView modelAndView = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(modelAndView, "index");
	}

	@Test
	@DisplayName("Delete a Student")
	public void deleteStudent() throws Exception {
		assertNotNull(studentDao.findById(1));

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}", 1))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		ModelAndView modelAndView = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(modelAndView, "index");
		assertFalse(studentDao.findById(1).isPresent(), "should not be there");
	}

	@Test
	@DisplayName("Error Page TESt")
	public void checkErrorPage() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}", 0))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		ModelAndView modelAndView = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(modelAndView, "error");
	}

	@Test
	@DisplayName("Student Inforamtion Test")
	public void displayStudentInformation() throws Exception {
		assertTrue(studentDao.findById(1).isPresent());
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 1))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		ModelAndView modelAndView = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(modelAndView, "studentInformation");
	}

	@Test
	@DisplayName("Student Infomation Invalid id")
	public void displayStudentInformationInvalidId() throws Exception {
		assertFalse(studentDao.findById(0).isPresent());
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 0))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		ModelAndView modelAndView = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(modelAndView, "error");
	}

	@Test
	@DisplayName("Creating a valid Grade")
	public void createValidGradeHTTPRequest() throws Exception {
		assertTrue(studentDao.findById(1).isPresent());
		GradebookCollegeStudent student = studentService.findStudentInformation(1);
		assertEquals(1, student.getStudentGrades().getMathGradeResults().size());
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post("/addGrades").contentType(MediaType.APPLICATION_JSON)
						.param("gradeValue", "80.5").param("gradeType", "math").param("studentId", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		ModelAndView modelAndView = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(modelAndView, "studentInformation");

		student = studentService.findStudentInformation(1);
		assertEquals(2, student.getStudentGrades().getMathGradeResults().size(), "Grades not added yet");
	}

	@Test
	@DisplayName("Creating a Grade for Invalid Id")
	public void createGradeForInvalidIdHTTPRequest() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post("/addGrades").contentType(MediaType.APPLICATION_JSON)
						.param("gradeValue", "80.5").param("gradeType", "math").param("studentId", "0"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		ModelAndView modelAndView = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(modelAndView, "error");
	}

	@Test
	@DisplayName("Creating grade for Invalid Subjects")
	public void createGradeForInvalidSubjectHTTPRequest() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post("/addGrades").contentType(MediaType.APPLICATION_JSON)
						.param("gradeValue", "80.5").param("gradeType", "English").param("studentId", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		ModelAndView modelAndView = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(modelAndView, "error");
	}

	@Test
	@DisplayName("Creating grade for Invalid GradeValue")
	public void createGradeForInvalidGradeVAluetHTTPRequest() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post("/addGrades").contentType(MediaType.APPLICATION_JSON)
						.param("gradeValue", "102").param("gradeType", "Math").param("studentId", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		ModelAndView modelAndView = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(modelAndView, "error");

	}

	@Test
	@DisplayName("For Deleting Grades")
	public void deleteGradesHTTPRequest() throws Exception {
		assertTrue(mathGradeDao.findById(1).isPresent());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/{id}/{gradeType}", 1, "math"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		ModelAndView modelAndView = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(modelAndView, "studentInformation");

		assertFalse(mathGradeDao.findById(1).isPresent());
	}

	@Test
	@DisplayName("DeletingGradesWithInvalidId")
	public void deleteGradesInvalidIdHTTPRequest() throws Exception {
		assertFalse(mathGradeDao.findById(0).isPresent());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/{id}/{gradeType}", 0, "math"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		ModelAndView modelAndView = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(modelAndView, "error");
	}
	@Test
	@DisplayName("DeletingGradesWithInvalidSubject")
	public void deleteGradesInvalidSubjectHTTPRequest() throws Exception {
		assertTrue(mathGradeDao.findById(1).isPresent());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/{id}/{gradeType}", 1, "English"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		ModelAndView modelAndView = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(modelAndView, "error");
	}

}
