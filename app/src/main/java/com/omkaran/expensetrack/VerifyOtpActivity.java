package com.omkaran.expensetrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.omkaran.expensetrack.api.RetrofitClient;
import com.omkaran.expensetrack.model.VerifyOtpRequest;
import com.omkaran.expensetrack.model.VerifyOtpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpActivity extends AppCompatActivity {

    EditText etOtp;
    Button btnVerifyOtp;
    TextView tvBack;
    TextView tvResendOtp;

    String email;
    String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_verify_otp);

        etOtp = findViewById(R.id.etOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        tvBack = findViewById(R.id.tvBack);
        tvResendOtp = findViewById(R.id.tvResendOtp);

        // Email received from Forgot Password screen
        email = getIntent().getStringExtra("email");

        btnVerifyOtp.setOnClickListener(v -> {

            String enteredOtp =
                    etOtp.getText().toString().trim();

            if (enteredOtp.isEmpty()) {
                etOtp.setError("Enter OTP");
                return;
            }

            if (enteredOtp.length() != 6) {
                etOtp.setError("Enter 6 digit OTP");
                return;
            }

            VerifyOtpRequest request =
                    new VerifyOtpRequest(email, enteredOtp);

            btnVerifyOtp.setEnabled(false);

            RetrofitClient
                    .getApiService(VerifyOtpActivity.this)
                    .verifyOtp(request)
                    .enqueue(new Callback<VerifyOtpResponse>() {

                        @Override
                        public void onResponse(
                                Call<VerifyOtpResponse> call,
                                Response<VerifyOtpResponse> response) {

                            btnVerifyOtp.setEnabled(true);

                            if (response.isSuccessful()
                                    && response.body() != null) {

                                VerifyOtpResponse data =
                                        response.body();

                                if (data.isSuccess()) {

                                    Toast.makeText(
                                            VerifyOtpActivity.this,
                                            "OTP Verified",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    Intent intent =
                                            new Intent(
                                                    VerifyOtpActivity.this,
                                                    NewPasswordActivity.class
                                            );

                                    intent.putExtra(
                                            "email",
                                            email
                                    );

                                    intent.putExtra(
                                            "otp",
                                            enteredOtp
                                    );

                                    startActivity(intent);

                                    finish();

                                } else {

                                    Toast.makeText(
                                            VerifyOtpActivity.this,
                                            data.getMessage(),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }

                            } else {

                                Toast.makeText(
                                        VerifyOtpActivity.this,
                                        "Invalid or expired OTP",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }

                        @Override
                        public void onFailure(
                                Call<VerifyOtpResponse> call,
                                Throwable t) {

                            btnVerifyOtp.setEnabled(true);

                            Toast.makeText(
                                    VerifyOtpActivity.this,
                                    "Network Error: "
                                            + t.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });

        tvBack.setOnClickListener(v -> {
            finish();
        });
    }
}