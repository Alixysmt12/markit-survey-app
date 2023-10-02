package com.example.markitsurvey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplicationLoginLogIdModel {

    @SerializedName("applicationLoginLogId")
    @Expose
    private String applicationLoginLogId;

    public String getApplicationLoginLogId() {
        return applicationLoginLogId;
    }

    public void setApplicationLoginLogId(String applicationLoginLogId) {
        this.applicationLoginLogId = applicationLoginLogId;
    }

}
