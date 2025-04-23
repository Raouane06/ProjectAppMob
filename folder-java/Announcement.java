package com.example.labb1;

public class Announcement {
    private String title;
    private String content;
    private String date;
    private String author;

    // Constructor, getters, and setters
    public Announcement(String title, String content, String date, String author) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.author = author;
    }

    // Add getters here
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getDate() { return date; }
    public String getAuthor() { return author; }
}