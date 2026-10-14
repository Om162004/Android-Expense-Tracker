package com.omkaran.expensetrack.api;

import android.content.Context;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL =
            "http://10.66.253.121:5066/";

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(Context context) {

        if (retrofit == null) {

            Interceptor authInterceptor = chain -> {

                Request originalRequest = chain.request();

                String token = context
                        .getSharedPreferences(
                                "ExpenseTrackPrefs",
                                Context.MODE_PRIVATE
                        )
                        .getString("token", null);

                Request.Builder requestBuilder =
                        originalRequest.newBuilder();

                if (token != null && !token.isEmpty()) {

                    requestBuilder.addHeader(
                            "Authorization",
                            "Bearer " + token
                    );
                }

                return chain.proceed(
                        requestBuilder.build()
                );
            };

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    )
                    .build();
        }

        return retrofit;
    }

    public static ApiService getApiService(Context context) {

        return getRetrofitInstance(context)
                .create(ApiService.class);
    }
}