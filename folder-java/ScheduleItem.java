package com.example.labb1;

public class ScheduleItem {
    private String courseName;
    private String startTime;
    private String endTime;
    private String room;
    private String professor;

    public ScheduleItem(String courseName, String startTime, String endTime, String room, String professor) {
        this.courseName = courseName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.professor = professor;
    }

    // Getters
    public String getCourseName() { return courseName; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getRoom() { return room; }
    public String getProfessor() { return professor; }
}