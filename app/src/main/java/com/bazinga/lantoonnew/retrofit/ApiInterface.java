package com.bazinga.lantoonnew.retrofit;

import com.bazinga.lantoonnew.home.changepassword.model.ChangePasswordResponse;
import com.bazinga.lantoonnew.home.chapter.lesson.model.PostLessonResponse;
import com.bazinga.lantoonnew.home.chapter.lesson.model.Score;
import com.bazinga.lantoonnew.home.chapter.model.ChapterResponse;
import com.bazinga.lantoonnew.home.leaderboard.model.LeaderResponse;
import com.bazinga.lantoonnew.home.mylanguage.model.MyLanguageResponse;
import com.bazinga.lantoonnew.home.payment.hash.HashCodeResponse;
import com.bazinga.lantoonnew.home.payment.model.PaymentPackageResponse;
import com.bazinga.lantoonnew.home.payment.model.PurchaseResponse;
import com.bazinga.lantoonnew.home.payment.model.TransactionResponse;
import com.bazinga.lantoonnew.home.profile.Profile;
import com.bazinga.lantoonnew.home.profile.ProfileData;
import com.bazinga.lantoonnew.home.profile.ProfilePicture;
import com.bazinga.lantoonnew.home.target.model.TargetResponse;
import com.bazinga.lantoonnew.login.data.model.LoggedInUserResponse;
import com.bazinga.lantoonnew.login.forget.OtpResponse;
import com.bazinga.lantoonnew.registration.langselection.model.Language;
import com.bazinga.lantoonnew.registration.model.User;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface ApiInterface {

    //Language List
    @GET("Lantoon/public/LanguageHandler.php/languageList")
    Call<List<Language>> getLanguages();

    //http://lantoon.net/Lantoon/public/ChapterHandler.php/chapterlistandroidv2/slide/%7Blanguageid%7D/%7Bslidenumber%7D/%7Buid%7D/%7Bversioncode%7D/%7Bdeviceid%7D
    //Chapter List with evaluation
    @GET("Lantoon/public/ChapterHandler.php/chapterlistandroidv2/slide/{languageid}/{slidenumber}/{uid}/{versioncode}/{deviceid}")
    Call<ChapterResponse> getChapterV2(@Path("languageid") int langid, @Path("slidenumber") int pageno, @Path("uid") String uid, @Path("versioncode") int versioncode, @Path("deviceid") String deviceid);

    //Chapter List with logout concept
    @GET("Lantoon/public/ChapterHandler.php/chapterlistandroid/slide/{languageid}/{slidenumber}/{uid}/{versioncode}/{deviceid}")
    Call<ChapterResponse> getChapter(@Path("languageid") int langid, @Path("slidenumber") int pageno, @Path("uid") String uid, @Path("versioncode") int versioncode, @Path("deviceid") String deviceid);

    //Chapter List
    @GET("Lantoon/public/ChapterHandler.php/chapterlistandroid/slide/{languageid}/{slidenumber}/{uid}/{versioncode}")
    Call<ChapterResponse> getChapter(@Path("languageid") int langid, @Path("slidenumber") int pageno, @Path("uid") String uid, @Path("versioncode") int versioncode);


 /*//Chapter List
    @GET("Lantoon/public/ChapterHandler.php/chapterlist1/slide/{languageid}/{slidenumber}/{uid}")
    Call<ChapterResponse> getChapter(@Path("languageid") int langid, @Path("slidenumber") int pageno, @Path("uid") String uid);*/

    //Profile
    @GET("Lantoon/public/UserHandler.php/getprofile/{uid}")
    Call<Profile> getProfile(@Path("uid") String uid);

    //My Languages
    @GET("Lantoon/public/UserHandler.php/mylanguages/{uid}")
    Call<MyLanguageResponse> getMyLanguage(@Path("uid") String uid);

    //Update Profile
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/updateprofile")
    Call<Profile> updateProfile(@Body ProfileData profileData);

    //Update MyLearnLanguage
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/updatemylanguage")
    Call<LoggedInUserResponse> updateLanguage(@Query("uid") String uid, @Query("learnlang") String learnlang, @Query("knownlang") int knownlang);

    //Update Refer Language
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/changeknownlanguage")
    Call<LoggedInUserResponse> updateReferLanguage(@Query("uid") String uid, @Query("learnlang") int learnlang, @Query("knownlang") String knownlang);

    //Change Password
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/changepassword")
    Call<ChangePasswordResponse> changePassword(@Query("uid") String uid, @Query("newpass") String newpass, @Query("oldpass") String oldpass);

    //Post OTP
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/validatemail")
    Call<OtpResponse> getOtp(@Query("email") String email);

    //Change Forget Password
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/changeforgetpass")
    Call<JsonObject> changeForgetPassword(@Query("uid") String uid, @Query("newpass") String newpass);

    /*//Update Profile Picture
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/updateprofilepic")
    Call<ProfilePicture> updateProfilePicture(@Body ProfilePictureData profilePictureData);*/
    //Update Profile Picture
    @Multipart
    @POST("Lantoon/public/UserHandler.php/updateprofilepic")
    Call<ProfilePicture> updateProfilePicture(@Part("userid") RequestBody userid,
                                              @Part MultipartBody.Part file);

    //Lesson Questions
    /*@GET("Lantoon/public/QuestionHandler.php/onelessonquestions/{languageid}/{chapterno}/{lessonno}")
    Call<JsonArray> getQuestions(@Path("languageid") int languageid, @Path("chapterno") int chapterNo, @Path("lessonno") int lessonno);*/
    @GET("Lantoon/public/QuestionHandler.php/onelessonquestionswithreference/{languageid}/{chapterno}/{lessonno}/{reflanguageid}/{uid}")
    Call<JsonObject> getQuestions(@Path("languageid") int languageid, @Path("chapterno") int chapterNo, @Path("lessonno") int lessonno, @Path("reflanguageid") int reflanguageid, @Path("uid") String uid);
    //https://lantoon.net/Lantoon/public/QuestionHandler.php/completedchapterlessonquestions/{languageid}/{chapterno}/{reflanguageid}/{uid}

    //Lesson Questions chapter type 2
    /*@GET("Lantoon/public/QuestionHandler.php/onelessonquestions/{languageid}/{chapterno}/{lessonno}")
    Call<JsonArray> getQuestions(@Path("languageid") int languageid, @Path("chapterno") int chapterNo, @Path("lessonno") int lessonno);*/
    @GET("Lantoon/public/QuestionHandler.php/onelessonrevisionquestionswithreference/{languageid}/{chapterno}/{lessonno}/{reflanguageid}/{uid}")
    Call<JsonObject> getQuestionsType2(@Path("languageid") int languageid, @Path("chapterno") int chapterNo, @Path("lessonno") int lessonno, @Path("reflanguageid") int reflanguageid, @Path("uid") String uid);
    //https://lantoon.net/Lantoon/public/QuestionHandler.php/completedchapterlessonquestions/{languageid}/{chapterno}/{reflanguageid}/{uid}

    //Completed Lesson Questions (Not Used)
    @GET("Lantoon/public/QuestionHandler.php/completedchapterlessonquestions/{languageid}/{chapterno}/{reflanguageid}/{uid}")
    Call<JsonObject> getCompletedQuestions(@Path("languageid") int languageid, @Path("chapterno") int chapterNo, @Path("reflanguageid") int reflanguageid, @Path("uid") String uid);

    //Signup
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/registeruser")
    Call<LoggedInUserResponse> createUser(@Body User user);

    //Score Update
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/ScoreHandler.php/scoreupdate")
    Call<PostLessonResponse> scoreUpdate(@Body Score score);

    //Login
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/login")
    Call<LoggedInUserResponse> userLogin(@Query("email") String email, @Query("pass") String password, @Query("deviceid") String deviceid, @Query("notify_token") String notify_token, @Query("os_type") String os_type);

    //Login test
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/login")
    Call<LoggedInUserResponse> userLogin(@Query("email") String email, @Query("pass") String password, @Query("deviceid") String deviceid, @Query("notify_token") String notify_token, @Query("os_type") String os_type, @Query("device_modal") String device_modal, @Query("login_type") Integer login_type);


    //Questions Images and Audio files
    @Streaming
    @GET("Lantoon/public/QuestionHandler.php/zipfile/{languageid}/{chapterno}/{lessonno}/{type}")
    Call<ResponseBody> downloadFileByUrl(@Path("languageid") int langid, @Path("chapterno") int chapterno, @Path("lessonno") int lessonno, @Path("type") int type);

    //Questions Images and Audio files
    @Streaming
    @GET("Lantoon/public/QuestionHandler.php/zipfilenew/{languageid}/{chapterno}/{lessonno}")
    Call<ResponseBody> downloadFileByUrlNew(@Path("languageid") int langid, @Path("chapterno") int chapterno, @Path("lessonno") int lessonno);

    //Leaderboard old
    @GET("Lantoon/public/ScoreHandler.php/leaderboard/{slideno}/{uid}/{langid}")
    Call<LeaderResponse> getLeaders(@Path("slideno") int slideno, @Path("uid") String uid, @Path("langid") int langid);

   //Leaderboard global
    @GET("Lantoon/public/ScoreHandler.php/leaderboardglobal/{slideno}/{uid}/{langid}")
    Call<LeaderResponse> getLeadersGlobal(@Path("slideno") int slideno, @Path("uid") String uid, @Path("langid") int langid);

   //Leaderboard institutional
    @GET("Lantoon/public/ScoreHandler.php/leaderboardinstitutional/{slideno}/{uid}/{langid}")
    Call<LeaderResponse> getLeadersInstitutional(@Path("slideno") int slideno, @Path("uid") String uid, @Path("langid") int langid);



    //Targets
    @GET("Lantoon/public/TargetHandler.php/mytargets/{uid}")
    Call<TargetResponse> getTargets(@Path("uid") String uid);

    //Payment Packages
    @GET("Lantoon/public/PackageHandler.php/fetchmypackages/{user_id}")
    Call<PaymentPackageResponse> getPaymentPackages(@Path("user_id") String uid);

    //Payment Get Transactionid
    @GET("Lantoon/public/PackageHandler.php/gettransactionid")
    Call<TransactionResponse> getPaymentTxnId();

    //Payment convert to hashcode
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/PackageHandler.php/converttohashcode")
    Call<HashCodeResponse> getPaymentHashCode(@Query("hash_data") String hash_data);

    //Payment after success
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/PackageHandler.php/purchasesuccess")
    Call<PurchaseResponse> postPaymentPurchaseDetails(@Query("transaction_id") String transaction_id,
                                              @Query("package_id") String package_id ,
                                              @Query("user_id") String user_id,
                                              @Query("language") String language,
                                              @Query("total_amount") String total_amount,
                                              @Query("paid_amount") String paid_amount,
                                              @Query("payment_type") String payment_type,
                                              @Query("chapters_unlocked") String chapters_unlocked,
                                              @Query("duration_in_days") String duration_in_days);


}