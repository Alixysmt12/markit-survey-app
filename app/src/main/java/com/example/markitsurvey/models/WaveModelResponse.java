package com.example.markitsurvey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WaveModelResponse implements Serializable {

    @SerializedName("projectWaveSettings")
    @Expose
    private List<ProjectWaveSetting> projectWaveSettings = null;

    public List<ProjectWaveSetting> getProjectWaveSettings() {
        return projectWaveSettings;
    }

    public void setProjectWaveSettings(List<ProjectWaveSetting> projectWaveSettings) {
        this.projectWaveSettings = projectWaveSettings;
    }
}
