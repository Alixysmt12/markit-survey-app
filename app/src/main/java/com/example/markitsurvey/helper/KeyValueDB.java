package com.example.markitsurvey.helper;

import android.content.SharedPreferences;

import com.example.markitsurvey.logger.AppLogger;
import com.example.markitsurvey.models.ProjectModel;

import java.util.List;

public class KeyValueDB {

    SharedPreferences sharedPref;

    public KeyValueDB(SharedPreferences sharedPreferences) {
        sharedPref = sharedPreferences;
    }

    public void save(String key, String value) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(String key, String defaultValue) {

        return sharedPref.getString(key, defaultValue);
    }

    public void  clear() {
        sharedPref.edit().clear().commit();

    }

    public void clearValue(String key) {
        AppLogger.i("e.key Remove",key);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.apply();
    }

}
