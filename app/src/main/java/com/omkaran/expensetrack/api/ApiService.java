package com.omkaran.expensetrack.api;

import com.omkaran.expensetrack.model.LoginRequest;
import com.omkaran.expensetrack.model.LoginResponse;
import com.omkaran.expensetrack.model.RegisterResponse;
import com.omkaran.expensetrack.model.UserResponse;
import com.omkaran.expensetrack.model.SummaryResponse;
import com.omkaran.expensetrack.model.TransactionRequest;
import com.omkaran.expensetrack.model.TransactionsResponse;
import com.omkaran.expensetrack.model.RegisterRequest;
import com.omkaran.expensetrack.model.ForgotPasswordRequest;
import com.omkaran.expensetrack.model.ForgotPasswordResponse;
import com.omkaran.expensetrack.model.VerifyOtpRequest;
import com.omkaran.expensetrack.model.VerifyOtpResponse;
import com.omkaran.expensetrack.model.ResetPasswordRequest;
import com.omkaran.expensetrack.model.ResetPasswordResponse;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/Auth/login")
    Call<LoginResponse> login(
            @Body LoginRequest request
    );

    @GET("api/Auth/me")
    Call<UserResponse> getCurrentUser();

    @GET("api/Transactions/summary")
    Call<SummaryResponse> getSummary();

    @POST("api/Transactions")
    Call<Void> addTransaction(
            @Body TransactionRequest request
    );
    @GET("api/Transactions")
    Call<TransactionsResponse> getTransactions();
    @POST("api/Auth/register")
    Call<RegisterResponse> register(
            @Body RegisterRequest request
    );
    @POST("api/Auth/forgot-password")
    Call<ForgotPasswordResponse> forgotPassword(
            @Body ForgotPasswordRequest request
    );
    @POST("api/Auth/verify-otp")
    Call<VerifyOtpResponse> verifyOtp(
            @Body VerifyOtpRequest request
    );
    @POST("api/Auth/reset-password")
    Call<ResetPasswordResponse> resetPassword(
            @Body ResetPasswordRequest request
    );
}