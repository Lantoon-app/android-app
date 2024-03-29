package com.bazinga.lantoon.login.ui.login;

import com.bazinga.lantoon.login.data.model.LoginData;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private LoginData loginData;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(LoginData loginData) {
        this.loginData = loginData;
    }

    public LoginData getloginData() {
        return loginData;
    }
}