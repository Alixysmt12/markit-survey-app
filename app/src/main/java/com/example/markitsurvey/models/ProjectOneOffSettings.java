package com.example.markitsurvey.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;

public class ProjectOneOffSettings implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("fieldStartDate")
    @Expose
    private String fieldStartDate;
    @SerializedName("fieldEndDate")
    @Expose
    private String fieldEndDate;
    @SerializedName("studyType")
    @Expose
    private String studyType;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("projectId")
    @Expose
    private Integer projectId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldStartDate() {
        return fieldStartDate;
    }

    public void setFieldStartDate(String fieldStartDate) {
        this.fieldStartDate = fieldStartDate;
    }

    public String getFieldEndDate() {
        return fieldEndDate;
    }

    public void setFieldEndDate(String fieldEndDate) {
        this.fieldEndDate = fieldEndDate;
    }

    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }


    public static ProjectOneOffSettings getOneOfSettings(String json)
    {
        ProjectOneOffSettings oneOffSetting = null;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ProjectOneOffSettings>() {
            }.getType();
            oneOffSetting = gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return oneOffSetting;

    }


}


