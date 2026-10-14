package com.omkaran.expensetrack;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.omkaran.expensetrack.api.ApiService;
import com.omkaran.expensetrack.api.RetrofitClient;
import com.omkaran.expensetrack.model.RegisterRequest;
import com.omkaran.expensetrack.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName;
    private EditText etEmail;
    private EditText etPassword;

    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {

            Toast.makeText(
                    RegisterActivity.this,
                    "Register button clicked",
                    Toast.LENGTH_SHORT
            ).show();

            registerUser();
        });
    }
    private void registerUser() {

        String fullName =
                etFullName.getText()
                        .toString()
                        .trim();

        String email =
                etEmail.getText()
                        .toString()
                        .trim();

        String password =
                etPassword.getText()
                        .toString()
                        .trim();

        // Validation
        if (TextUtils.isEmpty(fullName)) {

            etFullName.setError("Enter your name");
            etFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {

            etEmail.setError("Enter your email");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {

            etPassword.setError("Enter your password");
            etPassword.requestFocus();
            return;
        }

        RegisterRequest request =
                new RegisterRequest(
                        fullName,
                        email,
                        password
                );

        ApiService apiService =
                RetrofitClient.getApiService(
                        RegisterActivity.this
                );

        btnRegister.setEnabled(false);

        apiService.register(request)
                .enqueue(
                        new Callback<RegisterResponse>() {

                            @Override
                            public void onResponse(
                                    Call<RegisterResponse> call,
                                    Response<RegisterResponse> response) {

                                btnRegister.setEnabled(true);

                                if (response.isSuccessful()
                                        && response.body() != null) {

                                    RegisterResponse result =
                                            response.body();

                                    if (result.isSuccess()) {

                                        Toast.makeText(
                                                RegisterActivity.this,
                                                "Registration successful ✅",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        finish();

                                    } else {

                                        Toast.makeText(
                                                RegisterActivity.this,
                                                result.getMessage(),
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }

                                } else {

                                    Toast.makeText(
                                            RegisterActivity.this,
                                            "Registration failed: HTTP "
                                                    + response.code(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            }

                            @Override
                            public void onFailure(
                                    Call<RegisterResponse> call,
                                    Throwable t) {

                                btnRegister.setEnabled(true);

                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Network Error: "
                                                + t.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
    }
}