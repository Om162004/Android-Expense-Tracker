package com.omkaran.expensetrack;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.omkaran.expensetrack.api.RetrofitClient;
import com.omkaran.expensetrack.model.ForgotPasswordRequest;
import com.omkaran.expensetrack.model.ForgotPasswordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etForgotEmail;
    Button btnSendOtp;
    TextView tvBackToLogin;
    TextView tvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);

        etForgotEmail = findViewById(R.id.etForgotEmail);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        tvBack = findViewById(R.id.tvBack);

        btnSendOtp.setOnClickListener(v -> {

            String email = etForgotEmail
                    .getText()
                    .toString()
                    .trim();

            if (email.isEmpty()) {
                etForgotEmail.setError("Enter your email");
                return;
            }

            ForgotPasswordRequest request =
                    new ForgotPasswordRequest(email);

            btnSendOtp.setEnabled(false);

            RetrofitClient
                    .getApiService(ForgotPasswordActivity.this)
                    .forgotPassword(request)
                    .enqueue(new Callback<ForgotPasswordResponse>() {

                        @Override
                        public void onResponse(
                                Call<ForgotPasswordResponse> call,
                                Response<ForgotPasswordResponse> response) {

                            btnSendOtp.setEnabled(true);

                            if (response.isSuccessful()
                                    && response.body() != null) {

                                ForgotPasswordResponse data =
                                        response.body();

                                if (data.isSuccess()) {

                                    Toast.makeText(
                                            ForgotPasswordActivity.this,
                                            "OTP sent successfully",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    Intent intent = new Intent(
                                            ForgotPasswordActivity.this,
                                            VerifyOtpActivity.class
                                    );

                                    intent.putExtra("email", email);
                                    //intent.putExtra("otp", data.getOtp());

                                    startActivity(intent);

                                } else {

                                    Toast.makeText(
                                            ForgotPasswordActivity.this,
                                            data.getMessage(),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }

                            } else {

                                Toast.makeText(
                                        ForgotPasswordActivity.this,
                                        "Email not registered",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }

                        @Override
                        public void onFailure(
                                Call<ForgotPasswordResponse> call,
                                Throwable t) {

                            btnSendOtp.setEnabled(true);

                            Toast.makeText(
                                    ForgotPasswordActivity.this,
                                    "Network Error: " + t.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });

        tvBackToLogin.setOnClickListener(v -> finish());

        tvBack.setOnClickListener(v -> finish());
    }
}