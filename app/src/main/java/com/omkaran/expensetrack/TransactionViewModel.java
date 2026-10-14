package com.omkaran.expensetrack;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.omkaran.expensetrack.model.Transaction;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private final TransactionRepository repository;

    private final LiveData<List<TransactionEntity>> allTransactions;

    public TransactionViewModel(@NonNull Application application) {

        super(application);

        repository =
                new TransactionRepository(application);

        allTransactions =
                repository.getAllTransactions();
    }

    public LiveData<List<TransactionEntity>> getAllTransactions() {
        return allTransactions;
    }

    public void insert(TransactionEntity transaction) {
        repository.insert(transaction);
    }

    public void saveApiTransactions(
            List<Transaction> transactions) {

        repository.saveApiTransactions(transactions);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}