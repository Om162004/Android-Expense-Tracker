package com.omkaran.expensetrack;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;
import java.util.List;

@Dao
public interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TransactionEntity transaction);
    @Query("SELECT * FROM transactions ORDER BY transactionDate DESC")
    LiveData<List<TransactionEntity>> getAllTransactions();

    @Query("SELECT * FROM transactions ORDER BY transactionDate DESC")
    List<TransactionEntity> getAllTransactionsDirect();

    @Query("DELETE FROM transactions")
    void deleteAll();

}