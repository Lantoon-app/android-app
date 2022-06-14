package com.bazinga.lantoonnew.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static boolean isTest = false;
    public static String BASE_TEST_URL = "https://www.lantoon.net/";
    public static String BASE_PROD_URL = "https://www.lantoon.co.in/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            if (isTest) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_TEST_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            } else {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_PROD_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }
        return retrofit;
    }
}