package com.example.markitsurvey.models;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class KML {

    private String regionName;
    private List<BoundariesSet> BoundariesSet;

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public List<BoundariesSet> getBoundariesSet() {
        return BoundariesSet;
    }

    public void setBoundariesSet(List<BoundariesSet> boundariesSet) {
        BoundariesSet = boundariesSet;
    }


    public static KML getKML(String json)
    {
        KML kml = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            kml = new Gson().fromJson(json,KML.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return kml;
    }
}
