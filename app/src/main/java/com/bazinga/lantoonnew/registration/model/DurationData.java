package com.bazinga.lantoonnew.registration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DurationData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("durationMin")
    @Expose
    private String durationMin;
    @SerializedName("caption")
    @Expose
    private String caption;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(String durationMin) {
        this.durationMin = durationMin;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
