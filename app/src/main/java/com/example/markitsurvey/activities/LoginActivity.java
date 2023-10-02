package com.example.markitsurvey.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.bosphere.filelogger.FL;
import com.bosphere.filelogger.FLConfig;
import com.bosphere.filelogger.FLConst;
import com.example.markitsurvey.R;
import com.example.markitsurvey.backgroundService.FetchAddressService;
import com.example.markitsurvey.controller.LoginController;
import com.example.markitsurvey.databinding.ActivityLoginBinding;
import com.example.markitsurvey.handlingTime.GPSTime;
import com.example.markitsurvey.helper.Constants;
import com.example.markitsurvey.helper.KeyValueDB;
import com.example.markitsurvey.helper.SystemHelper;
import com.example.markitsurvey.helper.UIHelper;
import com.example.markitsurvey.helper.Utility;
import com.example.markitsurvey.logger.AppLogger;
import com.example.markitsurvey.logger.CustomFormatter;
import com.example.markitsurvey.models.AppVersion;
import com.example.markitsurvey.models.ApplicationDeviceResponse;
import com.example.markitsurvey.models.ApplicationLoginLogIdResponse;
import com.example.markitsurvey.models.ApplicationLoginLogResponse;
import com.example.markitsurvey.models.ServerDateTime;
import com.example.markitsurvey.models.SystemInfo;
import com.example.markitsurvey.models.UserDecode;
import com.example.markitsurvey.models.UserModelsResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSIONS = 0x1;
    ActivityLoginBinding binding;
    KeyValueDB keyValueDB;
    float v = 0;
    boolean isNavigate = false;
    String address = "";
    private FusedLocationProviderClient mFusedLocationClient;
    boolean isLocationRequest = false;
    LoginController loginController;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    double longitudeBest, latitudeBest;

    String token = "";

    private ResultReceiver resultReceiver;
    GPSTime gpsTime;
    String email;
    String password;

    LottieAnimationView lottieAnimationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        resultReceiver = new AddressResultReceiver(new Handler());
        gpsTime = new GPSTime();

        keyValueDB = new KeyValueDB(getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));
        lottieAnimationView = findViewById(R.id.animation_logo);
        lottieAnimationView.setVisibility(View.INVISIBLE);
        //================================================================================
        IntentFilter get_login_Intent = new IntentFilter("my.own.do_login_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_doLogin_broadcastReceiver, get_login_Intent);

        IntentFilter get_s3ApplicationLogging = new IntentFilter("my.own.do_app_login_log_id_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_post_log_id_broadcastReceiver, get_s3ApplicationLogging);

        IntentFilter get_app_version = new IntentFilter("my.own.do_app_version_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_check_version_broadcastReceiver, get_app_version);

        IntentFilter get_app_detailed = new IntentFilter("my.own.do_app_detailed_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_post_app_detailed_broadcastReceiver, get_app_detailed);


        IntentFilter get_app_date_time = new IntentFilter("my.own.do_app_date_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_doDate_broadcastReceiver, get_app_date_time);

        IntentFilter get_app_login_log = new IntentFilter("my.own.do_app_login_log_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_post_login_log_broadcastReceiver, get_app_login_log);
        //================================================================================

        loginController = new LoginController(this);

        LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

       // long networkTS = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();

        setPermissions();
        initialize();
        isLocationPermissionGranted();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lottieAnimationView.setVisibility(View.VISIBLE);

                if (userCredentials()){

                    if (isLocationRequest){

                        if (checkMobileApplicationTime()){
                            checkCurrentDateAndTime();
                        }else {

                            loginController.checkVersion(getString(R.string.applicationId));
                        }
                    }
                    else {
                        gpsTime.InitialiseLocationListener(LoginActivity.this);
                        getCurrentLocationRequest();
                    }

                }
            }
        });

        binding.userName.setTranslationX(-300);
        binding.userPassword.setTranslationX(-300);

        binding.userName.setAlpha(v);
        binding.userPassword.setAlpha(v);
        binding.imageView2.setAlpha(v);
        binding.btnLogin.setAlpha(v);
        binding.imageView3.setAlpha(v);

        binding.userName.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        binding.userPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        binding.imageView2.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        binding.btnLogin.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        binding.imageView3.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
    }


    private void isLocationPermissionGranted() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission

        } else {
            // already permission granted
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Toast.makeText(LoginActivity.this, "Last Location : Lat" + location.getLatitude() + " Lng: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                        getCurrentLocationRequest();
                    } else {
                        getCurrentLocationRequest();
                    }
                }
            });
        }
    }

    private boolean userCredentials() {

        email = binding.userName.getText().toString().trim();
        password = binding.userPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Name!", Toast.LENGTH_SHORT).show();
            lottieAnimationView.setVisibility(View.INVISIBLE);

            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
            lottieAnimationView.setVisibility(View.INVISIBLE);

            return false;
        }
        return true;




    }




    private void showNeedPermissionsMessage() {

        message("");
    }

    private void message(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean checkMobileApplicationTime() {
        boolean isDateCorrect = Boolean.parseBoolean(keyValueDB.getValue(getResources().getString(R.string.serverDateAndTime), "").isEmpty()
                ? "false" : keyValueDB.getValue(getResources().getString(R.string.serverDateAndTime), ""));
        return isDateCorrect;
    }

    private void setPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final int checkAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            final int checkStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            final int checkTIme = ContextCompat.checkSelfPermission(this, Manifest.permission.SET_TIME);
            final int checkLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            final int checkCorse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

            if (checkAudio != PackageManager.PERMISSION_GRANTED || checkStorage != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                    showNeedPermissionsMessage();
                }
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    showNeedPermissionsMessage();
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showNeedPermissionsMessage();
                } else {
                    requestPermissions(new String[]{
                                    Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.SET_TIME,
                                    Manifest.permission.ACCESS_WIFI_STATE,
                                    Manifest.permission.READ_PHONE_STATE},
                            REQUEST_CODE_PERMISSIONS);
                }
            } else {

            }
        } else {

        }
    }


    public void checkCurrentDateAndTime() {

        if (Utility.isNetworkAvailable(LoginActivity.this)) {
            loginController.CheckDateTime();

        } else {
            UIHelper.showError("Alert!", "Please check Internet connection", LoginActivity.this);
            lottieAnimationView.setVisibility(View.INVISIBLE);
            isNavigate = true;
        }
    }

    @Override
    public void onBackPressed() {
        UIHelper.showExitDialog("Exit", "Are you sure do you want to exit the app?", LoginActivity.this);
    }

    private void initialize() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void getCurrentLocationRequest() {
        lottieAnimationView.setVisibility(View.INVISIBLE);
        isLocationRequest = true;
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setSmallestDisplacement(0.0f);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                mFusedLocationClient.removeLocationUpdates(locationCallback);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;

                    double lat = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double lng = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    double acc = locationResult.getLocations().get(latestLocationIndex).getAccuracy();
                    long time = locationResult.getLocations().get(latestLocationIndex).getTime();
                    latitudeBest = lat;
                    longitudeBest = lng;
                    keyValueDB.save("lat", String.valueOf(lat));
                    keyValueDB.save("lng", String.valueOf(lng));
                    keyValueDB.save("acc", String.valueOf(acc));
                    Toast.makeText(LoginActivity.this, "Current Location : Lat" + lat
                            + "\n Lng: " + lng
                            + "\n Accuracy: " + acc, Toast.LENGTH_SHORT).show();
                    Location location = new Location("providerNA");
                    location.setLatitude(lat);
                    location.setLongitude(lng);
                    fetchAddressFromLatLng(location);
                }
            }


        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

    }

    private void fetchAddressFromLatLng(Location location)
    {
        Intent intent = new Intent(this, FetchAddressService.class);
        intent.putExtra(Constants.RECEIVER,resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA,location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                Toast.makeText(LoginActivity.this, resultData.getString(Constants.RESULT_DATA_KEY),
                        Toast.LENGTH_SHORT).show();
                    address = resultData.getString(Constants.RESULT_DATA_KEY);
                keyValueDB.save("address",String.valueOf(resultData.getString(Constants.RESULT_DATA_KEY)));
            } else {
                Toast.makeText(LoginActivity.this, resultData.getString(Constants.RESULT_DATA_KEY),
                        Toast.LENGTH_SHORT).show();

            }
        }
    }


    //////////////////////////////////////////////////////////////////
    private void s3ApplicationLogging()
    {

        UserDecode userDecode =UserDecode.ConvertToUserEntityUser(token);

        int lat = (int) latitudeBest;
        int lon = (int) longitudeBest;

        loginController.PostApplicationLoginLog(getString(R.string.applicationId),getString(R.string.applicationVersionId)
        ,userDecode.getId(),lat,lon,address);


    }

    private void getApplicationSyncDataId()
    {
        final KeyValueDB keyValueDB = new KeyValueDB(getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));



        String applicationLogId =  keyValueDB.getValue("applicationLogId", "");

        loginController.PostApplicationLoginLogId(applicationLogId);

    }


    private void enableLogger(int userId) {
        Utility.createDirectory("/sdcard/.MarkitSurvery/AppLogger/");
        FL.init(new FLConfig.Builder(this)
                .minLevel(FLConst.Level.V)
                .logToFile(true)
                .formatter(new CustomFormatter("" + userId))
                .dir(new File(new File("/sdcard/.MarkitSurvery/"), "AppLogger"))
                .retentionPolicy(FLConst.RetentionPolicy.FILE_COUNT)
                .build());
        FL.setEnabled(true);
        AppLogger.i(this.getClass().getName());
    }

    private void DeviceDetail()
    {
        final KeyValueDB keyValueDB = new KeyValueDB(getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));

        String syncDataId =  keyValueDB.getValue("syncDataId", "");

        SystemInfo device = SystemHelper.getSystemInformation(LoginActivity.this);

        loginController.PostApplicationDetailed(syncDataId,device.getManufacturer(),device.getModel(),device.getBrand(),device.getBoard(),device.getHardware()
        ,device.getSerialNo(),device.getDeviceId(),device.getAndroidID(),device.getScreenResolution(),device.getScreenSize(),device.getScreenDensity()
        ,device.getBootLoader(),device.getUser(),device.getHost(),device.getVersion(),device.getAPILevel(),device.getBuildID(),device.getBuildTime()
        ,device.getExternalMemoryIsAvailable(),device.getExternalMemory(),device.getInternalMemory(),device.getRAMInfo(),device.getVersionRelease());


    }
    ////////////////////////////////////////////////////////////////////////////////


    private BroadcastReceiver fetch_doDate_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            ServerDateTime data = (ServerDateTime) intent.getSerializableExtra("date");
            if (data != null) {

                try {

                    String convertedDate = "";
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                    Date date = format.parse(data.getServerDateTime());
                    convertedDate = sdf.format(date);

                    keyValueDB.save(getResources().getString(R.string.serverDateAndTime), convertedDate);

                    lottieAnimationView.setVisibility(View.INVISIBLE);
                } catch (ParseException e) {
                    e.printStackTrace();
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                }
            } else {

                Toast.makeText(getApplicationContext(), "error in date", Toast.LENGTH_SHORT).show();

                lottieAnimationView.setVisibility(View.INVISIBLE);
            }

        }
    };

    private BroadcastReceiver fetch_doLogin_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            UserModelsResponse data = (UserModelsResponse) intent.getSerializableExtra("result");
            if (data.getProjects() != null) {

                keyValueDB.save("token", data.getToken());
                keyValueDB.save("projectJSON", new Gson().toJson(data.getProjects()));

                //keyValueDB.save("textAlignment", "false");
                UserDecode user = UserDecode.ConvertToUserEntityUser(data.getToken());
                enableLogger(user.getId());
                AppLogger.i("token", token);
                token = data.getToken();
                s3ApplicationLogging();

            } else {

                Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();

                lottieAnimationView.setVisibility(View.INVISIBLE);
            }
        }
    };




    private BroadcastReceiver fetch_check_version_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            AppVersion appVersion = (AppVersion) intent.getSerializableExtra("appVersion");
            if (appVersion != null) {

                AppLogger.i("ServerVersion", appVersion.getVersion());
                AppLogger.i("AppVersion", getString(R.string.applicationVersion));
                if (!appVersion.getVersion().equalsIgnoreCase(getString(R.string.applicationVersion))){

                    UIHelper.applicationUpdateDialog("Alert!","Your application version is old Please update your application",LoginActivity.this);
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                }else {

                    loginController.userLogin(email, password, "08c53114-cbe9-42f9-aa4d-18024e44e90c");
                }
            } else {

                Toast.makeText(getApplicationContext(), "version error", Toast.LENGTH_SHORT).show();
                lottieAnimationView.setVisibility(View.INVISIBLE);
            }
        }
    };


    private BroadcastReceiver fetch_post_log_id_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ApplicationLoginLogIdResponse data = (ApplicationLoginLogIdResponse) intent.getSerializableExtra("logResponseId");
            if (data != null) {

                String syncDataId = data.getId();
                keyValueDB.save("syncDataId", String.valueOf(syncDataId));
                DeviceDetail();
            } else {

                Toast.makeText(getApplicationContext(), "error log id", Toast.LENGTH_SHORT).show();
                lottieAnimationView.setVisibility(View.INVISIBLE);
            }

        }
    };


    private BroadcastReceiver fetch_post_login_log_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ApplicationLoginLogResponse data = (ApplicationLoginLogResponse) intent.getSerializableExtra("logResponse");


            if (data != null) {

                int applicationLoginLogId = data.getId();
                keyValueDB.save("applicationLogId", String.valueOf(applicationLoginLogId));
                getApplicationSyncDataId();

            } else {

                Toast.makeText(getApplicationContext(), "error log", Toast.LENGTH_SHORT).show();
                lottieAnimationView.setVisibility(View.INVISIBLE);
            }

        }
    };



    private BroadcastReceiver fetch_post_app_detailed_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ApplicationDeviceResponse data = (ApplicationDeviceResponse) intent.getSerializableExtra("detailed");
            if (data != null) {


                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm a");
                String date = df.format(Calendar.getInstance().getTime());
                keyValueDB.save("lastTimeLogin",date);

                keyValueDB.save("token", token);
                lottieAnimationView.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, "login Successfully", Toast.LENGTH_SHORT).show();


                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            } else {

                Toast.makeText(getApplicationContext(), "error in application detailed", Toast.LENGTH_SHORT).show();

                lottieAnimationView.setVisibility(View.INVISIBLE);

            }

        }


    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_post_app_detailed_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_post_login_log_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_post_log_id_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_check_version_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_doLogin_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_doDate_broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_post_app_detailed_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_post_login_log_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_post_log_id_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_check_version_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_doLogin_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_doDate_broadcastReceiver);
    }
}