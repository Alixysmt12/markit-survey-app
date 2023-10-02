package com.example.markitsurvey.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ProjectWaveSetting implements Serializable {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("waveStartDate")
    @Expose
    private String waveStartDate;
    @SerializedName("waveEndDate")
    @Expose
    private String waveEndDate;
    @SerializedName("waveName")
    @Expose
    private String waveName;
    @SerializedName("activeWave")
    @Expose
    private Boolean activeWave;
    @SerializedName("currentWave")
    @Expose
    private Boolean currentWave;
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

    public String getWaveStartDate() {
        return waveStartDate;
    }

    public void setWaveStartDate(String waveStartDate) {
        this.waveStartDate = waveStartDate;
    }

    public String getWaveEndDate() {
        return waveEndDate;
    }

    public void setWaveEndDate(String waveEndDate) {
        this.waveEndDate = waveEndDate;
    }

    public String getWaveName() {
        return waveName;
    }

    public void setWaveName(String waveName) {
        this.waveName = waveName;
    }

    public Boolean getActiveWave() {
        return activeWave;
    }

    public void setActiveWave(Boolean activeWave) {
        this.activeWave = activeWave;
    }

    public Boolean getCurrentWave() {
        return currentWave;
    }

    public void setCurrentWave(Boolean currentWave) {
        this.currentWave = currentWave;
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


    public static ArrayList<ProjectWaveSetting> ConvertToWavesList(String json)
    {
        ArrayList<ProjectWaveSetting> waveArrayList = null;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ProjectWaveSetting>>() {
            }.getType();
            waveArrayList = gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return waveArrayList;

    }

    public static ProjectWaveSetting getCurrentWave(String json)
    {
        ProjectWaveSetting wave = null;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ProjectWaveSetting>() {
            }.getType();
            wave = gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return wave;

    }

}
