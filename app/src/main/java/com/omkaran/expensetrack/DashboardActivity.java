package com.omkaran.expensetrack;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.omkaran.expensetrack.api.ApiService;
import com.omkaran.expensetrack.api.RetrofitClient;
import com.omkaran.expensetrack.model.SummaryResponse;
import com.omkaran.expensetrack.model.Transaction;
import com.omkaran.expensetrack.model.TransactionsResponse;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvBalance;
    private TextView tvIncome;
    private TextView tvExpense;

    private LinearLayout recentTransactionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_dashboard);
        setContentView(R.layout.activity_dashboard);

        TextView ivProfile = findViewById(R.id.ivProfile);

        ivProfile.setOnClickListener(v -> {

            Intent intent = new Intent(
                    DashboardActivity.this,
                    ProfileActivity.class
            );

            startActivity(intent);
        });
//        ImageView ivProfile = findViewById(R.id.ivProfile);
//
//        ivProfile.setOnClickListener(v -> {
//            Intent intent = new Intent(
//                    DashboardActivity.this,
//                    ProfileActivity.class
//            );
//
//            startActivity(intent);
//        });
        // =========================
        // FIND VIEWS
        // =========================

        tvName = findViewById(R.id.tvName);
        tvBalance = findViewById(R.id.tvBalance);
        tvIncome = findViewById(R.id.tvIncome);
        tvExpense = findViewById(R.id.tvExpense);

        recentTransactionsContainer =
                findViewById(R.id.recentTransactionsContainer);

        // =========================
        // SAVED USER NAME
        // =========================

        String fullName =
                getSharedPreferences(
                        "ExpenseTrackPrefs",
                        MODE_PRIVATE
                ).getString(
                        "fullName",
                        "User"
                );

        tvName.setText(fullName);

        // =========================
        // LOAD DASHBOARD DATA
        // =========================

        loadSummary();
        loadRecentTransactions();

        // =========================
        // ADD TRANSACTION
        // =========================

        Button btnAddTransaction =
                findViewById(R.id.btnAddTransaction);

        btnAddTransaction.setOnClickListener(v -> {

            Intent intent = new Intent(
                    DashboardActivity.this,
                    AddTransactionActivity.class
            );

            startActivity(intent);
        });


        // =========================
        // VIEW ALL
        // =========================

        TextView tvViewAll =
                findViewById(R.id.tvViewAll);

        tvViewAll.setOnClickListener(v -> {

            Intent intent = new Intent(
                    DashboardActivity.this,
                    TransactionsActivity.class
            );

            startActivity(intent);
        });
    }

    // =====================================================
    // ON RESUME
    // =====================================================

    @Override
    protected void onResume() {
        super.onResume();

        loadSummary();
        loadRecentTransactions();
    }

    // =====================================================
    // LOAD SUMMARY
    // =====================================================

    private void loadSummary() {

        ApiService apiService =
                RetrofitClient.getApiService(
                        DashboardActivity.this
                );

        apiService.getSummary().enqueue(
                new Callback<SummaryResponse>() {

                    @Override
                    public void onResponse(
                            Call<SummaryResponse> call,
                            Response<SummaryResponse> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            SummaryResponse summary =
                                    response.body();

                            tvIncome.setText(
                                    "₹ " +
                                            String.format(
                                                    Locale.getDefault(),
                                                    "%.2f",
                                                    summary.getTotalIncome()
                                            )
                            );

                            tvExpense.setText(
                                    "₹ " +
                                            String.format(
                                                    Locale.getDefault(),
                                                    "%.2f",
                                                    summary.getTotalExpense()
                                            )
                            );

                            tvBalance.setText(
                                    "₹ " +
                                            String.format(
                                                    Locale.getDefault(),
                                                    "%.2f",
                                                    summary.getBalance()
                                            )
                            );

                        } else {

                            Toast.makeText(
                                    DashboardActivity.this,
                                    "Summary API Error: HTTP "
                                            + response.code(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<SummaryResponse> call,
                            Throwable t) {

                        Toast.makeText(
                                DashboardActivity.this,
                                "API Error: "
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    // =====================================================
    // LOAD RECENT TRANSACTIONS
    // =====================================================

    private void loadRecentTransactions() {

        ApiService apiService =
                RetrofitClient.getApiService(
                        DashboardActivity.this
                );

        apiService.getTransactions().enqueue(
                new Callback<TransactionsResponse>() {

                    @Override
                    public void onResponse(
                            Call<TransactionsResponse> call,
                            Response<TransactionsResponse> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().isSuccess()
                                && response.body().getData() != null) {

                            List<Transaction> transactions =
                                    response.body().getData();

                            showRecentTransactions(
                                    transactions
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<TransactionsResponse> call,
                            Throwable t) {

                        // Recent transaction fail hone par
                        // dashboard par Toast nahi dikhayenge.
                    }
                }
        );
    }

    // =====================================================
    // SHOW RECENT TRANSACTIONS
    // =====================================================

    private void showRecentTransactions(
            List<Transaction> transactions) {

        recentTransactionsContainer.removeAllViews();

        // Sirf latest 3 transactions
        int count =
                Math.min(
                        transactions.size(),
                        3
                );

        for (int i = 0; i < count; i++) {

            Transaction transaction =
                    transactions.get(i);

            // =========================
            // MAIN ROW
            // =========================

            LinearLayout row =
                    new LinearLayout(this);

            row.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            row.setGravity(
                    Gravity.CENTER_VERTICAL
            );

            row.setPadding(
                    16,
                    14,
                    16,
                    14
            );

            row.setBackgroundResource(
                    R.drawable.dashboard_card
            );

            LinearLayout.LayoutParams rowParams =
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

            rowParams.setMargins(
                    0,
                    0,
                    0,
                    10
            );

            row.setLayoutParams(rowParams);

            // =========================
            // ICON
            // =========================

            TextView tvIcon =
                    new TextView(this);

            tvIcon.setText(
                    getCategoryIcon(
                            transaction.getCategory()
                    )
            );

            tvIcon.setTextSize(22);

            tvIcon.setGravity(
                    Gravity.CENTER
            );

            LinearLayout.LayoutParams iconParams =
                    new LinearLayout.LayoutParams(
                            45,
                            45
                    );

            tvIcon.setLayoutParams(
                    iconParams
            );

            // =========================
            // CATEGORY + DESCRIPTION
            // =========================

            LinearLayout middle =
                    new LinearLayout(this);

            middle.setOrientation(
                    LinearLayout.VERTICAL
            );

            LinearLayout.LayoutParams middleParams =
                    new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            1
                    );

            middleParams.setMargins(
                    12,
                    0,
                    8,
                    0
            );

            middle.setLayoutParams(
                    middleParams
            );

            TextView tvCategory =
                    new TextView(this);

            String category =
                    transaction.getCategory();

            if (category == null ||
                    category.trim().isEmpty()) {

                category = "Other";
            }

            tvCategory.setText(
                    category
            );

            tvCategory.setTextSize(15);

            tvCategory.setTextColor(
                    Color.parseColor("#222222")
            );

            tvCategory.setTypeface(
                    null,
                    android.graphics.Typeface.BOLD
            );

            TextView tvDescription =
                    new TextView(this);

            String description =
                    transaction.getDescription();

            if (description == null ||
                    description.trim().isEmpty()) {

                description = "No description";
            }

            tvDescription.setText(
                    description
            );

            tvDescription.setTextSize(12);

            tvDescription.setTextColor(
                    Color.parseColor("#777777")
            );

            middle.addView(
                    tvCategory
            );

            middle.addView(
                    tvDescription
            );

            // =========================
            // AMOUNT
            // =========================

            TextView tvAmount =
                    new TextView(this);

            String type =
                    transaction.getType();

            if (type == null) {
                type = "Expense";
            }

            String amount =
                    String.format(
                            Locale.getDefault(),
                            "%.2f",
                            transaction.getAmount()
                    );

            if ("Income".equalsIgnoreCase(type)) {

                tvAmount.setText(
                        "+ ₹ " + amount
                );

                tvAmount.setTextColor(
                        Color.parseColor("#16A34A")
                );

            } else {

                tvAmount.setText(
                        "- ₹ " + amount
                );

                tvAmount.setTextColor(
                        Color.parseColor("#DC2626")
                );
            }

            tvAmount.setTextSize(14);

            tvAmount.setTypeface(
                    null,
                    android.graphics.Typeface.BOLD
            );

            // =========================
            // ADD TO ROW
            // =========================

            row.addView(tvIcon);

            row.addView(middle);

            row.addView(tvAmount);

            recentTransactionsContainer.addView(
                    row
            );
        }
    }

    // =====================================================
    // CATEGORY ICON
    // =====================================================

    private String getCategoryIcon(
            String category) {

        if (category == null) {
            return "💳";
        }

        switch (category.toLowerCase()) {

            case "food":
                return "🍴";

            case "travel":
                return "✈️";

            case "shopping":
                return "🛒";

            case "business":
                return "💼";

            case "salary":
                return "💰";

            case "bills":
                return "🧾";

            case "entertainment":
                return "🎬";

            case "recharges":
                return "📱";

            default:
                return "💳";
        }
    }
}