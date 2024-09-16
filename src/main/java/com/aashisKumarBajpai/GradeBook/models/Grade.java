package com.aashisKumarBajpai.GradeBook.models;

public interface Grade {
    double getGrade();

    int getId();

    void setId(int id);

    int getStudentId();

    void setStudentId(int studentId);

    void setGrade(double grade);
}
