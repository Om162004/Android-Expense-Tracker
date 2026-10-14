package com.omkaran.expensetrack.model;

public class TransactionRequest {

    private double amount;
    private String type;
    private String category;
    private String description;
    private String transactionDate;

    public TransactionRequest(
            double amount,
            String type,
            String category,
            String description,
            String transactionDate) {

        this.amount = amount;
        this.type = type;
        this.category = category;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getTransactionDate() {
        return transactionDate;
    }
}