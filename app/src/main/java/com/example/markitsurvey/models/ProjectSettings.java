package com.example.markitsurvey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProjectSettings implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("QAParameters")
    @Expose
    private List<Object> qAParameters = null;
    @SerializedName("QAThreshold")
    @Expose
    private Integer qAThreshold;
    @SerializedName("projectQAPercentage")
    @Expose
    private Integer projectQAPercentage;
    @SerializedName("batchPassingPercentage")
    @Expose
    private Integer batchPassingPercentage;
    @SerializedName("sampleGenerationPercentage")
    @Expose
    private Integer sampleGenerationPercentage;
    @SerializedName("isAutoSave")
    @Expose
    private Boolean isAutoSave;
    @SerializedName("locationEnabled")
    @Expose
    private Boolean locationEnabled;
    @SerializedName("locationAccuracy")
    @Expose
    private Integer locationAccuracy;
    @SerializedName("retailAuditAppFlow")
    @Expose
    private Object retailAuditAppFlow;
    @SerializedName("metaData")
    @Expose
    private Object metaData;
    @SerializedName("retailAuditAppHandling")
    @Expose
    private Object retailAuditAppHandling;
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

    public List<Object> getQAParameters() {
        return qAParameters;
    }

    public void setQAParameters(List<Object> qAParameters) {
        this.qAParameters = qAParameters;
    }

    public Integer getQAThreshold() {
        return qAThreshold;
    }

    public void setQAThreshold(Integer qAThreshold) {
        this.qAThreshold = qAThreshold;
    }

    public Integer getProjectQAPercentage() {
        return projectQAPercentage;
    }

    public void setProjectQAPercentage(Integer projectQAPercentage) {
        this.projectQAPercentage = projectQAPercentage;
    }

    public Integer getBatchPassingPercentage() {
        return batchPassingPercentage;
    }

    public void setBatchPassingPercentage(Integer batchPassingPercentage) {
        this.batchPassingPercentage = batchPassingPercentage;
    }

    public Integer getSampleGenerationPercentage() {
        return sampleGenerationPercentage;
    }

    public void setSampleGenerationPercentage(Integer sampleGenerationPercentage) {
        this.sampleGenerationPercentage = sampleGenerationPercentage;
    }

    public Boolean getIsAutoSave() {
        return isAutoSave;
    }

    public void setIsAutoSave(Boolean isAutoSave) {
        this.isAutoSave = isAutoSave;
    }

    public Boolean getLocationEnabled() {
        return locationEnabled;
    }

    public void setLocationEnabled(Boolean locationEnabled) {
        this.locationEnabled = locationEnabled;
    }

    public Integer getLocationAccuracy() {
        return locationAccuracy;
    }

    public void setLocationAccuracy(Integer locationAccuracy) {
        this.locationAccuracy = locationAccuracy;
    }

    public Object getRetailAuditAppFlow() {
        return retailAuditAppFlow;
    }

    public void setRetailAuditAppFlow(Object retailAuditAppFlow) {
        this.retailAuditAppFlow = retailAuditAppFlow;
    }

    public Object getMetaData() {
        return metaData;
    }

    public void setMetaData(Object metaData) {
        this.metaData = metaData;
    }

    public Object getRetailAuditAppHandling() {
        return retailAuditAppHandling;
    }

    public void setRetailAuditAppHandling(Object retailAuditAppHandling) {
        this.retailAuditAppHandling = retailAuditAppHandling;
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

}
