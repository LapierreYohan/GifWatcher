package com.example.phonenetworks.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Record implements Serializable {
    @SerializedName("fields")
    @Expose
    private Fields fields;
    @SerializedName("record_timestamp")
    @Expose
    private String recordTimestamp;

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public String getRecordTimestamp() {
        return recordTimestamp;
    }

    public void setRecordTimestamp(String recordTimestamp) {
        this.recordTimestamp = recordTimestamp;
    }
}
