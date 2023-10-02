package com.example.markitsurvey.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class QAStatusByUserIdModel implements Serializable {

    @SerializedName("QAStatusByUserId")
    @Expose
    private List<QAStatusByUserId> qAStatusByUserId = null;

    public List<QAStatusByUserId> getQAStatusByUserId() {
        return qAStatusByUserId;
    }

    public void setQAStatusByUserId(List<QAStatusByUserId> qAStatusByUserId) {
        this.qAStatusByUserId = qAStatusByUserId;
    }


}
