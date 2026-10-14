package com.omkaran.expensetrack.model;

public class UserResponse {

    private boolean success;
    private String userId;
    private String fullName;
    private String email;

    public boolean isSuccess() {
        return success;
    }

    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
}