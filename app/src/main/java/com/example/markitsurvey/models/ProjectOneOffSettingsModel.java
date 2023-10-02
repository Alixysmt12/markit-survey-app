package com.example.markitsurvey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProjectOneOffSettingsModel implements Serializable {

    @SerializedName("projectOneOffSettings")
    @Expose
    private ProjectOneOffSettings projectOneOffSettings;

    public ProjectOneOffSettings getProjectOneOffSettings() {
        return projectOneOffSettings;
    }

    public void setProjectOneOffSettings(ProjectOneOffSettings projectOneOffSettings) {
        this.projectOneOffSettings = projectOneOffSettings;
    }
}
