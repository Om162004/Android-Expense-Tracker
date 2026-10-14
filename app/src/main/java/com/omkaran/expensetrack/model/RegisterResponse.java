package com.omkaran.expensetrack.model;

public class RegisterResponse {

    private boolean success;
    private String message;
    private int userId;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }
}