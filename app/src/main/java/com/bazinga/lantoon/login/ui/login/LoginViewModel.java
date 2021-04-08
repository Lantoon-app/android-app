package com.bazinga.lantoon.login.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.login.data.model.LoggedInUser;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    LoggedInUser loggedInUser;


    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, String deviceId) {


        try {

            System.out.println("Login input" + username + password);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<LoggedInUser> call = apiInterface.userLogin(username.trim(), password.trim(), deviceId);
            //Call<LoggedInUser> call = apiInterface.userLogin("test@test.com", "12345678", "afsdfsdfsdfsd");
            call.enqueue(new Callback<LoggedInUser>() {
                @Override
                public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                    Log.e("Login onResponse body= ", response.body().toString());
                    if (response.isSuccessful() && response.body() != null) {

                        loggedInUser = response.body();
                        if (loggedInUser.getStatus().getCode() == 1008) {
                            loginResult.setValue(new LoginResult(new LoggedInUserView(loggedInUser.getLoginData())));
                        } else {
                            loginResult.setValue(new LoginResult(loggedInUser.getStatus().getMessage()));
                            //loginResult.setValue(new LoginResult(R.string.login_failed));
                        }

                        Log.d("Login onResponse body= ", new Gson().toJson(response.body()));
                        Log.d("Login onResponse body= ", String.valueOf(response.code()));
                        //Log.d("Login onResponse msg= ", loggedInUser.getData().getEmail());
                    } else {
                        Log.e("Login onResponse msg= ", response.message() + response.code());
                        loginResult.setValue(new LoginResult(response.message() + response.code()));
                    }

                }

                @Override
                public void onFailure(Call<LoggedInUser> call, Throwable t) {
                    call.cancel();
                    t.printStackTrace();
                    loginResult.setValue(new LoginResult(t.getMessage()));
                    Log.e("Login onFailure msg= ", "" + t.getMessage() + " " + t.getLocalizedMessage());
                    return;
                }
            });
        } catch (Exception e) {
            // return new Result.Error(new IOException("Error logging in", e));
            loginResult.setValue(new LoginResult(e.getMessage()));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username != null) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 7;
    }
}