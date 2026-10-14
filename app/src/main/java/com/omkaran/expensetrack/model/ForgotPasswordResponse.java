package com.omkaran.expensetrack.model;

public class ForgotPasswordResponse {

    private boolean success;
    private String message;
    private String otp;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getOtp() {
        return otp;
    }
}