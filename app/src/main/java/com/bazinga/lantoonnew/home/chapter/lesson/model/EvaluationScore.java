package com.bazinga.lantoonnew.home.chapter.lesson.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EvaluationScore {
    // user_id, evaluation_id, language_id, score_details, spent_time

    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("evaluation_id")
    @Expose
    private String evaluation_id;

    @SerializedName("language_id")
    @Expose
    private String language_id;

    @SerializedName("spent_time")
    @Expose
    private String spentTime;

    @SerializedName("score_details")
    @Expose
    private List<ScoreDetails> score_details = null;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public String getEvaluation_id() {
        return evaluation_id;
    }

    public void setEvaluation_id(String evaluation_id) {
        this.evaluation_id = evaluation_id;
    }


    public String getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(String spentTime) {
        this.spentTime = spentTime;
    }


    public List<ScoreDetails> getScore_details() {
        return score_details;
    }

    public void setScore_details(List<ScoreDetails> score_details) {
        this.score_details = score_details;
    }


}
