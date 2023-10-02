package com.example.markitsurvey.models;

public class MyProjectModel {
    int image;
    String name;
    String date;
    String createdBy;

    public MyProjectModel(int image) {
        this.image = image;
    }

    /*    public MyProjectModel(int image, String name, String date, String createdBy) {
        this.image = image;
        this.name = name;
        this.date = date;
        this.createdBy = createdBy;
    }*/

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
