package com.example.markitsurvey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubArea implements Serializable {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cordinates")
    @Expose
    private String cordinates;
    @SerializedName("filePath")
    @Expose
    private String filePath;
    @SerializedName("companyId")
    @Expose
    private Integer companyId;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("areaName")
    @Expose
    private String areaName;
    @SerializedName("primaryPopulation")
    @Expose
    private Integer primaryPopulation;
    @SerializedName("secondaryPopulation")
    @Expose
    private Integer secondaryPopulation;
    @SerializedName("primaryHousehold")
    @Expose
    private Integer primaryHousehold;
    @SerializedName("secondaryHousehold")
    @Expose
    private Integer secondaryHousehold;
    @SerializedName("areaLatLng")
    @Expose
    private String areaLatLng;
    @SerializedName("MapImage")
    @Expose
    private Object mapImage;
    @SerializedName("countryId")
    @Expose
    private Integer countryId;
    @SerializedName("stateId")
    @Expose
    private Integer stateId;
    @SerializedName("cityId")
    @Expose
    private Integer cityId;
    @SerializedName("superAreaId")
    @Expose
    private Integer superAreaId;
    @SerializedName("areaId")
    @Expose
    private Integer areaId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCordinates() {
        return cordinates;
    }

    public void setCordinates(String cordinates) {
        this.cordinates = cordinates;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getPrimaryPopulation() {
        return primaryPopulation;
    }

    public void setPrimaryPopulation(Integer primaryPopulation) {
        this.primaryPopulation = primaryPopulation;
    }

    public Integer getSecondaryPopulation() {
        return secondaryPopulation;
    }

    public void setSecondaryPopulation(Integer secondaryPopulation) {
        this.secondaryPopulation = secondaryPopulation;
    }

    public Integer getPrimaryHousehold() {
        return primaryHousehold;
    }

    public void setPrimaryHousehold(Integer primaryHousehold) {
        this.primaryHousehold = primaryHousehold;
    }

    public Integer getSecondaryHousehold() {
        return secondaryHousehold;
    }

    public void setSecondaryHousehold(Integer secondaryHousehold) {
        this.secondaryHousehold = secondaryHousehold;
    }

    public String getAreaLatLng() {
        return areaLatLng;
    }

    public void setAreaLatLng(String areaLatLng) {
        this.areaLatLng = areaLatLng;
    }

    public Object getMapImage() {
        return mapImage;
    }

    public void setMapImage(Object mapImage) {
        this.mapImage = mapImage;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getSuperAreaId() {
        return superAreaId;
    }

    public void setSuperAreaId(Integer superAreaId) {
        this.superAreaId = superAreaId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

}
