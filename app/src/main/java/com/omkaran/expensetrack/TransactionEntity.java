package com.omkaran.expensetrack;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class TransactionEntity {

    @PrimaryKey
    private int transactionId;

    private double amount;
    private String type;
    private String category;
    private String description;
    private String transactionDate;

    public TransactionEntity(
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
}