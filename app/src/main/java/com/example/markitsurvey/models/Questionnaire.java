package com.example.markitsurvey.models;

import java.io.Serializable;

public class Questionnaire implements Serializable {


    private int id;
    private String questionnaireJSON;
    private String createdAt;
    private String updatedAt;
    private int sectionId;
    private String version;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionnaireJSON() {
        return questionnaireJSON;
    }

    public void setQuestionnaireJSON(String questionnaireJSON) {
        this.questionnaireJSON = questionnaireJSON;
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

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
