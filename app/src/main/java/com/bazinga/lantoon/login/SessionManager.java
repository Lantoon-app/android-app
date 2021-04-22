package com.bazinga.lantoon.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.bazinga.lantoon.MainActivity;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.profile.ProfileData;
import com.bazinga.lantoon.login.data.model.LoginData;
import com.bazinga.lantoon.login.ui.login.LoginActivity;
import com.bazinga.lantoon.registration.model.User;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "LantoonPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_SP_STRING_USER = "userdata";
    public static final String KEY_SP_STRING_USER_NAME = "username";
    public static final String KEY_SP_STRING_USER_PICTURE = "picture";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String user) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing user in pref
        editor.putString(KEY_SP_STRING_USER, user);


        // commit changes
        editor.commit();
    }

    public void setProfilePic(String strPicture) {
        // Storing login value as TRUE
        // editor.putBoolean(IS_LOGIN, true);

        // Storing user in pref
        editor.putString(KEY_SP_STRING_USER_PICTURE, strPicture);


        // commit changes
        editor.commit();
    }

    public void setUserName(String strName) {
        // Storing login value as TRUE
        // editor.putBoolean(IS_LOGIN, true);

        // Storing user in pref
        editor.putString(KEY_SP_STRING_USER_NAME, strName);


        // commit changes
        editor.commit();
    }

    public String getUserName() {
        // Storing login value as TRUE
        // editor.putBoolean(IS_LOGIN, true);

        // Storing user in pref
        String name = pref.getString(KEY_SP_STRING_USER_NAME, null);


        return name;
    }

    public String getProfilePic() {
        // Storing login value as TRUE
        // editor.putBoolean(IS_LOGIN, true);
        String picture = "";
        // Storing user in pref
        if (pref != null)
            picture = pref.getString(KEY_SP_STRING_USER_PICTURE, null);


        return picture;
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (this.isLoggedIn()) {
            System.out.println("Login success");
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, HomeActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    /**
     * Get stored session data
     */
    public LoginData getUserDetails() {

        // user name
        LoginData loginData = new GsonBuilder().create().fromJson(pref.getString(KEY_SP_STRING_USER, null), LoginData.class);


        // return user
        return loginData;
    }


    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
