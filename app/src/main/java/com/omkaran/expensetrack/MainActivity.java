package com.omkaran.expensetrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;
import com.omkaran.expensetrack.model.UserResponse;
import com.omkaran.expensetrack.api.ApiService;
import com.omkaran.expensetrack.api.RetrofitClient;
import com.omkaran.expensetrack.model.LoginRequest;
import com.omkaran.expensetrack.model.LoginResponse;

import android.text.method.PasswordTransformationMethod;
import android.text.method.HideReturnsTransformationMethod;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        EditText etPassword = findViewById(R.id.etPassword);
        //forget password
        TextView tvForgotPassword =
                findViewById(R.id.tvForgotPassword);

        tvForgotPassword.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    ForgotPasswordActivity.class
            );

            startActivity(intent);
        });
        // Password Eye Button
        etPassword.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_UP) {

                if (event.getX() >=
                        etPassword.getWidth()
                                - etPassword.getCompoundDrawables()[2].getBounds().width()
                                - etPassword.getPaddingEnd()) {

                    if (etPassword.getTransformationMethod()
                            instanceof PasswordTransformationMethod) {

                        // Show password
                        etPassword.setTransformationMethod(
                                HideReturnsTransformationMethod.getInstance()
                        );

                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.ic_visibility_off,
                                0
                        );

                    } else {

                        // Hide password
                        etPassword.setTransformationMethod(
                                PasswordTransformationMethod.getInstance()
                        );

                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.ic_visibility_off,
                                0
                        );
                    }

                    etPassword.setSelection(
                            etPassword.getText().length()
                    );

                    return true;
                }
            }

            return false;
        });

        Button btnLogin = findViewById(R.id.btnLogin);
        EditText etEmail = findViewById(R.id.etEmail);
        TextView tvRegister = findViewById(R.id.tvRegister);

        tvRegister.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Email validation
            if (email.isEmpty()) {
                etEmail.setError("Enter email");
                etEmail.requestFocus();
                return;
            }

            // Password validation
            if (password.isEmpty()) {
                etPassword.setError("Enter password");
                etPassword.requestFocus();
                return;
            }

            // Login Request
            LoginRequest request = new LoginRequest(
                    email,
                    password
            );

            ApiService apiService =
                    RetrofitClient.getApiService(MainActivity.this);

            apiService.login(request).enqueue(
                    new Callback<LoginResponse>() {

                        @Override
                        public void onResponse(
                                Call<LoginResponse> call,
                                Response<LoginResponse> response) {

                            if (response.isSuccessful()
                                    && response.body() != null) {

                                LoginResponse loginResponse =
                                        response.body();

                                if (loginResponse.isSuccess()) {

                                    // =========================
                                    // SAVE LOGIN INFORMATION
                                    // =========================

                                    String token =
                                            loginResponse.getToken();

                                    int userId =
                                            loginResponse.getUserId();

                                    String fullName =
                                            loginResponse.getFullName();

                                    String userEmail =
                                            loginResponse.getEmail();

                                    getSharedPreferences(
                                            "ExpenseTrackPrefs",
                                            MODE_PRIVATE
                                    )
                                            .edit()
                                            .putString("token", token)
                                            .putInt("userId", userId)
                                            .putString("fullName", fullName)
                                            .putString("email", userEmail)
                                            .apply();

                                    // Login successful
                                    Intent intent = new Intent(
                                            MainActivity.this,
                                            DashboardActivity.class
                                    );

                                    startActivity(intent);

                                    finish();
                                    ApiService apiService =
                                            RetrofitClient.getApiService(MainActivity.this);

                                    apiService.getCurrentUser().enqueue(new Callback<UserResponse>() {

                                        @Override
                                        public void onResponse(
                                                Call<UserResponse> call,
                                                Response<UserResponse> response) {

                                            if (response.isSuccessful() && response.body() != null) {

                                                UserResponse user = response.body();

                                                Toast.makeText(
                                                        MainActivity.this,
                                                        "User: " + user.getFullName()
                                                                + "\nEmail: " + user.getEmail(),
                                                        Toast.LENGTH_LONG
                                                ).show();

                                            } else {

                                                Toast.makeText(
                                                        MainActivity.this,
                                                        "ME API Error: HTTP " + response.code(),
                                                        Toast.LENGTH_LONG
                                                ).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(
                                                Call<UserResponse> call,
                                                Throwable t) {

                                            Toast.makeText(
                                                    MainActivity.this,
                                                    "ME API Error: " + t.getMessage(),
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }
                                    });

                                    // Temporary check
                                    String savedToken =
                                            getSharedPreferences(
                                                    "ExpenseTrackPrefs",
                                                    MODE_PRIVATE
                                            )
                                                    .getString(
                                                            "token",
                                                            null
                                                    );

                                    Toast.makeText(
                                            MainActivity.this,
                                            "Token Saved: "
                                                    + (savedToken != null),
                                            Toast.LENGTH_LONG
                                    ).show();

                                } else {

                                    Toast.makeText(
                                            MainActivity.this,
                                            loginResponse.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }

                            } else {

                                try {

                                    String errorBody =
                                            response.errorBody() != null
                                                    ? response.errorBody().string()
                                                    : "No error body";

                                    Toast.makeText(
                                            MainActivity.this,
                                            "HTTP "
                                                    + response.code()
                                                    + "\n"
                                                    + errorBody,
                                            Toast.LENGTH_LONG
                                    ).show();

                                } catch (Exception e) {

                                    Toast.makeText(
                                            MainActivity.this,
                                            "HTTP "
                                                    + response.code(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(
                                Call<LoginResponse> call,
                                Throwable t) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "API ERROR: "
                                            + t.getClass().getSimpleName()
                                            + "\n"
                                            + t.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );
        });
    }
}