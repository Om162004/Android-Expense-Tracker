package com.omkaran.expensetrack;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.omkaran.expensetrack.model.Transaction;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionRepository {

    private final TransactionDao transactionDao;
    private final LiveData<List<TransactionEntity>> allTransactions;
    private final ExecutorService executorService;

    public TransactionRepository(Application application) {

        ExpenseDatabase database =
                ExpenseDatabase.getInstance(application);

        transactionDao = database.transactionDao();

        allTransactions =
                transactionDao.getAllTransactions();

        executorService =
                Executors.newSingleThreadExecutor();
    }

    public LiveData<List<TransactionEntity>> getAllTransactions() {
        return allTransactions;
    }

    public void insert(TransactionEntity transaction) {

        executorService.execute(() ->
                transactionDao.insert(transaction)
        );
    }

    // API Transaction → Room Entity
    public void saveApiTransactions(
            List<Transaction> transactions) {

        executorService.execute(() -> {

            for (Transaction transaction : transactions) {

                TransactionEntity entity =
                        new TransactionEntity(
                                transaction.getTransactionId(),
                                transaction.getAmount(),
                                transaction.getType(),
                                transaction.getCategory(),
                                transaction.getDescription(),
                                transaction.getTransactionDate()
                        );

                transactionDao.insert(entity);
            }
        });
    }

    public void deleteAll() {

        executorService.execute(() ->
                transactionDao.deleteAll()
        );
    }
}