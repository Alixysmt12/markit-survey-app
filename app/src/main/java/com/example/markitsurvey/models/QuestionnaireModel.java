package com.example.markitsurvey.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;

public class QuestionnaireModel implements Serializable {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private DataQue data;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("error")
    @Expose
    private Object error;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public DataQue getData() {
        return data;
    }

    public void setData(DataQue data) {
        this.data = data;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public static QuestionnaireModel convertToQuestionnaireVersion(String json)
    {
        QuestionnaireModel questionnaireVersion = null;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<QuestionnaireModel>() {
            }.getType();
            questionnaireVersion = gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return questionnaireVersion;

    }

}

