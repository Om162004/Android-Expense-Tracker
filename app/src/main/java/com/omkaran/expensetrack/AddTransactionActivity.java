package com.omkaran.expensetrack;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.omkaran.expensetrack.api.ApiService;
import com.omkaran.expensetrack.api.RetrofitClient;
import com.omkaran.expensetrack.model.TransactionRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTransactionActivity extends AppCompatActivity {

    private RadioButton rbIncome;
    private RadioButton rbExpense;

    private EditText etAmount;
    private Spinner spCategory;
    private EditText etDescription;
    private EditText etDate;

    private Button btnSaveTransaction;

    // Selected date + current time
    private Calendar selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_transaction);

        // Find Views
        rbIncome = findViewById(R.id.rbIncome);
        rbExpense = findViewById(R.id.rbExpense);

        etAmount = findViewById(R.id.etAmount);
        spCategory = findViewById(R.id.spCategory);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);

        btnSaveTransaction =
                findViewById(R.id.btnSaveTransaction);

        // Default Expense
        rbExpense.setChecked(true);

        // Categories
        String[] categories = {
                "Food",
                "Travel",
                "Shopping",
                "Recharges",
                "Bills",
                "Salary",
                "Business",
                "Entertainment",
                "Other"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categories
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spCategory.setAdapter(adapter);

        // Current date + current time
        selectedDateTime = Calendar.getInstance();

        showDateOnEditText();

        // Date picker
        etDate.setOnClickListener(v -> showDatePicker());

        // Save
        btnSaveTransaction.setOnClickListener(
                v -> saveTransaction()
        );
    }

    // Show selected date on screen
    private void showDateOnEditText() {

        SimpleDateFormat displayFormat =
                new SimpleDateFormat(
                        "dd MMM yyyy",
                        Locale.getDefault()
                );

        etDate.setText(
                displayFormat.format(
                        selectedDateTime.getTime()
                )
        );
    }

    // Date Picker
    private void showDatePicker() {

        DatePickerDialog dialog =
                new DatePickerDialog(
                        this,

                        (view, year, month, dayOfMonth) -> {

                            /*
                             * DatePicker se sirf date change hogi.
                             * Current time same rahega.
                             */

                            selectedDateTime.set(
                                    Calendar.YEAR,
                                    year
                            );

                            selectedDateTime.set(
                                    Calendar.MONTH,
                                    month
                            );

                            selectedDateTime.set(
                                    Calendar.DAY_OF_MONTH,
                                    dayOfMonth
                            );

                            showDateOnEditText();
                        },

                        selectedDateTime.get(
                                Calendar.YEAR
                        ),

                        selectedDateTime.get(
                                Calendar.MONTH
                        ),

                        selectedDateTime.get(
                                Calendar.DAY_OF_MONTH
                        )
                );

        dialog.show();
    }

    // Save Transaction
    private void saveTransaction() {

        String amountText =
                etAmount.getText().toString().trim();

        String description =
                etDescription.getText().toString().trim();

        // Amount validation
        if (amountText.isEmpty()) {

            etAmount.setError("Enter amount");
            etAmount.requestFocus();

            return;
        }

        double amount;

        try {

            amount =
                    Double.parseDouble(amountText);

        } catch (NumberFormatException e) {

            etAmount.setError("Invalid amount");
            etAmount.requestFocus();

            return;
        }

        // Transaction type
        String type;

        if (rbIncome.isChecked()) {

            type = "Income";

        } else {

            type = "Expense";
        }

        // Category
        String category =
                spCategory.getSelectedItem().toString();

        /*
         * Backend ke liye:
         *
         * yyyy-MM-dd'T'HH:mm:ss
         *
         * Example:
         * 2026-09-04T22:40:15
         */

        SimpleDateFormat apiFormat =
                new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss",
                        Locale.getDefault()
                );

        String transactionDate =
                apiFormat.format(
                        selectedDateTime.getTime()
                );

        // Request
        TransactionRequest request =
                new TransactionRequest(
                        amount,
                        type,
                        category,
                        description,
                        transactionDate
                );

        // API
        ApiService apiService =
                RetrofitClient.getApiService(
                        AddTransactionActivity.this
                );

        btnSaveTransaction.setEnabled(false);

        apiService.addTransaction(request)
                .enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(
                            Call<Void> call,
                            Response<Void> response) {

                        btnSaveTransaction.setEnabled(true);

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    AddTransactionActivity.this,
                                    "Transaction Saved ✅",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();

                        } else {

                            Toast.makeText(
                                    AddTransactionActivity.this,
                                    "API Error: HTTP "
                                            + response.code(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Void> call,
                            Throwable t) {

                        btnSaveTransaction.setEnabled(true);

                        Toast.makeText(
                                AddTransactionActivity.this,
                                "API Error: "
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}