package com.omkaran.expensetrack;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.omkaran.expensetrack.api.ApiService;
import com.omkaran.expensetrack.api.RetrofitClient;
import com.omkaran.expensetrack.model.Transaction;
import com.omkaran.expensetrack.model.TransactionsResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionsActivity extends AppCompatActivity {

    private RecyclerView recyclerTransactions;
    private EditText etSearch;
    private TextView tvEmptyState;
    private TransactionAdapter adapter;

    private TransactionViewModel viewModel;

    // Room se aane wale transactions
    private List<Transaction> allTransactions;

    // RecyclerView ke liye Header + Transaction
    private List<Object> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transactions);

        tvEmptyState =
                findViewById(R.id.tvEmptyState);

        recyclerTransactions =
                findViewById(R.id.recyclerTransactions);

        etSearch =
                findViewById(R.id.etSearch);

        allTransactions =
                new ArrayList<>();

        transactionList =
                new ArrayList<>();

        recyclerTransactions.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter =
                new TransactionAdapter(
                        transactionList
                );

        recyclerTransactions.setAdapter(adapter);

        // =========================
        // VIEWMODEL
        // =========================

        viewModel =
                new ViewModelProvider(this)
                        .get(TransactionViewModel.class);

        // =========================
        // LIVE DATA
        // =========================

        viewModel.getAllTransactions().observe(
                this,
                entities -> {

                    allTransactions.clear();

                    if (entities != null) {

                        for (TransactionEntity entity :
                                entities) {

                            Transaction transaction =
                                    new Transaction(
                                            entity.getTransactionId(),
                                            entity.getAmount(),
                                            entity.getType(),
                                            entity.getCategory(),
                                            entity.getDescription(),
                                            entity.getTransactionDate()
                                    );

                            allTransactions.add(
                                    transaction
                            );
                        }
                    }

                    filterTransactions(
                            etSearch.getText()
                                    .toString()
                    );
                }
        );

        // API se latest data fetch
        loadTransactions();

        // =========================
        // SEARCH
        // =========================

        etSearch.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        filterTransactions(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                }
        );
    }

    // =====================================================
    // LOAD TRANSACTIONS FROM API
    // =====================================================

    private void loadTransactions() {

        ApiService apiService =
                RetrofitClient.getApiService(
                        TransactionsActivity.this
                );

        apiService.getTransactions()
                .enqueue(
                        new Callback<TransactionsResponse>() {

                            @Override
                            public void onResponse(
                                    Call<TransactionsResponse> call,
                                    Response<TransactionsResponse> response) {

                                if (response.isSuccessful()
                                        && response.body() != null) {

                                    TransactionsResponse result =
                                            response.body();

                                    if (result.isSuccess()
                                            && result.getData() != null) {

                                        // =========================
                                        // API → ROOM
                                        // =========================

                                        viewModel.saveApiTransactions(
                                                result.getData()
                                        );

                                    } else {

                                        Toast.makeText(
                                                TransactionsActivity.this,
                                                "No transactions found",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }

                                } else {

                                    Toast.makeText(
                                            TransactionsActivity.this,
                                            "API Error: HTTP "
                                                    + response.code(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            }

                            @Override
                            public void onFailure(
                                    Call<TransactionsResponse> call,
                                    Throwable t) {

                                /*
                                 * API fail hone par Room ka
                                 * existing data LiveData ke
                                 * through automatically dikhega.
                                 */
                            }
                        }
                );
    }

    // =====================================================
    // SHOW TRANSACTIONS WITH DATE GROUPING
    // =====================================================

    private void showTransactions(
            List<Transaction> transactions) {

        transactionList.clear();

        // =========================
        // NO TRANSACTIONS
        // =========================

        if (transactions == null ||
                transactions.isEmpty()) {

            recyclerTransactions.setVisibility(
                    RecyclerView.GONE
            );

            tvEmptyState.setVisibility(
                    TextView.VISIBLE
            );

            String search =
                    etSearch.getText()
                            .toString()
                            .trim();

            if (!search.isEmpty()) {

                tvEmptyState.setText(
                        "🔍\n\nNo Results Found\n\n" +
                                "Try searching with another keyword."
                );

            } else {

                tvEmptyState.setText(
                        "💸\n\nNo Transactions Yet\n\n" +
                                "Start adding your income and expenses " +
                                "to see them here."
                );
            }

            adapter.notifyDataSetChanged();

            return;
        }

        // =========================
        // TRANSACTIONS AVAILABLE
        // =========================

        recyclerTransactions.setVisibility(
                RecyclerView.VISIBLE
        );

        tvEmptyState.setVisibility(
                TextView.GONE
        );

        String previousDate = "";

        for (Transaction transaction :
                transactions) {

            String currentDate =
                    getDateOnly(
                            transaction.getTransactionDate()
                    );

            if (!currentDate.equals(previousDate)) {

                transactionList.add(
                        getDateHeader(
                                transaction.getTransactionDate()
                        )
                );

                previousDate = currentDate;
            }

            transactionList.add(
                    transaction
            );
        }

        adapter.notifyDataSetChanged();
    }

    // =====================================================
    // DATE HEADER
    // =====================================================

    private String getDateHeader(String dateText) {

        if (dateText == null ||
                dateText.trim().isEmpty()) {

            return "Unknown Date";
        }

        try {

            dateText =
                    dateText.replace("T", " ");

            if (dateText.contains(".")) {

                dateText =
                        dateText.substring(
                                0,
                                dateText.indexOf(".")
                        );
            }

            SimpleDateFormat inputFormat =
                    new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.getDefault()
                    );

            Date date =
                    inputFormat.parse(dateText);

            if (date == null) {
                return "Unknown Date";
            }

            // Today
            SimpleDateFormat todayFormat =
                    new SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                    );

            String today =
                    todayFormat.format(
                            new Date()
                    );

            String transactionDate =
                    todayFormat.format(date);

            if (today.equals(transactionDate)) {
                return "Today";
            }

            // Yesterday
            java.util.Calendar calendar =
                    java.util.Calendar.getInstance();

            calendar.add(
                    java.util.Calendar.DAY_OF_YEAR,
                    -1
            );

            String yesterday =
                    todayFormat.format(
                            calendar.getTime()
                    );

            if (yesterday.equals(transactionDate)) {
                return "Yesterday";
            }

            // Other dates
            SimpleDateFormat outputFormat =
                    new SimpleDateFormat(
                            "dd MMM yyyy",
                            Locale.getDefault()
                    );

            return outputFormat.format(date);

        } catch (Exception e) {

            return "Unknown Date";
        }
    }

    // =====================================================
    // DATE ONLY
    // =====================================================

    private String getDateOnly(String dateText) {

        if (dateText == null) {
            return "";
        }

        try {

            dateText =
                    dateText.replace("T", " ");

            if (dateText.contains(".")) {

                dateText =
                        dateText.substring(
                                0,
                                dateText.indexOf(".")
                        );
            }

            SimpleDateFormat inputFormat =
                    new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.getDefault()
                    );

            SimpleDateFormat outputFormat =
                    new SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                    );

            Date date =
                    inputFormat.parse(dateText);

            if (date != null) {

                return outputFormat.format(date);
            }

        } catch (Exception ignored) {
        }

        return dateText;
    }

    // =====================================================
    // SEARCH
    // =====================================================

    private void filterTransactions(
            String query) {

        query =
                query.toLowerCase().trim();

        List<Transaction> filteredList =
                new ArrayList<>();

        // Search empty → all
        if (query.isEmpty()) {

            filteredList.addAll(
                    allTransactions
            );

        } else {

            for (Transaction transaction :
                    allTransactions) {

                String category =
                        transaction.getCategory();

                String description =
                        transaction.getDescription();

                String type =
                        transaction.getType();

                String amount =
                        String.valueOf(
                                transaction.getAmount()
                        );

                String date =
                        transaction.getTransactionDate();

                if (category == null) {
                    category = "";
                }

                if (description == null) {
                    description = "";
                }

                if (type == null) {
                    type = "";
                }

                if (date == null) {
                    date = "";
                }

                if (category.toLowerCase()
                        .contains(query)

                        || description.toLowerCase()
                        .contains(query)

                        || type.toLowerCase()
                        .contains(query)

                        || amount.contains(query)

                        || date.toLowerCase()
                        .contains(query)) {

                    filteredList.add(
                            transaction
                    );
                }
            }
        }

        // Grouping ke saath display
        showTransactions(
                filteredList
        );
    }
}