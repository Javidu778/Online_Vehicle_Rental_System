package com.rental.model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String email;
    private String fullName;
    private String phone;
    private Timestamp createdAt;

    // Constructors
    public User() {}

    public User(int userId, String username, String password, String role, String email, String fullName, String phone) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
} 