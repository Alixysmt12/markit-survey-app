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
import com.example.markitsurvey.api.Api;
import com.example.markitsurvey.logger.AppLogger;
import com.example.markitsurvey.models.AppVersion;
import com.example.markitsurvey.models.ApplicationDeviceModel;
import com.example.markitsurvey.models.ApplicationDeviceResponse;
import com.example.markitsurvey.models.ApplicationLoginLogIdModel;
import com.example.markitsurvey.models.ApplicationLoginLogIdResponse;
import com.example.markitsurvey.models.ApplicationLoginLogModel;
import com.example.markitsurvey.models.ApplicationLoginLogResponse;
import com.example.markitsurvey.models.ServerDateTime;
import com.example.markitsurvey.models.UserLoginModel;
import com.example.markitsurvey.models.UserModelsResponse;

import java.io.IOException;
import java.io.Serializable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginController {

    public Context context;

    public LoginController(Context context) {
        this.context = context;
    }


    public void PostApplicationDetailed(String syncDataId, String manufacturer, String model, String brand, String board,
                                        String hardware, String serialNo, String deviceId,
                                        String androidId, String screenResolution, String screenSize,
                                        String screenDensity, String bootLoader,
                                        String user, String host, String version, String APILevel, String buildID,
                                        String buildTime, String externalMemoryIsAvailable, String externalMemory,
                                        String internalMemory, String RAMInfo, String versionRelease) {

        ApplicationDeviceModel applicationDeviceModel = new ApplicationDeviceModel();

        applicationDeviceModel.setSyncDataId(syncDataId);
        applicationDeviceModel.setManufacturer(manufacturer);
        applicationDeviceModel.setModel(model);
        applicationDeviceModel.setBrand(brand);
        applicationDeviceModel.setBoard(board);
        applicationDeviceModel.setHardware(hardware);
        applicationDeviceModel.setSerialNo(serialNo);
        applicationDeviceModel.setDeviceId(deviceId);
        applicationDeviceModel.setAndroidId(androidId);
        applicationDeviceModel.setScreenResolution(screenResolution);
        applicationDeviceModel.setScreenSize(screenSize);
        applicationDeviceModel.setScreenDensity(screenDensity);
        applicationDeviceModel.setBootLoader(bootLoader);
        applicationDeviceModel.setUser(user);
        applicationDeviceModel.setHost(host);
        applicationDeviceModel.setVersion(version);
        applicationDeviceModel.setAPILevel(APILevel);
        applicationDeviceModel.setBuildID(buildID);
        applicationDeviceModel.setBuildTime(buildTime);
        applicationDeviceModel.setExternalMemoryIsAvailable(externalMemoryIsAvailable);
        applicationDeviceModel.setExternalMemory(externalMemory);
        applicationDeviceModel.setInternalMemory(internalMemory);
        applicationDeviceModel.setRAMInfo(RAMInfo);
        applicationDeviceModel.setVersionRelease(versionRelease);


        Api.getClient().postApplicationDeviceDetailed(applicationDeviceModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ApplicationDeviceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ApplicationDeviceResponse applicationDeviceResponse) {

                        sendBroadCast_post_device_detailed(applicationDeviceResponse);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {


                        if (e instanceof IOException) {
                            if (context != null) {
                                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

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


    public void PostApplicationLoginLog(String applicationId, String applicationVersionId, Integer loginUser, Integer lat, Integer lon, String address) {

        ApplicationLoginLogModel applicationLoginLogModel = new ApplicationLoginLogModel();
        applicationLoginLogModel.setApplicationId(applicationId);
        applicationLoginLogModel.setApplicationVersionId(applicationVersionId);
        applicationLoginLogModel.setLoginUser(loginUser);
        applicationLoginLogModel.setLat(lat);
        applicationLoginLogModel.setLng(lon);
        applicationLoginLogModel.setAddress(address);

        Api.getClient().postApplicationLoginLog(applicationLoginLogModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ApplicationLoginLogResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ApplicationLoginLogResponse applicationLoginLogResponse) {

                        sendBroadCast_post_application_login_log(applicationLoginLogResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {


                        if (e instanceof IOException) {
                            if (context != null) {
                                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

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

    public void PostApplicationLoginLogId(String applicationLoginLogId) {

        ApplicationLoginLogIdModel applicationLoginLogIdModel = new ApplicationLoginLogIdModel();
        applicationLoginLogIdModel.setApplicationLoginLogId(applicationLoginLogId);

        Api.getClient().postApplicationLoginLogId(applicationLoginLogIdModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ApplicationLoginLogIdResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ApplicationLoginLogIdResponse applicationLoginLogIdResponse) {

                        sendBroadCast_post_application_login_log_id(applicationLoginLogIdResponse);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if (e instanceof IOException) {
                            if (context != null) {

                                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

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

    public void CheckDateTime() {

        Api.getClient().checkDate()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ServerDateTime>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {


                    }

                    @Override
                    public void onNext(@NonNull ServerDateTime serverDateTime) {

                        sendBroadCast_check_date(serverDateTime);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if (e instanceof IOException) {
                            if (context != null) {
                                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

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

    public void userLogin(String name, String password, String token) {

        UserLoginModel loginResponseModel = new UserLoginModel();
        loginResponseModel.setUserName(name);
        loginResponseModel.setPassword(password);
        loginResponseModel.setKey(token);

        Api.getClient().doLogin(loginResponseModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UserModelsResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull UserModelsResponse userModelsResponse) {
                        sendBroadCast_do_login(userModelsResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if (e instanceof IOException) {
                            if (context != null) {
                                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

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

    public void checkVersion(String versionNumber) {

        Api.getClient().checkVersion(versionNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<AppVersion>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull AppVersion appVersion) {
                        sendBroadCast_check_version(appVersion);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if (e instanceof IOException) {
                            if (context != null) {
                                AppLogger.i("Exception", e.getMessage());
                                Toast.makeText(context, "checkVersion " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void sendBroadCast_do_login(UserModelsResponse result) {
        Intent localIntent = new Intent("my.own.do_login_broadcast");
        localIntent.putExtra("result", (Serializable) result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }


    private void sendBroadCast_check_version(AppVersion result) {
        Intent localIntent = new Intent("my.own.do_app_version_broadcast");
        localIntent.putExtra("appVersion", (Serializable) result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }


    private void sendBroadCast_check_date(ServerDateTime result) {
        Intent localIntent = new Intent("my.own.do_app_date_broadcast");
        localIntent.putExtra("date", (Serializable) result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }

    private void sendBroadCast_post_device_detailed(ApplicationDeviceResponse result) {
        Intent localIntent = new Intent("my.own.do_app_detailed_broadcast");
        localIntent.putExtra("detailed", (Serializable) result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }

    private void sendBroadCast_post_application_login_log(ApplicationLoginLogResponse result) {
        Intent localIntent = new Intent("my.own.do_app_login_log_broadcast");
        localIntent.putExtra("logResponse", (Serializable) result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }

    private void sendBroadCast_post_application_login_log_id(ApplicationLoginLogIdResponse result) {
        Intent localIntent = new Intent("my.own.do_app_login_log_id_broadcast");
        localIntent.putExtra("logResponseId", (Serializable) result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }


}
