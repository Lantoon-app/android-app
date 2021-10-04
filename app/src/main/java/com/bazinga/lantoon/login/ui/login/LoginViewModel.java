package com.bazinga.lantoon.login.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.login.data.model.LoggedInUserResponse;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    LoggedInUserResponse loggedInUserResponse;


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
            Call<LoggedInUserResponse> call = apiInterface.userLogin(username.trim(), password.trim(), deviceId);
            //Call<LoggedInUser> call = apiInterface.userLogin("test@test.com", "12345678", "afsdfsdfsdfsd");
            call.enqueue(new Callback<LoggedInUserResponse>() {
                @Override
                public void onResponse(Call<LoggedInUserResponse> call, Response<LoggedInUserResponse> response) {
                   //Log.e("Login onResponse body= ", response.body().toString());
                    if (response.isSuccessful() && response.body() != null) {

                        loggedInUserResponse = response.body();
                        if (loggedInUserResponse.getStatus().getCode() == 1008) {
                            Log.d("Login onResponse body= ", new Gson().toJson(response.body()));
                            Log.d("Login onResponse body= ", String.valueOf(response.body().getLoginData().getSpeakCode()));
                            loginResult.setValue(new LoginResult(new LoggedInUserView(loggedInUserResponse.getLoginData())));
                        } else {
                            loginResult.setValue(new LoginResult(loggedInUserResponse.getStatus().getMessage()));
                            //loginResult.setValue(new LoginResult(R.string.login_failed));
                        }


                        //Log.d("Login onResponse msg= ", loggedInUser.getData().getEmail());
                    } else {
                        //Log.e("Login onResponse msg= ", response.message() + response.code());
                        loginResult.setValue(new LoginResult(response.message() + response.code()));
                    }

                }

                @Override
                public void onFailure(Call<LoggedInUserResponse> call, Throwable t) {
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
        }else return true;
        /*if (username != null) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }*/
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 7;
    }
}