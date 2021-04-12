package com.bazinga.lantoon.retrofit;

import com.bazinga.lantoon.home.chapter.lesson.model.PostLessonResponse;
import com.bazinga.lantoon.home.chapter.lesson.model.Score;
import com.bazinga.lantoon.home.chapter.model.ChapterResponse;
import com.bazinga.lantoon.home.profile.Profile;
import com.bazinga.lantoon.home.profile.ProfileData;
import com.bazinga.lantoon.home.profile.ProfilePicture;
import com.bazinga.lantoon.home.profile.ProfilePictureData;
import com.bazinga.lantoon.login.data.model.LoggedInUser;
import com.bazinga.lantoon.registration.langselection.model.Language;
import com.bazinga.lantoon.registration.model.User;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface ApiInterface {

    //Language List
    @GET("Lantoon/public/LanguageHandler.php/languageList")
    Call<List<Language>> getLanguages();

    //Chapter List
    @GET("Lantoon/public/ChapterHandler.php/chapterlist1/slide/{languageid}/{slidenumber}/{uid}")
    Call<ChapterResponse> getChapter(@Path("languageid") int langid, @Path("slidenumber") int pageno, @Path("uid") String uid);

    //Profile
    @GET("Lantoon/public/UserHandler.php/getprofile/{uid}")
    Call<Profile> getProfile(@Path("uid") String uid);

    //Update Profile
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/updateprofile")
    Call<Profile> updateProfile(@Body ProfileData profileData);

    //Update Profile Picture
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/updateprofilepic")
    Call<ProfilePicture> updateProfilePicture(@Body ProfilePictureData profilePictureData);

    //Lesson Questions
    /*@GET("Lantoon/public/QuestionHandler.php/onelessonquestions/{languageid}/{chapterno}/{lessonno}")
    Call<JsonArray> getQuestions(@Path("languageid") int languageid, @Path("chapterno") int chapterNo, @Path("lessonno") int lessonno);*/
    @GET("Lantoon/public/QuestionHandler.php/onelessonquestionswithreference/{languageid}/{chapterno}/{lessonno}/{reflanguageid}/{uid}")
    Call<JsonObject> getQuestions(@Path("languageid") int languageid, @Path("chapterno") int chapterNo, @Path("lessonno") int lessonno, @Path("reflanguageid") int reflanguageid, @Path("uid") String uid);

    //Signup
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/adduser")
    Call<User> createUser(@Body User user);

    //Score Update
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/ScoreHandler.php/scoreupdate")
    Call<PostLessonResponse> scoreUpdate(@Body Score score);

    //Login
    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/login")
    Call<LoggedInUser> userLogin(@Query("email") String email, @Query("pass") String password, @Query("deviceid") String deviceid);


    //Questions Images and Audio files
    @Streaming
    @GET("Lantoon/public/QuestionHandler.php/zipfile/{languageid}/{chapterno}/{lessonno}/{type}")
    Call<ResponseBody> downloadFileByUrl(@Path("languageid") int langid, @Path("chapterno") int chapterno, @Path("lessonno") int lessonno, @Path("type") int type);

}