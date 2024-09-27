package com.hrelix.app.models;

public class AuthRequest {
    private String email;
    private String password;

    // Getters and Setters

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
