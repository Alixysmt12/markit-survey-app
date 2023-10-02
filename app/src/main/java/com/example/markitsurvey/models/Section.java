package com.example.markitsurvey.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Section implements Serializable {


    private int id;
    private String sectionName;
    private String sectionDescription;
    private String sequence;
    private String createdAt;
    private String updatedAt;
    private int projectId;
    private Questionnaire questionnaire;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionDescription() {
        return sectionDescription;
    }

    public void setSectionDescription(String sectionDescription) {
        this.sectionDescription = sectionDescription;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
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

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }


    public static ArrayList<Section> ConvertToProjectsEntity(String json) {
        ArrayList<Section> sections = null;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Section>>() {
            }.getType();
            sections = gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sections;

    }
}
