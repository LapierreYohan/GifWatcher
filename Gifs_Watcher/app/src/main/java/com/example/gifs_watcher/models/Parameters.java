package com.example.phonenetworks.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Parameters implements Serializable {

    @SerializedName("dataset")
    @Expose
    private String dataset;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("timezone")
    @Expose
    private String timezone;

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

}
