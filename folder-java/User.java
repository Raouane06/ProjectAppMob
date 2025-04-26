package com.example.labb1;

public class User {
    private String id;
    private String username;
    private String email;

    // Constructor, getters
    public User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}