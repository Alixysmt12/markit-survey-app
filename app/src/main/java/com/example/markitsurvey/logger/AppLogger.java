package com.example.markitsurvey.logger;

import com.bosphere.filelogger.FL;
import com.google.gson.JsonElement;

public class AppLogger {

    public static void i(String TAG, JsonElement json)
    {

        FL.i(TAG,json);
    }

    public static void i(String message)
    {

        FL.i(message);
    }

    public static void i(String TAG, String message, Object obj)
    {

        FL.i(TAG,message,obj);
    }
    public static void i(String TAG, String message)
    {

        FL.i(TAG,message);
    }

    public static void e(String TAG, String message)
    {

        FL.e(TAG,message);
    }
    public static void e(String message)
    {

        FL.e(message);
    }
}
