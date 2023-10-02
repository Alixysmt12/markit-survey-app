package com.example.markitsurvey.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GPSPoint {

    private double lat;
    private double lng;
    private String updatedTime;
    private int userId;
    private double accuracy;

    public GPSPoint(double lat,double lng,String updatedTime,double accuracy)
    {
        setLat(lat);
        setLng(lng);
        setUpdatedTime(updatedTime);
        setAccuracy(accuracy);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }


    public static ArrayList<GPSPoint> ConvertToEntity(String json)
    {
        ArrayList<GPSPoint> gpsPoints = null;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<GPSPoint>>() {
            }.getType();
            gpsPoints = gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gpsPoints;

    }
}
