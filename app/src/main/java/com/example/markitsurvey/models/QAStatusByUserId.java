package com.example.markitsurvey.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;

public class QAStatusByUserId implements Serializable {

    @SerializedName("Total")
    @Expose
    private String total;
    @SerializedName("Pending")
    @Expose
    private String pending;
    @SerializedName("Pass")
    @Expose
    private String pass;
    @SerializedName("Fail")
    @Expose
    private String fail;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFail() {
        return fail;
    }

    public void setFail(String fail) {
        this.fail = fail;
    }


    public static QAStatusByUserId ConvertToBaseRetailAuditAppFlow(String json) {
        QAStatusByUserId baseQAStatus = null;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<QAStatusByUserId>() {
            }.getType();
            baseQAStatus = gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseQAStatus;
    }
}
