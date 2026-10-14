package com.omkaran.expensetrack.model;

public class Transaction {

    private int transactionId;
    private double amount;
    private String type;
    private String category;
    private String description;
    private String transactionDate;

    public int getTransactionId() {
        return transactionId;
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

    // Room Entity → Transaction
    public Transaction(
            int transactionId,
            double amount,
            String type,
            String category,
            String description,
            String transactionDate) {

        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    // Retrofit ke liye empty constructor
    public Transaction() {
    }
}