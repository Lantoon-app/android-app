package com.bazinga.lantoon.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static String BASE_URL ="https://www.lantoon.net/";
    //public static String BASE_URL ="https://www.lantoon.co.in/";
        private static Retrofit retrofit;
    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}