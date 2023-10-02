package com.example.markitsurvey.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Region implements Serializable {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("reportingStatus")
    @Expose
    private Boolean reportingStatus;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("projectId")
    @Expose
    private Integer projectId;
    @SerializedName("assignedBy")
    @Expose
    private Object assignedBy;
    @SerializedName("reportingTo")
    @Expose
    private Object reportingTo;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("subAreaId")
    @Expose
    private Integer subAreaId;
    @SerializedName("subArea")
    @Expose
    private SubArea subArea;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getReportingStatus() {
        return reportingStatus;
    }

    public void setReportingStatus(Boolean reportingStatus) {
        this.reportingStatus = reportingStatus;
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

    public Object getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(Object assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Object getReportingTo() {
        return reportingTo;
    }

    public void setReportingTo(Object reportingTo) {
        this.reportingTo = reportingTo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSubAreaId() {
        return subAreaId;
    }

    public void setSubAreaId(Integer subAreaId) {
        this.subAreaId = subAreaId;
    }

    public SubArea getSubArea() {
        return subArea;
    }

    public void setSubArea(SubArea subArea) {
        this.subArea = subArea;
    }


    public static Region ConvertToRegionEntity(String json)
    {
        Region region = null;
        try {
            Gson gson = new Gson();

            region = gson.fromJson(json, Region.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return region;

    }


    public static ArrayList<Region> ConvertToRegions(String json)
    {
        ArrayList<Region> regions = null;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Region>>() {
            }.getType();
            regions = gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return regions;

    }

}
