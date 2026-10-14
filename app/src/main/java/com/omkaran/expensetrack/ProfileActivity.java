package com.omkaran.expensetrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView tvProfileName;
    TextView tvProfileEmail;
    TextView tvFullName;
    TextView tvEmail;
    TextView tvUserId;
    TextView tvBack;

    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);

        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvUserId = findViewById(R.id.tvUserId);

        tvBack = findViewById(R.id.tvBack);
        btnLogout = findViewById(R.id.btnLogout);

        SharedPreferences prefs =
                getSharedPreferences(
                        "ExpenseTrackPrefs",
                        MODE_PRIVATE
                );

        String fullName = prefs.getString(
                "fullName",
                "User"
        );

        String email = prefs.getString(
                "email",
                "No email"
        );

        int userId = prefs.getInt(
                "userId",
                0
        );

        tvProfileName.setText(fullName);
        tvProfileEmail.setText(email);

        tvFullName.setText(fullName);
        tvEmail.setText(email);
        tvUserId.setText(String.valueOf(userId));

        // Back
        tvBack.setOnClickListener(v -> finish());

        // Logout
        btnLogout.setOnClickListener(v -> {

            prefs.edit().clear().apply();

            Intent intent = new Intent(
                    ProfileActivity.this,
                    MainActivity.class
            );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            finish();
        });
    }
}