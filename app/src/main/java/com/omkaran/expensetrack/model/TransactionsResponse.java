package com.omkaran.expensetrack.model;

import java.util.List;

public class TransactionsResponse {

    private boolean success;
    private List<Transaction> data;

    public boolean isSuccess() {
        return success;
    }

    public List<Transaction> getData() {
        return data;
    }
}