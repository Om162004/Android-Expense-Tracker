package com.omkaran.expensetrack.model;

public class SummaryResponse {

    private boolean success;
    private double totalIncome;
    private double totalExpense;
    private double balance;

    public boolean isSuccess() {
        return success;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public double getBalance() {
        return balance;
    }
}