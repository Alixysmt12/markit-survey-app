package com.example.markitsurvey.api;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Api  extends AppCompatActivity {

    public static final String API_URL = "http://54.179.225.22:6050/api/v1/";
    public static final String S3SiteUrl = "http://52.221.243.180:6100/api/v1/";
    public static final String R3baseURL = "http://3.1.169.122:6660/";

    public static ApiClient getClient() {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().retryOnConnectionFailure(true).addInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                .build().create(ApiClient.class);

    }
}
