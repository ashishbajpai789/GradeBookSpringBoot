package com.aashisKumarBajpai.GradeBook.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;



@Component
public class Gradebook {

    private List<GradebookCollegeStudent> students = new ArrayList<>();

    public Gradebook() {

    }
    public Gradebook(List<GradebookCollegeStudent> students) {
        this.students = students;
    }

    public List<GradebookCollegeStudent> getStudents() {
        return students;
    }

    public void setStudents(List<GradebookCollegeStudent> students) {
        this.students = students;
    }
}
