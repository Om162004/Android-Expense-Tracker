package com.omkaran.expensetrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.omkaran.expensetrack.api.RetrofitClient;
import com.omkaran.expensetrack.model.ResetPasswordRequest;
import com.omkaran.expensetrack.model.ResetPasswordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPasswordActivity extends AppCompatActivity {

    EditText etNewPassword;
    EditText etConfirmPassword;
    Button btnResetPassword;
    TextView tvBack;

    String email;
    String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_password);

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvBack = findViewById(R.id.tvBack);

        email = getIntent().getStringExtra("email");
        otp = getIntent().getStringExtra("otp");

        btnResetPassword.setOnClickListener(v -> {

            String newPassword =
                    etNewPassword.getText().toString().trim();

            String confirmPassword =
                    etConfirmPassword.getText().toString().trim();

            if (newPassword.isEmpty()) {
                etNewPassword.setError("Enter new password");
                return;
            }

            if (newPassword.length() < 6) {
                etNewPassword.setError(
                        "Password must be at least 6 characters"
                );
                return;
            }

            if (confirmPassword.isEmpty()) {
                etConfirmPassword.setError(
                        "Confirm your password"
                );
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                etConfirmPassword.setError(
                        "Passwords do not match"
                );
                return;
            }

            ResetPasswordRequest request =
                    new ResetPasswordRequest(
                            email,
                            otp,
                            newPassword
                    );

            btnResetPassword.setEnabled(false);

            RetrofitClient
                    .getApiService(NewPasswordActivity.this)
                    .resetPassword(request)
                    .enqueue(new Callback<ResetPasswordResponse>() {

                        @Override
                        public void onResponse(
                                Call<ResetPasswordResponse> call,
                                Response<ResetPasswordResponse> response) {

                            btnResetPassword.setEnabled(true);

                            if (response.isSuccessful()
                                    && response.body() != null) {

                                ResetPasswordResponse data =
                                        response.body();

                                if (data.isSuccess()) {

                                    Toast.makeText(
                                            NewPasswordActivity.this,
                                            "Password reset successfully",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    Intent intent =
                                            new Intent(
                                                    NewPasswordActivity.this,
                                                    MainActivity.class
                                            );

                                    intent.addFlags(
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    );

                                    startActivity(intent);

                                    finish();

                                } else {

                                    Toast.makeText(
                                            NewPasswordActivity.this,
                                            data.getMessage(),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }

                            } else {

                                Toast.makeText(
                                        NewPasswordActivity.this,
                                        "OTP invalid or expired",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }

                        @Override
                        public void onFailure(
                                Call<ResetPasswordResponse> call,
                                Throwable t) {

                            btnResetPassword.setEnabled(true);

                            Toast.makeText(
                                    NewPasswordActivity.this,
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