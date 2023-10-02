package com.example.markitsurvey.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.markitsurvey.R;
import com.example.markitsurvey.activities.ProjectDetailedActivity;
import com.example.markitsurvey.api.Api;
import com.example.markitsurvey.logger.AppLogger;
import com.example.markitsurvey.models.ProjectMetaDataModel;
import com.example.markitsurvey.models.ProjectOneOffSettingsModel;
import com.example.markitsurvey.models.ProjectSettingsModels;
import com.example.markitsurvey.models.QAStatusByUserIdModel;
import com.example.markitsurvey.models.QuestionnaireModel;
import com.example.markitsurvey.models.RegionModel;
import com.example.markitsurvey.models.WaveModelResponse;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Serializable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailedProjectController {

    public Context context;

    public DetailedProjectController(Context context) {
        this.context = context;
    }


    public void getProjectOneOffSettings(Integer projectId) {

        Api.getClient().getOneOffSetting(projectId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ProjectOneOffSettingsModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProjectOneOffSettingsModel projectOneOffSettingsModel) {


                        sendBroadCast_get_one_off(projectOneOffSettingsModel);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if (e instanceof IOException) {
                            if (context != null) {
                                Toast.makeText(context, "getProjectOneOffSettings " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                AppLogger.i("Exception", e.getMessage());
                                LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                                lottieAnimationView.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            AppLogger.i("Exception", e.getLocalizedMessage());
                            LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                            lottieAnimationView.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void getWave(Integer projectId) {
        Api.getClient().getWave(projectId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<WaveModelResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull WaveModelResponse waveModelResponse) {

                        sendBroadCast_get_wave(waveModelResponse);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {


                        if (e instanceof IOException) {
                            if (context != null) {
                                AppLogger.i("Exception", e.getMessage());
                                Toast.makeText(context, "getWave " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                                lottieAnimationView.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            AppLogger.i("Exception", e.getLocalizedMessage());

                            LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                            lottieAnimationView.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getQAStatusPassFail(Integer userId, Integer projectId, String wave) {

        Api.getClient().getCustomizedResearchQAStatusByUserId(userId, projectId, wave)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<QAStatusByUserIdModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull QAStatusByUserIdModel qaStatusByUserIdModel) {

                        endBroadCast_get_xpass_fail(qaStatusByUserIdModel);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if (e instanceof IOException) {
                            if (context != null) {
                                AppLogger.i("Exception", e.getMessage());
                                Toast.makeText(context, "getQAStatusPassFail " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                                lottieAnimationView.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            AppLogger.i("Exception", e.getLocalizedMessage());
                            LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                            lottieAnimationView.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getQuestionnaire(Integer userId, Integer projectId) {
        Api.getClient().getQuestionnaire(userId, projectId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonObject) {

                        sendBroadCast_get_ques(jsonObject);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if (e instanceof IOException) {
                            if (context != null) {
                                AppLogger.i("Exception", e.getMessage());
                                Toast.makeText(context, "getQuestionnaire " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                                lottieAnimationView.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            AppLogger.i("Exception", e.getLocalizedMessage());
                            LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                            lottieAnimationView.setVisibility(View.GONE);
                        }

                    }


                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getQuestionnaireVersion(Integer projectId) {

        Api.getClient().getQuestionnaireVersion(projectId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<QuestionnaireModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull QuestionnaireModel questionnaireModel) {

                        sendBroadCast_get_quest_id(questionnaireModel);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if (e instanceof IOException) {
                            if (context != null) {
                                AppLogger.i("Exception", e.getMessage());
                                Toast.makeText(context, "getQuestionnaireVersion " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                                lottieAnimationView.setVisibility(View.GONE);
                            }
                        } else {
                            AppLogger.i("Exception", e.getLocalizedMessage());
                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                            lottieAnimationView.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getRegion(Integer userId, Integer projectId) {
        Api.getClient().getRegions(userId, projectId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<RegionModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull RegionModel regionModel) {
                        sendBroadCast_get_region(regionModel);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {


                        if (e instanceof IOException) {
                            if (context != null) {
                                AppLogger.i("Exception", e.getMessage());
                                Toast.makeText(context, "getRegion " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                                lottieAnimationView.setVisibility(View.GONE);
                            }
                        } else {
                            AppLogger.i("Exception", e.getLocalizedMessage());
                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                            lottieAnimationView.setVisibility(View.GONE);
                        }

                    }


                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getProjectSettingByProjectId(Integer projectId){
        Api.getClient().getProjectSettingByProjectId(projectId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ProjectSettingsModels>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProjectSettingsModels projectSettingsModels) {

                        endBroadCast_get_project_setting_by_project_id_fail(projectSettingsModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {


                        if (e instanceof IOException) {
                            if (context != null) {
                                AppLogger.i("Exception", e.getMessage());
                                Toast.makeText(context, "getProjectSettingByProjectId" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                                lottieAnimationView.setVisibility(View.GONE);
                            }
                        } else {
                            AppLogger.i("Exception", e.getLocalizedMessage());
                           // Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                            lottieAnimationView.setVisibility(View.GONE);


                        }

                        sendBroadCast_exception(e.getLocalizedMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getProjectMetaData(Integer projectId) {
        Api.getClient().getMetaData(projectId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ProjectMetaDataModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProjectMetaDataModel projectMetaDataModel) {
                        sendBroadCast_get_meta_data(projectMetaDataModel);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if (e instanceof IOException) {
                            if (context != null) {
                                AppLogger.i("Exception", e.getMessage());
                                Toast.makeText(context, "Meta Data Not Define " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "Meta Data Not Define", Toast.LENGTH_SHORT).show();
                                LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                                lottieAnimationView.setVisibility(View.GONE);
                            }
                        } else {
                            AppLogger.i("Exception", e.getLocalizedMessage());
                            //Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            LottieAnimationView lottieAnimationView = (LottieAnimationView) ((Activity) context).findViewById(R.id.animation_logo);
                            lottieAnimationView.setVisibility(View.GONE);

                            sendBroadCast_exception_meta_data(e.getLocalizedMessage());

                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void sendBroadCast_exception(String localizedMessage) {

        Intent localIntent = new Intent("my.own.do_get_project_setting_by_project_id_broadcast");
        localIntent.putExtra("resultError", localizedMessage);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }


    private void sendBroadCast_exception_meta_data(String localizedMessage) {

        Intent localIntent = new Intent("my.own.do_get_meta_data_broadcast");
        localIntent.putExtra("resultError", localizedMessage);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }

    private void sendBroadCast_get_meta_data(ProjectMetaDataModel result) {
        Intent localIntent = new Intent("my.own.do_get_meta_data_broadcast");
        localIntent.putExtra("result", (Serializable) result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);

    }


    private void sendBroadCast_get_quest_id(QuestionnaireModel result) {
        Intent localIntent = new Intent("my.own.do_get_que_broadcast");
        localIntent.putExtra("result", (Serializable) result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }

    private void sendBroadCast_get_region(RegionModel result) {
        Intent localIntent = new Intent("my.own.do_get_region_broadcast");
        localIntent.putExtra("result", (Serializable) result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }

    private void sendBroadCast_get_ques(JsonObject result) {
        Intent localIntent = new Intent("my.own.do_get_guestionnaire_broadcast");
        localIntent.putExtra("result", String.valueOf(result));
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }

    private void sendBroadCast_get_wave(WaveModelResponse result) {
        Intent localIntent = new Intent("my.own.do_get_wave_broadcast");
        localIntent.putExtra("result", (Serializable) result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }


    private void sendBroadCast_get_one_off(ProjectOneOffSettingsModel result) {
        Intent localIntent = new Intent("my.own.do_get_one_off_broadcast");
        localIntent.putExtra("result", (Serializable) result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }

    private void endBroadCast_get_xpass_fail(QAStatusByUserIdModel result) {
        Intent localIntent = new Intent("my.own.do_get_pass_fail_broadcast");
        localIntent.putExtra("result", (Serializable) result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);

    }


    private void endBroadCast_get_project_setting_by_project_id_fail(ProjectSettingsModels result) {
        Intent localIntent = new Intent("my.own.do_get_project_setting_by_project_id_broadcast");
        localIntent.putExtra("result", (Serializable) result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);

    }




}
