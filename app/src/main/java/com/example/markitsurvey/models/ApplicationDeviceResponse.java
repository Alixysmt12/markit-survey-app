package com.example.markitsurvey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApplicationDeviceResponse implements Serializable {



    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("manufacturer")
    @Expose
    private String manufacturer;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("board")
    @Expose
    private String board;
    @SerializedName("hardware")
    @Expose
    private String hardware;
    @SerializedName("serialNo")
    @Expose
    private String serialNo;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("androidId")
    @Expose
    private String androidId;
    @SerializedName("screenResolution")
    @Expose
    private String screenResolution;
    @SerializedName("screenSize")
    @Expose
    private String screenSize;
    @SerializedName("screenDensity")
    @Expose
    private String screenDensity;
    @SerializedName("bootLoader")
    @Expose
    private String bootLoader;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("host")
    @Expose
    private String host;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("APILevel")
    @Expose
    private String aPILevel;
    @SerializedName("buildID")
    @Expose
    private String buildID;
    @SerializedName("buildTime")
    @Expose
    private String buildTime;
    @SerializedName("externalMemoryIsAvailable")
    @Expose
    private String externalMemoryIsAvailable;
    @SerializedName("externalMemory")
    @Expose
    private String externalMemory;
    @SerializedName("internalMemory")
    @Expose
    private String internalMemory;
    @SerializedName("RAMInfo")
    @Expose
    private String rAMInfo;
    @SerializedName("versionRelease")
    @Expose
    private String versionRelease;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getScreenResolution() {
        return screenResolution;
    }

    public void setScreenResolution(String screenResolution) {
        this.screenResolution = screenResolution;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public String getScreenDensity() {
        return screenDensity;
    }

    public void setScreenDensity(String screenDensity) {
        this.screenDensity = screenDensity;
    }

    public String getBootLoader() {
        return bootLoader;
    }

    public void setBootLoader(String bootLoader) {
        this.bootLoader = bootLoader;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAPILevel() {
        return aPILevel;
    }

    public void setAPILevel(String aPILevel) {
        this.aPILevel = aPILevel;
    }

    public String getBuildID() {
        return buildID;
    }

    public void setBuildID(String buildID) {
        this.buildID = buildID;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public String getExternalMemoryIsAvailable() {
        return externalMemoryIsAvailable;
    }

    public void setExternalMemoryIsAvailable(String externalMemoryIsAvailable) {
        this.externalMemoryIsAvailable = externalMemoryIsAvailable;
    }

    public String getExternalMemory() {
        return externalMemory;
    }

    public void setExternalMemory(String externalMemory) {
        this.externalMemory = externalMemory;
    }

    public String getInternalMemory() {
        return internalMemory;
    }

    public void setInternalMemory(String internalMemory) {
        this.internalMemory = internalMemory;
    }

    public String getRAMInfo() {
        return rAMInfo;
    }

    public void setRAMInfo(String rAMInfo) {
        this.rAMInfo = rAMInfo;
    }

    public String getVersionRelease() {
        return versionRelease;
    }

    public void setVersionRelease(String versionRelease) {
        this.versionRelease = versionRelease;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
