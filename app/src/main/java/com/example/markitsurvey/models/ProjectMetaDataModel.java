package com.example.markitsurvey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProjectMetaDataModel implements Serializable {

    @SerializedName("projectMetaData")
    @Expose
    private List<ProjectMetaDatum> projectMetaData = null;

    public List<ProjectMetaDatum> getProjectMetaData() {
        return projectMetaData;
    }

    public void setProjectMetaData(List<ProjectMetaDatum> projectMetaData) {
        this.projectMetaData = projectMetaData;
    }
}
