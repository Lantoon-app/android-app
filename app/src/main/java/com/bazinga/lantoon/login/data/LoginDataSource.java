package com.bazinga.lantoon.login.data;

import android.util.Log;

import com.bazinga.lantoon.login.data.model.LoggedInUser;
import com.bazinga.lantoon.registration.model.User;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            /*LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);*/
            System.out.println("Login input" + username + password);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            //Call<User> call = apiInterface.userLogin(username.trim(),password.trim(),"asdfsdfsd");
            Call<User> call = apiInterface.userLogin("test@test.com","12345678","asdfsdfsd");
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.e("Login onResponse body= ", response.body().toString());
                    if (response.isSuccessful() && response.body() != null) {


                        Log.e("Login onResponse body= ", new Gson().toJson(response.body()));
                        Log.e("Login onResponse body= ", String.valueOf(response.code()));
                        Log.e("Login onResponse msg= ", response.message());
                    } else {
                        Log.e("Login onResponse msg= ", response.message() + response.code());
                    }
                    LoggedInUser fakeUser =
                            new LoggedInUser(
                                    java.util.UUID.randomUUID().toString(),
                                    "Jane Doe");
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    call.cancel();
                    t.printStackTrace();
                    Log.e("Login onFailure msg= ", "" + t.getMessage() + " " + t.getLocalizedMessage());
                    return;
                }
            });
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
        return null;
    }

    public void logout() {
        // TODO: revoke authentication
    }
}