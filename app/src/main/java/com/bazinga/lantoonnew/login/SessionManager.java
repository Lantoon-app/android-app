package com.bazinga.lantoonnew.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.bazinga.lantoonnew.GetStartActivity;
import com.bazinga.lantoonnew.SplashActivity;
import com.bazinga.lantoonnew.Tags;
import com.bazinga.lantoonnew.home.HomeActivity;

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
    public static final String KEY_SP_STRING_USERID = "userid";
    public static final String KEY_SP_STRING_USER_NAME = "username";
    public static final String KEY_SP_STRING_USER_PICTURE = "profpicture";
    public static final String KEY_SP_STRING_EMAIL_ID = "emailid";
    public static String KEY_SP_LEARN_LANGUAGE_ID = "learnlang";
    public static String KEY_SP_LEARN_LANGUAGE_NAME = "learnlangname";
    public static String KEY_SP_LEARN_LANGUAGE_NATIVE_NAME = "learnlangnativename";
    public static String KEY_SP_KNOWN_LANGUAGE_ID = "knownlang";
    public static String KEY_SP_KNOWN_LANGUAGE_NAME = "knownlangname";
    public static String KEY_SP_KNOWN_LANGUAGE_NATIVE_NAME = "knownlangnativename";
    public static String KEY_SP_SPEAKE_CODE = "speakcode";
    public static String KEY_SP_REGION_CODE = "regioncode";
    public static String KEY_SP_REGISTRATION_TYPE = "regtype";

    public static String KEY_SP_BASEURL = "baseurl";

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


        editor.putString(KEY_SP_STRING_USER, user);

        editor.commit();
    }

    public void setProfilePic(String strPicture) {

        editor.putString(KEY_SP_STRING_USER_PICTURE, strPicture);

        editor.commit();
    }

    public void setUid(String strUserId) {

        editor.putString(KEY_SP_STRING_USERID, strUserId);

        editor.commit();
    }

    public String getUid() {

        return pref.getString(KEY_SP_STRING_USERID, null);
    }

    public void setUserName(String strName) {

        editor.putString(KEY_SP_STRING_USER_NAME, strName);

        editor.commit();
    }

    public String getUserName() {

        return pref.getString(KEY_SP_STRING_USER_NAME, null);
    }

    public void setEmailId(String emailId) {

        editor.putString(KEY_SP_STRING_EMAIL_ID, emailId);

        editor.commit();
    }

    public String getEmailId() {

        return pref.getString(KEY_SP_STRING_EMAIL_ID, null);
    }

    public void setSpeakCode(String speakCode) {

        editor.putString(KEY_SP_SPEAKE_CODE, speakCode);

        editor.commit();
    }

    public String getSpeakCode() {

        return pref.getString(KEY_SP_SPEAKE_CODE, null);
    }

    public void setLearnLangId(Integer learnLangId) {

        editor.putInt(KEY_SP_LEARN_LANGUAGE_ID, learnLangId);

        editor.commit();
    }

    public Integer getLearnLangId() {

        return pref.getInt(KEY_SP_LEARN_LANGUAGE_ID, 0);
    }

    public void setLearnLangName(String learnLangName) {

        editor.putString(KEY_SP_LEARN_LANGUAGE_NAME, learnLangName);

        editor.commit();
    }

    public String getLearnLangName() {

        return pref.getString(KEY_SP_LEARN_LANGUAGE_NAME, null);
    }

    public void setLearnLangNativeName(String learnLangNativeName) {

        editor.putString(KEY_SP_LEARN_LANGUAGE_NATIVE_NAME, learnLangNativeName);

        editor.commit();
    }

    public String getLearnLangNativeName() {

        return pref.getString(KEY_SP_LEARN_LANGUAGE_NATIVE_NAME, null);
    }

    public void setKnownLangId(Integer knownLangId) {

        editor.putInt(KEY_SP_KNOWN_LANGUAGE_ID, knownLangId);

        editor.commit();
    }

    public Integer getKnownLangId() {

        return pref.getInt(KEY_SP_KNOWN_LANGUAGE_ID, 0);
    }

    public void setKnownLangName(String knownLangName) {

        editor.putString(KEY_SP_KNOWN_LANGUAGE_NAME, knownLangName);

        editor.commit();
    }

    public String getKnownLangName() {

        return pref.getString(KEY_SP_KNOWN_LANGUAGE_NAME, null);
    }

    public void setKnownLangNativeName(String knownLangNativeName) {

        editor.putString(KEY_SP_KNOWN_LANGUAGE_NATIVE_NAME, knownLangNativeName);

        editor.commit();
    }

    public String getKnownLangNativeName() {

        return pref.getString(KEY_SP_KNOWN_LANGUAGE_NATIVE_NAME, null);
    }

    public void setRegionCode(String regionCode) {

        editor.putString(KEY_SP_REGION_CODE, regionCode);

        editor.commit();
    }

    public String getRegionCode() {

        return pref.getString(KEY_SP_REGION_CODE, null);
    }

    public void setRegistrationType(Integer registrationType) {

        editor.putInt(KEY_SP_REGISTRATION_TYPE, registrationType);

        editor.commit();
    }

    public Integer getRegistrationType() {

        return pref.getInt(KEY_SP_REGISTRATION_TYPE, 0);
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
     *
     * @param token
     */
    public void checkLogin(String token) {
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
        } else {
            System.out.println("Login failure");

            Intent i = new Intent(_context, GetStartActivity.class);
            i.putExtra(Tags.TAG_NOTIFICATION_TOKEN, token);
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
   /* public LoginData getUserDetails() {

        // user name
        LoginData loginData = new GsonBuilder().create().fromJson(pref.getString(KEY_SP_STRING_USER, null), LoginData.class);


        // return user
        return loginData;
    }*/


    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, SplashActivity.class);
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
