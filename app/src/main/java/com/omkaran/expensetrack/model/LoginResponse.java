package com.omkaran.expensetrack.model;
public class LoginResponse {

    private boolean success;
    private String message;
    private int userId;
    private String fullName;
    private String email;
    private String token;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}