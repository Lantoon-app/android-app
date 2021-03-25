package com.bazinga.lantoon;

import android.os.Environment;

import java.io.File;

public class Utils {

    public static String TAG_EMAILID = "email";
    public static String TAG_USERNAME = "uname";
    public static String TAG_PASSWORD = "password";
    public static String TAG_COUNTRY_CODE = "countrycode";
    public static String TAG_PHONE_NUMBER = "phone";
    public static String TAG_REGION = "";
    public static String TAG_INSTITUTE = "";
    public static String TAG_GROUP_CODE = "";
    public static String TAG_LEARN_LANGUAGE = "learnlang";
    public static String TAG_KNOWN_LANGUAGE = "knownlang";
    public static String TAG_DEVICE_ID = "deviceId";
    public static String TAG_USER_ROLE = "userrole";
    public static String TAG_CURRENT_LOCATION = "currentlocation";
    public static String TAG_MINS_PER_DAY = "minsperday";
    public static String TAG_REGISTRATION_TYPE = "registrationtype";
    public static String TAG_QUESTION_TYPE= "qtype";
    public static String TAG_QUESTION_NO="qno";
    public static String TAG_QUESTIONS_TOTAL="total";

    public static String ZIP_FILE_DESTINATION_PATH= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    public static String FILE_DESTINATION_PATH= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    public static String IMAGE_FILE_DESTINATION_FOLDER=File.separator +"content"+ File.separator +"images"+File.separator;
    public static String AUDIO_FILE_DESTINATION_FOLDER=File.separator +"content"+ File.separator +"audio"+File.separator;
    public static String IMAGEQUES_FILE_DESTINATION_FOLDER=File.separator +"content"+ File.separator +"imageques"+File.separator;
    public static String AUDIOANS_FILE_DESTINATION_FOLDER=File.separator +"content"+ File.separator +"audioans"+File.separator;

   /* public static String IMAGE_FILE_DESTINATION_FOLDER_PATH= ZIP_FILE_DESTINATION_PATH+IMAGE_FILE_DESTINATION_FOLDER+"/";
    public static String AUDIO_FILE_DESTINATION_FOLDER_PATH= ZIP_FILE_DESTINATION_PATH+AUDIO_FILE_DESTINATION_FOLDER+"/";
    public static String IMAGEQUES_FILE_DESTINATION_FOLDER_PATH= ZIP_FILE_DESTINATION_PATH+IMAGEQUES_FILE_DESTINATION_FOLDER+"/";
    public static String AUDIOANS_FILE_DESTINATION_FOLDER_PATH= ZIP_FILE_DESTINATION_PATH+AUDIOANS_FILE_DESTINATION_FOLDER+"/";*/

}
