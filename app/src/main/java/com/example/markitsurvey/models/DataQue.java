package com.example.markitsurvey.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;

public class DataQue implements Serializable {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("restore")
    @Expose
    private Object restore;
    @SerializedName("trackVersion")
    @Expose
    private Object trackVersion;
    @SerializedName("previousVersion")
    @Expose
    private String previousVersion;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("sectionId")
    @Expose
    private Integer sectionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Object getRestore() {
        return restore;
    }

    public void setRestore(Object restore) {
        this.restore = restore;
    }

    public Object getTrackVersion() {
        return trackVersion;
    }

    public void setTrackVersion(Object trackVersion) {
        this.trackVersion = trackVersion;
    }

    public String getPreviousVersion() {
        return previousVersion;
    }

    public void setPreviousVersion(String previousVersion) {
        this.previousVersion = previousVersion;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }


    public static DataQue convertToQuestionnaireVersion(String json)
    {
        DataQue questionnaireVersion = null;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<DataQue>() {
            }.getType();
            questionnaireVersion = gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return questionnaireVersion;

    }
}
