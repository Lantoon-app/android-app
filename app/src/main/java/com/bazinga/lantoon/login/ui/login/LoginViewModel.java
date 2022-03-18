package com.bazinga.lantoon.login.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.widget.Toast;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.login.data.model.LoggedInUserResponse;
import com.bazinga.lantoon.registration.DurationSelectionActivity;
import com.bazinga.lantoon.registration.model.User;
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
    LoggedInUserResponse loggedInUserResponse;


    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, String deviceId, String notify_token, String device_model, int login_type) {


        try {

            System.out.println("Login input" + username + password);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<LoggedInUserResponse> call = apiInterface.userLogin(username.trim(), password.trim(), deviceId, notify_token, "Android", device_model, login_type);
            //Call<LoggedInUser> call = apiInterface.userLogin("test@test.com", "12345678", "afsdfsdfsdfsd");
            call.enqueue(new Callback<LoggedInUserResponse>() {
                @Override
                public void onResponse(Call<LoggedInUserResponse> call, Response<LoggedInUserResponse> response) {
                    //Log.e("Login onResponse body= ", response.body().toString());
                    if (response.isSuccessful() && response.body() != null) {

                        loggedInUserResponse = response.body();
                        if (loggedInUserResponse.getStatus().getCode() == 1008) {
                            Log.d("Login onResponse body= ", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
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

    public void register(User user) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoggedInUserResponse> call = apiInterface.createUser(user);
        call.enqueue(new Callback<LoggedInUserResponse>() {
            @Override
            public void onResponse(Call<LoggedInUserResponse> call, Response<LoggedInUserResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    loggedInUserResponse = response.body();
                    if (loggedInUserResponse.getStatus().getCode() == 1001) {
                        Log.d("Register onResponse body= ", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                        Log.d("Register onResponse body= ", String.valueOf(response.body().getLoginData().getSpeakCode()));
                        loginResult.setValue(new LoginResult(new LoggedInUserView(loggedInUserResponse.getLoginData())));
                    } else {
                        loginResult.setValue(new LoginResult(loggedInUserResponse.getStatus().getMessage()));

                    }

                } else {
                    loginResult.setValue(new LoginResult(response.message() + response.code()));
                }
            }

            @Override
            public void onFailure(Call<LoggedInUserResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                loginResult.setValue(new LoginResult(t.getMessage()));
                Log.e("Register onFailure msg= ", "" + t.getMessage() + " " + t.getLocalizedMessage());
                return;
            }
        });
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
        } else return true;

    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 7;
    }
}