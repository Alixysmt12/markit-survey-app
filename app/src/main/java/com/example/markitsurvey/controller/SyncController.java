package com.example.markitsurvey.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.markitsurvey.api.Api;
import com.example.markitsurvey.logger.AppLogger;
import com.example.markitsurvey.models.SyncResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SyncController {

    public Context context;

    public SyncController(Context context) {
        this.context = context;
    }


    public void syncQuestionnaire(String parameters){

            Api.getClient().syncSimpleSurveyData(parameters)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<SyncResponse>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull SyncResponse syncResponse) {

                            sendBroadCast_sync_data(syncResponse);

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            if (e instanceof IOException) {
                                if (context != null) {
                                    Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    AppLogger.i("Exception", e.getMessage());
                                }
                            } else {
                                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                AppLogger.i("Exception", e.getLocalizedMessage());
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        }



    private void sendBroadCast_sync_data(SyncResponse syncResponse) {

        Intent localIntent = new Intent("my.own.do_app_sync_data_user_broadcast");
        localIntent.putExtra("result", (Serializable) syncResponse);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }
}
