package com.aashisKumarBajpai.GradeBook.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.aashisKumarBajpai.GradeBook.models.CollegeStudent;
import com.aashisKumarBajpai.GradeBook.models.Gradebook;
import com.aashisKumarBajpai.GradeBook.service.StudentAndGradeService;

@Controller
public class GradebookController {

	@Autowired
	private Gradebook gradebook;
	@Autowired
	StudentAndGradeService studentAndGradeService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getStudents(Model m) {
		Iterable<CollegeStudent> collegeStudents = (Iterable<CollegeStudent>) studentAndGradeService.getGradebook();
		m.addAttribute("students", collegeStudents);
		return "index";
	}

	@PostMapping("/")
	public String createStudent(@ModelAttribute("student") CollegeStudent myStudent, Model m) {
		studentAndGradeService.createStudent(myStudent.getFirstname(), myStudent.getLastname(),
				myStudent.getEmailAddress());
		Iterable<CollegeStudent> students = studentAndGradeService.getGradebook();
		m.addAttribute("students", students);
		return "index";
	}

	@GetMapping("/delete/student/{id}")
	public String deleteStudent(@PathVariable int id, Model m) {
		if (!studentAndGradeService.checkStudentIsNull(id))
			return "error";
		else {
			studentAndGradeService.deleteById(id);
			Iterable<CollegeStudent> studentsIterable =  studentAndGradeService.getGradebook();
			m.addAttribute("students", studentsIterable);
			return "index";
		}
	}

	@GetMapping("/studentInformation/{id}")
	public String studentInformation(@PathVariable int id, Model m) {
		if (!studentAndGradeService.checkStudentIsNull(id))
			return "error";
		else {
			studentAndGradeService.configureStudentDetails(id, m);
			return "studentInformation";
		}
	}

	@PostMapping("/addGrades")
	public String addGrades(@RequestParam("gradeValue") double gradeValue, @RequestParam("gradeType") String gradeType,
			@RequestParam("studentId") int studentId, Model m) {
		if (!studentAndGradeService.checkStudentIsNull(studentId))
			return "error";
		boolean success = studentAndGradeService.createGrade(gradeValue, studentId, gradeType);
		if (!success)
			return "error";
		
		studentAndGradeService.configureStudentDetails(studentId, m);
		return "studentInformation";
	}

	@GetMapping("/delete/{id}/{gradeType}")
	public String deleteGradeByGradeId(@PathVariable("id") int id, @PathVariable("gradeType") String gradeType, Model m) {
		int studentId = studentAndGradeService.deleteSingleGrade(id, gradeType);
		if (studentId == 0)
			return "error";

		studentAndGradeService.configureStudentDetails(studentId, m);
		return "studentInformation";
	}
}
