package com.example.labb1;

public class GradeItem {
    private final String subject;
    private final String grade;

    public GradeItem(String subject, String grade) {
        this.subject = subject;
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public String getGrade() {
        return grade;
    }
}
