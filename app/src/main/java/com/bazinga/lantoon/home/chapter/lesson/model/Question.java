package com.bazinga.lantoon.home.chapter.lesson.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {
    @SerializedName("qid")
    @Expose
    private String qid;
    @SerializedName("qno")
    @Expose
    private String qno;
    @SerializedName("word")
    @Expose
    private String word;
    @SerializedName("cell_value")
    @Expose
    private String cellValue;
    @SerializedName("right_image_path")
    @Expose
    private String rightImagePath;
    @SerializedName("wrong_image_path1")
    @Expose
    private String wrongImagePath1;
    @SerializedName("wrong_image_path2")
    @Expose
    private String wrongImagePath2;
    @SerializedName("wrong_image_path3")
    @Expose
    private String wrongImagePath3;

    @SerializedName("audio_path")
    @Expose
    private String audioPath;
    @SerializedName("ans_word")
    @Expose
    private String ansWord;
    @SerializedName("qtype_image_path")
    @Expose
    private String qtypeImagePath;
    @SerializedName("ans_audio_path")
    @Expose
    private String ansAudioPath;
    @SerializedName("q_type")
    @Expose
    private String qType;
    @SerializedName("use_ref_lang")
    @Expose
    private String useRefLang;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQno() {
        return qno;
    }

    public void setQno(String qno) {
        this.qno = qno;
    }

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

    public String getRightImagePath() {
        return rightImagePath;
    }

    public void setRightImagePath(String rightImagePath) {
        this.rightImagePath = rightImagePath;
    }

    public String getWrongImagePath1() {
        return wrongImagePath1;
    }

    public void setWrongImagePath1(String wrongImagePath1) {
        this.wrongImagePath1 = wrongImagePath1;
    }
public String getWrongImagePath2() {
        return wrongImagePath2;
    }

    public void setWrongImagePath2(String wrongImagePath2) {
        this.wrongImagePath2 = wrongImagePath2;
    }
public String getWrongImagePath3() {
        return wrongImagePath3;
    }

    public void setWrongImagePath3(String wrongImagePath3) {
        this.wrongImagePath3 = wrongImagePath3;
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

    public String getQtypeImagePath() {
        return qtypeImagePath;
    }

    public void setQtypeImagePath(String qtypeImagePath) {
        this.qtypeImagePath = qtypeImagePath;
    }

    public String getAnsAudioPath() {
        return ansAudioPath;
    }

    public void setAnsAudioPath(String ansAudioPath) {
        this.ansAudioPath = ansAudioPath;
    }

    public String getQType() {
        return qType;
    }

    public void setQType(String qType) {
        this.qType = qType;
    }

    public String getUseRefLang() {
        return useRefLang;
    }

    public void setUseRefLang(String useRefLang) {
        this.useRefLang = useRefLang;
    }
}
