package com.bazinga.lantoon.retrofit;

import com.bazinga.lantoon.home.chapter.lesson.model.Question;
import com.bazinga.lantoon.home.chapter.model.Chapter;
import com.bazinga.lantoon.registration.langselection.model.Language;
import com.bazinga.lantoon.registration.model.User;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET("Lantoon/public/LanguageHandler.php/languageList")
    Call<List<Language>> getLanguages();

    @GET("Lantoon/public/ChapterHandler.php/chapterlist/slide/{languageid}/{slidenumber}")
    Call<List<Chapter>> getChapter(@Path("languageid") int langid, @Path("slidenumber") int pageno);

    @GET("Lantoon/public/QuestionHandler.php/onelessonquestions/{languageid}/{chapterno}/{lessonno}")
    Call<JsonArray> getQuestions(@Path("languageid") int languageid, @Path("chapterno") int chapterNo, @Path("lessonno") int lessonno);

    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/adduser")
    Call<User> createUser(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/login")
    Call<User> userLogin(@Query("email") String email, @Query("pass") String password, @Query("deviceid") String deviceid);
    /*@Headers("Content-Type: application/json")
    @POST("Lantoon/public/UserHandler.php/login")
    Call<User> userLogin(@Body User user);
*/

    @Streaming
    @GET
    Call<ResponseBody> downloadFileByUrl(@Url String fileUrl);
}