package com.example.markitsurvey.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProjectModel implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("projectName")
    @Expose
    private String projectName;
    @SerializedName("projectDescription")
    @Expose
    private String projectDescription;
    @SerializedName("startingDate")
    @Expose
    private String startingDate;
    @SerializedName("endingDate")
    @Expose
    private String endingDate;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("projectTypeId")
    @Expose
    private Integer projectTypeId;
    @SerializedName("studyType")
    @Expose
    private String studyType;
    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;
    @SerializedName("projectClassificationTypeId")
    @Expose
    private Integer projectClassificationTypeId;
    @SerializedName("projectClassificationType")
    @Expose
    private ProjectClassificationType projectClassificationType;
    @SerializedName("users")
    @Expose
    private List<User> users = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
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

    public Integer getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(Integer projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getProjectClassificationTypeId() {
        return projectClassificationTypeId;
    }

    public void setProjectClassificationTypeId(Integer projectClassificationTypeId) {
        this.projectClassificationTypeId = projectClassificationTypeId;
    }

    public ProjectClassificationType getProjectClassificationType() {
        return projectClassificationType;
    }

    public void setProjectClassificationType(ProjectClassificationType projectClassificationType) {
        this.projectClassificationType = projectClassificationType;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    public static ArrayList<ProjectModel> ConvertToProjectsEntity(String json)
    {
        ArrayList<ProjectModel> projects = null;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ProjectModel>>() {
            }.getType();
            projects = gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return projects;

    }

    public static ProjectModel getProjectById(String json,int projectId) {
        List<ProjectModel> projectList = ProjectModel.ConvertToProjectsEntity(json);
        ProjectModel project = null;
        for (ProjectModel item : projectList) {
            if (item.getId() == projectId) {
                project = item;
                break;
            }
        }
        return project;
    }

}
