package com.example.markitsurvey.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.markitsurvey.JavaScriptInterface.JavaScriptInterface;
import com.example.markitsurvey.R;
import com.example.markitsurvey.helper.Constants;
import com.example.markitsurvey.helper.KeyValueDB;
import com.example.markitsurvey.helper.Utility;
import com.example.markitsurvey.logger.AppLogger;
import com.example.markitsurvey.models.GPSPoint;
import com.example.markitsurvey.models.ProjectModel;
import com.example.markitsurvey.models.Questionnaire;
import com.example.markitsurvey.models.Region;
import com.example.markitsurvey.models.Section;
import com.example.markitsurvey.models.Survey;
import com.example.markitsurvey.models.UserDecode;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SurveyActivity extends AppCompatActivity {


    WebView webView;
    KeyValueDB keyValueDB;
    int projectId;
    Survey survey;
    String activityState = "comingFromActivity";
    private PermissionRequest myRequest;
    private static final int FILECHOOSER_RESULTCODE = 5173;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 101;

    double longitudeBest, latitudeBest;
    private FusedLocationProviderClient mFusedLocationClient;
    private ValueCallback<Uri> mUploadMessage;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    boolean isLocationRequest = false;
    RelativeLayout progress_overlay;
    List<GPSPoint> gpsPoints;
    private ResultReceiver resultReceiver;
    Region region;
    LocationManager locationManager;
   // TextView lblTestIDMsg;
    UserDecode user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey2);

        webView = (WebView) findViewById(R.id.webview);
        resultReceiver = new AddressResultReceiver(new Handler());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsPoints = new ArrayList<>();
        progress_overlay = (RelativeLayout) findViewById(R.id.progress_overlay);
        //lblTestIDMsg = (TextView) findViewById(R.id.lblError);
        progress_overlay.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);

        keyValueDB = new KeyValueDB(getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));
        getUser();
        projectId = getIntent().getExtras().getInt("projectId");
        survey = (Survey) getIntent().getSerializableExtra("survey");
        region = (Region) getIntent().getSerializableExtra("region");
        keyValueDB.save("comingFromActivity", activityState);
        keyValueDB.save("qnrlat", "");
        keyValueDB.save("qnrlat", "");

        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            AppLogger.i("SDK LAYER_TYPE_HARDWARE", String.valueOf(Build.VERSION.SDK_INT));
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            AppLogger.i("SDK LAYER_TYPE_SOFTWARE", String.valueOf(Build.VERSION.SDK_INT));
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);


        initialize();
        if (Boolean.parseBoolean(keyValueDB.getValue("googleLocation", "").isEmpty() ? "false" : keyValueDB.getValue("googleLocation", ""))) {
            getCurrentLocationRequest();
        } else {

            getLocation();
        }

        setSurveyParameters();


        webView.addJavascriptInterface(new JavaScriptInterface(this, getQuestionnaire(), survey), "Android");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                AppLogger.i("onPermissionRequest");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    request.grant(request.getResources());
                }
                myRequest = request;

                //   askForPermission(request.getOrigin().toString(), Manifest.permission.RECORD_AUDIO, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
                //   askForPermission(request.getOrigin().toString(), Manifest.permission.CAMERA, MY_PERMISSIONS_REQUEST_CAMERA);
//                for (String permission : request.getResources()) {
//                    switch (permission) {
//                        case "android.webkit.resource.AUDIO_CAPTURE": {
//                            askForPermission(request.getOrigin().toString(), Manifest.permission.RECORD_AUDIO, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
//                            break;
//                        }
//
//                    }
//                }
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                AppLogger.i("WebView Log: ", consoleMessage.message());
                AppLogger.i("WebView Log:", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return true;
            }

            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                this.openFileChooser(uploadMsg, "*/*");
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                this.openFileChooser(uploadMsg, acceptType, null);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }


            public boolean onShowFileChooser(


                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams) {
                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(SurveyActivity.this.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = Utility.createImageFile();
                        AppLogger.i("Image-File-Path", photoFile.getAbsolutePath());
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    } catch (IOException ex) {
                        Log.e("TAG", "Image file creation failed", ex);
                        AppLogger.i("Image-File-creation-failed", ex.toString());
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        AppLogger.i("Image-File-Path", photoFile.getAbsolutePath());
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);
                return true;
            }
        });
        webView.loadUrl("file:///android_asset/index.html");
        // webView.loadUrl("https://www.google.com");
        if (user.getRoleGroup().equalsIgnoreCase(getString(R.string.testRole))) {
            Typeface font = Typeface.createFromAsset(getAssets(), "urdufont/Jameel_Noori_Nastaleeq.ttf");
          //  lblTestIDMsg.setTypeface(font);
           // lblTestIDMsg.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                Log.d("WebView", "PERMISSION FOR AUDIO");
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    myRequest.grant(myRequest.getResources());
                    //   myWebView.loadUrl("file:///android_asset/first.html");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    } else if (intent != null) {
                        if (mCM != null) {
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};

                        }
                    }
                }
            }


            if (results != null) {
                final File file = new File(results[0].getPath());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utility.ImageCompression imageCompression = new Utility().new ImageCompression(SurveyActivity.this);
                        //imageCompression.execute(file.getAbsolutePath(), "/.Markit/Images/", file.getName());
                        ProjectModel project = ProjectModel.getProjectById(keyValueDB.getValue("projectJSON", ""), survey.getProjectId());
                        imageCompression.execute(file.getAbsolutePath(), Utility.imageFolderPath(project.getProjectName()), file.getName());
                        AppLogger.i("Image-Save");
                    }
                }, 1000);
                mUMA.onReceiveValue(results);
                mUMA = null;
            } else {
                mUMA.onReceiveValue(results);
                mUMA = null;
            }

        } else {
            if (requestCode == FCR) {
                if (null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }

    private void showDialog(String message) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //  mFusedLocationClient.removeLocationUpdates(locationCallback);
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }

    @Override
    public void onBackPressed() {
        showDialog("Are you sure to discard the survey?");
    }

    @Override
    public void onStop() {
        // Do your stuff here
        super.onStop();
//        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityState = keyValueDB.getValue("comingFromActivity", "");
        if (activityState.equalsIgnoreCase("comingFromActivity")) {

        }
    }

    private void setSurveyParameters() {
        survey.setQuestionnaireId(getQuestionnaire().getId());
        survey.setSectionId(getQuestionnaire().getSectionId());
        survey.setQuestionnaireVersion(getQuestionnaire().getVersion());
    }

    private Questionnaire getQuestionnaire() {
        UserDecode user = UserDecode.ConvertToUserEntityUser(keyValueDB.getValue("token", ""));
        String json = keyValueDB.getValue("user" + user.getId() + "_project" + survey.getProjectId() + "_Questionnaire", "");
        List<Section> sections = Section.ConvertToProjectsEntity(json);
        Questionnaire questionnaire = sections.get(0).getQuestionnaire();
        return questionnaire;
    }

    @Override
    protected void onPause() {
        super.onPause();

        activityState = "comingFromOutside";
        keyValueDB.save("comingFromActivity", activityState);

    }

    private void initialize() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void getCurrentLocationRequest() {
        isLocationRequest = true;
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        //locationRequest.setSmallestDisplacement(2);

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
        LocationServices.getFusedLocationProviderClient(SurveyActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double lat = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double lng = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            double acc = locationResult.getLocations().get(latestLocationIndex).getAccuracy();
                            long time = locationResult.getLocations().get(latestLocationIndex).getTime();
                            Date date = new Date();
                            date.setTime(time);
                            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String newDateStr = curFormater.format(date);
                            keyValueDB.save("lat", String.valueOf(lat));
                            keyValueDB.save("lng", String.valueOf(lng));
                            AppLogger.i("Before Checking out of boundary");
                            AppLogger.i("lat", String.valueOf(lat));
                            AppLogger.i("lng", String.valueOf(lng));
                            if (Utility.checkShopLocation(region, lng, lat)) {
                                AppLogger.i("Before Checking Accuracy");
                                if (acc <= 50) {

                                    AppLogger.i("Before Checking Accuracy");
                                    LocationServices.getFusedLocationProviderClient(SurveyActivity.this)
                                            .removeLocationUpdates(this);
                                    keyValueDB.save("qnrlat", String.valueOf(lat));
                                    keyValueDB.save("qnrlng", String.valueOf(lng));
                                    keyValueDB.save("qnracc", String.valueOf(locationResult.getLocations().get(latestLocationIndex).getAccuracy()));
                                    AppLogger.e("Exact Point", lat + "," + lng);
                                    progress_overlay.setVisibility(View.GONE);
                                    webView.setVisibility(View.VISIBLE);

                                }
                            } else {
                                Toast.makeText(SurveyActivity.this, "You are out of boundary : Lat" + lat
                                        + "\n Lng: " + lng, Toast.LENGTH_SHORT).show();
                            }

                            Toast.makeText(SurveyActivity.this, "Google Current Location : Lat" + lat
                                    + "\n Lng: " + lng + "Accuracy: " + acc + " Time: " + newDateStr, Toast.LENGTH_SHORT).show();

                        }
                    }
                }, Looper.myLooper());


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
                Toast.makeText(SurveyActivity.this, resultData.getString(Constants.RESULT_DATA_KEY),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SurveyActivity.this, resultData.getString(Constants.RESULT_DATA_KEY),
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void getUser() {
        user = UserDecode.ConvertToUserEntityUser(keyValueDB.getValue("token", ""));
    }

    public void getLocation() {
        //  AppLogger.i("Location Calling Start");
        if (!checkLocation()) {
            return;
        }
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = locationManager.getBestProvider(criteria, true);

        AppLogger.i("Location Provider", provider);
        AppLogger.i("Location Calling Start");
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, locationListenerBest);
//        if (provider != null) {
//            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    Activity#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for Activity#requestPermissions for more details.
//                return;
//            }
//
//            locationManager.requestLocationUpdates(provider, 0, 0.0f, locationListenerBest);
//
//        }
//        else
//        {
//            AppLogger.i("Location Calling Start");
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, locationListenerBest);
//        }
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }


    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(SurveyActivity.this);
        dialog.setTitle("Enable GPSPoint")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable GPSPoint to " +
                        "use this app")
                .setPositiveButton("GPSPoint Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(final Location location) {
            AppLogger.i("Location Change Call", "" + location.getAccuracy());

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (location.getAccuracy() <= 50) {
                        latitudeBest = 0.0;
                        longitudeBest = 0.0;
                        latitudeBest = location.getLatitude();
                        longitudeBest = location.getLongitude();
                        keyValueDB.save("qnrlat", String.valueOf(location.getLatitude()));
                        keyValueDB.save("qnrlng", String.valueOf(location.getLongitude()));
                        keyValueDB.save("qnracc", String.valueOf(location.getAccuracy()));
                        AppLogger.i("Found Accuracy", "" + location.getAccuracy());
                        AppLogger.i("Location Found");
                        AppLogger.i("lat", String.valueOf(location.getLatitude()));
                        AppLogger.i("lng", String.valueOf(location.getLongitude()));
                        progress_overlay.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                        locationManager.removeUpdates(locationListenerBest);
                    }
                    //   AppLogger.i("Accuracy",""+location.getAccuracy());
                    Toast.makeText(SurveyActivity.this, "Best Provider GPS update\n" + location.getLongitude() + " " + location.getLatitude() + " " + "Accuracy: " + location.getAccuracy(), Toast.LENGTH_SHORT).show();

                    Log.d("UI thread", "I am the UI thread");
                }
            });
            //  longitudeValueBest.setText(longitudeBest + "");
            //  latitudeValueBest.setText(latitudeBest + "");
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
}