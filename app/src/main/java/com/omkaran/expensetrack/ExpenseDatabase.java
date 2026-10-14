package com.omkaran.expensetrack;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {TransactionEntity.class},
        version = 2,
        exportSchema = false
)
public abstract class ExpenseDatabase extends RoomDatabase {

    public abstract TransactionDao transactionDao();

    private static ExpenseDatabase INSTANCE;

    public static synchronized ExpenseDatabase getInstance(Context context) {

        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    ExpenseDatabase.class,
                    "expense_database"
            ).build();
        }

        return INSTANCE;
    }
}