package com.bazinga.lantoonnew.home.chapter.lesson;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoonnew.Tags;
import com.bazinga.lantoonnew.home.chapter.lesson.model.Question;
import com.bazinga.lantoonnew.home.chapter.lesson.ui.D2Fragment;
import com.bazinga.lantoonnew.home.chapter.lesson.ui.L1Fragment;
import com.bazinga.lantoonnew.home.chapter.lesson.ui.P1Fragment;
import com.bazinga.lantoonnew.home.chapter.lesson.ui.P2Fragment;
import com.bazinga.lantoonnew.home.chapter.lesson.ui.P3Fragment;
import com.bazinga.lantoonnew.home.chapter.lesson.ui.QFragment;
import com.bazinga.lantoonnew.home.chapter.lesson.ui.QP1Fragment;
import com.bazinga.lantoonnew.home.chapter.lesson.ui.QP2Fragment;
import com.bazinga.lantoonnew.home.chapter.lesson.ui.QP3Fragment;
import com.bazinga.lantoonnew.retrofit.ApiClient;
import com.bazinga.lantoonnew.retrofit.ApiInterface;
import com.bazinga.lantoonnew.retrofit.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsViewModel extends ViewModel {

    MutableLiveData<List<Fragment>> questionsLiveData;
    private static final String TAG = QuestionsViewModel.class.getSimpleName();

    private MutableLiveData<TaskModel> progressTask;
    private TaskModel mTask;
    private QuestionsAsyncTask taskAsync;
    DownloadZipFileTask downloadZipFileTask;
    boolean isEvaluation;
    int langid, chaperno, lessonno, knownLang, chapterType;

    public QuestionsViewModel(boolean isEvaluation, int langid, int chaperno, int lessonno, int knownLang, int chapterType) {
        Log.d("numbers", String.valueOf(langid + chaperno + lessonno + knownLang));
        /*if(ApiClient.isTest){
            this.isEvaluation = isEvaluation;
            if(isEvaluation)
            this.langid = 1;
            else this.langid = 100;
            this.chaperno = 1;
            this.lessonno = 1;
            this.knownLang = 1;
            this.chapterType = chapterType;
        }else {*/
            this.isEvaluation = isEvaluation;
            this.langid = langid;
            this.chaperno = chaperno;
            this.lessonno = lessonno;
            this.knownLang = knownLang;
            this.chapterType = chapterType;
        //}
    }

    private void startTask() {
        Log.d(TAG, "startTask: ");
        if (mTask == null) mTask = new TaskModel(TaskState.STOP);
        taskAsync = new QuestionsAsyncTask();
        taskAsync.execute();
    }

    public void stopTask() {
        taskAsync.cancel(true);
    }

    public MutableLiveData<TaskModel> getProgressTask() {

        if (progressTask == null) {
            progressTask = new MutableLiveData<>();
            startTask();
        }

        return progressTask;
    }

    public class QuestionsAsyncTask extends AsyncTask<Void, Float, Boolean> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute: ");
            mTask.setStatus(TaskState.RUNNING);
            progressTask.setValue(mTask);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i(TAG, "doInBackground: ");

            /*downloadZipFile(langid, chaperno, lessonno, 1);
            downloadZipFile(langid, chaperno, lessonno, 2);
            downloadZipFile(langid, chaperno, lessonno, 3);
            downloadZipFile(langid, chaperno, lessonno, 4);*/
            if (chapterType == 1)
                downloadZipFile(langid, chaperno, lessonno);
            else
                questionsFragmentData(isEvaluation,langid, chaperno, lessonno, knownLang, QuestionsActivity.strUserId);

            return true;
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            Log.d(TAG, "onProgressUpdate() called with: values = [" + Arrays.toString(values) + "]");

            mTask.setValue(values[0]);
            progressTask.postValue(mTask);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Log.i(TAG, "onPostExecute: ");
            //Finalized


        }

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
        mTask.setStatus(TaskState.CANCELLED);
        progressTask.setValue(mTask);
        taskAsync.cancel(true);
    }

    private void downloadZipFile(int langid, int chaperno, int lessonno) {
        Log.d("isRandomQuestion", String.valueOf(QuestionsActivity.isRandomQuestion));
        String folderStruc;
        if (!QuestionsActivity.isRandomQuestion)
            folderStruc = String.valueOf(langid + "/" + chaperno + "/" + lessonno + "/");
        else folderStruc = String.valueOf(langid + "/" + chaperno + "/");

        Log.d("isRandomQuestion", String.valueOf(QuestionsActivity.isRandomQuestion) + " " + folderStruc);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        //Call<ResponseBody> call = apiInterface.downloadFileByUrl(langid, chaperno, lessonno, type);
        Call<ResponseBody> call = apiInterface.downloadFileByUrlNew(langid, chaperno, lessonno);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Got the body for the file " + response.message().toString());

                    //Toast.makeText(get, "Downloading...", Toast.LENGTH_SHORT).show();

                    downloadZipFileTask = new DownloadZipFileTask(folderStruc);
                    downloadZipFileTask.execute(response.body());

                } else {
                    Log.d(TAG, "Connection failed " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private class DownloadZipFileTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, String> {
        //int type = 0;
        String folerStruc = "";

        public DownloadZipFileTask(String folerStruc) {
            //this.type = type;
            this.folerStruc = folerStruc;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(ResponseBody... urls) {
            //Copy you logic to calculate progress and call
            try {
                saveToDisk(urls[0], "1.zip", folerStruc);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {

            Log.d("API123", progress[0].second + " ");

            if (progress[0].first == 100)
                //Toast.makeText(getApplicationContext(), "File downloaded successfully", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "File downloaded successfully");


            if (progress[0].second > 0) {
                int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);
                //progressBar.setProgress(currentProgress);

                //txtProgressPercent.setText("Progress " + currentProgress + "%");

            }

            if (progress[0].first == -1) {
                //Toast.makeText(getApplicationContext(), "Download failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Download failed");
            }

        }

        public void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveToDisk(ResponseBody body, String filename, String folerStruc) throws Exception {
        try {
            String folderName = "";
            File destinationFile = new File(QuestionsActivity.strFilePath + File.separator, filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                Log.d("destinationFile", destinationFile.getPath());
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);
                byte data[] = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                Log.d(TAG, "File Size=" + fileSize);
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    progress += count;
                    Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                    downloadZipFileTask.doProgress(pairs);
                    Log.d(TAG, "Progress: " + progress + "/" + fileSize + " >>>> " + (float) progress / fileSize);
                }

                outputStream.flush();

                Log.d(TAG, destinationFile.getParent());
                Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                downloadZipFileTask.doProgress(pairs);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                downloadZipFileTask.doProgress(pairs);
                Log.d(TAG, "Failed to save the file!");
                return;
            } finally {

                //UnzipUtility unzipUtility = new UnzipUtility();
                /*if (type == 1)
                    folderName = Utils.IMAGE_FILE_DESTINATION_FOLDER;
                if (type == 2)
                    folderName = Utils.AUDIO_FILE_DESTINATION_FOLDER;
                if (type == 3)
                    folderName = Utils.IMAGEQUES_FILE_DESTINATION_FOLDER;
                if (type == 4)
                    folderName = Utils.AUDIOANS_FILE_DESTINATION_FOLDER;*/
                Log.d("unziploc", QuestionsActivity.strFilePath + Tags.FILE_DESTINATION_FOLDER + folerStruc);
                unzip(destinationFile.getPath(), QuestionsActivity.strFilePath + Tags.FILE_DESTINATION_FOLDER + folerStruc);

                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        }
    }

    private void questionsFragmentData(boolean isEvaluation, int langid, int chaperno, int lessonno, int reflanguageid, String uid) {
        System.out.println("langid" + langid + "chaperno" + chaperno + "lessonno" + lessonno + "uid" + uid);
        questionsLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call;
        if (chapterType == 1)
            call = apiInterface.getQuestions(langid, chaperno, lessonno, reflanguageid, uid);
        else if(chapterType == 2 && isEvaluation)
            call = apiInterface.getEvaluationQuestions(langid, chaperno);
        else
            call = apiInterface.getQuestionsType2(langid, chaperno, lessonno, reflanguageid, uid);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    Status status = gson.fromJson(response.body().get("status").getAsJsonObject(), Status.class);

                    //int statusCode = response.body().get("status").getAsJsonObject().get("code").getAsInt();
                    if (status.getCode() == 1022) {
                        //Log.d("response ", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                        JsonObject jsonObject = response.body().get("data").getAsJsonObject();
                        Log.d("response ", new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));


                        List<Fragment> fragments = buildFragments(jsonObject);
                        questionsLiveData.setValue(fragments);
                        mTask.setStatus(TaskState.COMPLETED);
                        progressTask.setValue(mTask);
                    }

                } else {
                    Log.e("response message= ", response.message() + response.code());
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                call.cancel();
                t.printStackTrace();
                Log.e("response ERROR= ", "" + t.getMessage() + " " + t.getLocalizedMessage());
            }
        });
    }

    public LiveData<List<Fragment>> getQuestionsMutableLiveData() {
        return questionsLiveData;
    }

    private List<Fragment> buildFragments(@NonNull JsonObject jsonObject) {
        List<Fragment> fragments = new ArrayList<Fragment>();


        Log.d("jaonobj aize", String.valueOf(jsonObject.size()));
        int f = 0;
        int totalQuestions = jsonObject.size();
        for (int i = 1; i < totalQuestions + 1; i++) {

            JsonObject j = jsonObject.getAsJsonArray(String.valueOf(i)).get(0).getAsJsonObject();
            Gson gson = new Gson();
            Question question = gson.fromJson(j, Question.class);
            String qtype = j.get("q_type").toString();
            String ss = "\"p1\"";

            if (jsonObject.get(String.valueOf(i)).getAsJsonArray().size() > 0 && qtype.contains("\"l1\"")) {

                fragments.add(f, L1Fragment.newInstance(i, totalQuestions, jsonObject.getAsJsonArray(String.valueOf(i)).toString()));
            }
            if (qtype.contains("\"p1\"")) {

                fragments.add(f, P1Fragment.newInstance(i, totalQuestions, j.toString()));
            }
            if (qtype.contains("\"p2\"")) {

                fragments.add(f, P2Fragment.newInstance(i, totalQuestions, j.toString()));
            }
            if (qtype.contains("\"p3\"")) {

                fragments.add(f, P3Fragment.newInstance(i, totalQuestions, j.toString()));
            }
            if (qtype.contains("\"q\"")) {

                fragments.add(f, QFragment.newInstance(i, totalQuestions, j.toString()));
            }
            if (qtype.contains("\"qp1\"")) {

                fragments.add(f, QP1Fragment.newInstance(i, totalQuestions, j.toString()));
            }
            if (qtype.contains("\"qp2\"")) {

                fragments.add(f, QP2Fragment.newInstance(i, totalQuestions, j.toString()));
            }
            if (qtype.contains("\"qp3\"")) {

                fragments.add(f, QP3Fragment.newInstance(i, totalQuestions, j.toString()));
            }
            if (qtype.contains("\"d2\"")) {

                fragments.add(f, D2Fragment.newInstance(i, totalQuestions, j.toString()));
            }
            f++;
        }


        return fragments;
    }

    public void unzip(String zipFilePath, String unzipAtLocation) throws Exception {

        Log.d("zipFilePath", zipFilePath);
        Log.d("unzipAtLocation", unzipAtLocation);
        File archive = new File(zipFilePath);

        try {

            ZipFile zipfile = new ZipFile(archive);

            for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {

                ZipEntry entry = (ZipEntry) e.nextElement();

                unzipEntry(zipfile, entry, unzipAtLocation);
            }
            questionsFragmentData(isEvaluation, langid, chaperno, lessonno, knownLang, QuestionsActivity.strUserId);
            Log.d("unzip", "done");
        } catch (Exception e) {

            Log.e("Unzip zip", "Unzip exception", e);
        }
    }

    private void unzipEntry(ZipFile zipfile, ZipEntry entry, String outputDir) throws IOException {

        if (entry.isDirectory()) {
            createDir(new File(outputDir, entry.getName()));
            return;
        }

        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()) {
            createDir(outputFile.getParentFile());
        }

        Log.v("ZIP E", "Extracting: " + entry);

        InputStream zin = zipfile.getInputStream(entry);
        BufferedInputStream inputStream = new BufferedInputStream(zin);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

        try {

            //IOUtils.copy(inputStream, outputStream);

            try {

                for (int c = inputStream.read(); c != -1; c = inputStream.read()) {
                    outputStream.write(c);
                }

            } finally {

                outputStream.close();
            }

        } finally {
            outputStream.close();
            inputStream.close();
        }
    }

    private void createDir(File dir) {

        if (dir.exists()) {
            return;
        }

        Log.v("ZIP E", "Creating dir " + dir.getName());

        if (!dir.mkdirs()) {

            throw new RuntimeException("Can not create dir " + dir);
        }
    }
}
