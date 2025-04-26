package com.example.labb1;

public class AnnouncementItem {
    private String title;
    private String content;
    private String date;
    private String sender;

    public AnnouncementItem(String title, String content, String date, String sender) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.sender = sender;
    }

    // Getters
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getDate() { return date; }
    public String getSender() { return sender; }
}
