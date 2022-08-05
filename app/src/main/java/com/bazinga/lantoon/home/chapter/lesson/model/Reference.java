package com.bazinga.lantoon.home.chapter.lesson.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reference {
    @SerializedName("word")
    @Expose
    private String word;
    @SerializedName("cell_value")
    @Expose
    private String cellValue;
    @SerializedName("audio_path")
    @Expose
    private String audioPath;
    @SerializedName("ans_word")
    @Expose
    private String ansWord;
    @SerializedName("ans_audio_path")
    @Expose
    private String ansAudioPath;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getCellValue() {
        return cellValue;
    }

    public void setCellValue(String cellValue) {
        this.cellValue = cellValue;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getAnsWord() {
        return ansWord;
    }

    public void setAnsWord(String ansWord) {
        this.ansWord = ansWord;
    }

    public String getAnsAudioPath() {
        return ansAudioPath;
    }

    public void setAnsAudioPath(String ansAudioPath) {
        this.ansAudioPath = ansAudioPath;
    }
}
