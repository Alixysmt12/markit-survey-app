package com.example.markitsurvey.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.example.markitsurvey.DBDAO.SurveyDAO;
import com.example.markitsurvey.R;
import com.example.markitsurvey.adapters.SurveyAdapter;
import com.example.markitsurvey.databinding.ActivitySurveyBinding;
import com.example.markitsurvey.helper.KeyValueDB;
import com.example.markitsurvey.helper.Utility;
import com.example.markitsurvey.logger.AppLogger;
import com.example.markitsurvey.models.AnswerJSON;
import com.example.markitsurvey.models.ProjectModel;
import com.example.markitsurvey.models.ProjectOneOffSettings;
import com.example.markitsurvey.models.ProjectWaveSetting;
import com.example.markitsurvey.models.Region;
import com.example.markitsurvey.models.Survey;
import com.example.markitsurvey.models.UserDecode;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.List;

public class SurveyActivityList extends AppCompatActivity {

    KeyValueDB keyValueDB;
    ProjectModel project;
    Region region;
    Survey survey;
    String REQUEST_GET_SURVEY_FROM_LOCAL_DB = "ShopsLocalDB";
    double longitudeBest, latitudeBest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    boolean isLocationRequest = false;

    UserDecode user = null;

    SurveyAdapter surveyAdapter;

    ActivitySurveyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySurveyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        keyValueDB = new KeyValueDB(getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));

        final Object obj = getIntent().getSerializableExtra("project");
        if (obj instanceof ProjectModel) {

            project = (ProjectModel) obj;

        }

        region = Region.ConvertToRegionEntity(keyValueDB.getValue("region", ""));
        initialize();
        getCurrentLocationRequest();

        binding.startMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SurveyActivityList.this, ActivityUserLocationMap.class);
                i.putExtra("region", region);
                startActivity(i);

            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkLocationIsOn()) {
                    if (Utility.checkShopLocation(region, longitudeBest, latitudeBest)) {
                        //createPermutationList();
                        UserDecode user = UserDecode.ConvertToUserEntityUser(keyValueDB.getValue("token", ""));
                        AppLogger.i("Create Survey Object");
                        survey = new Survey();
                        survey.setSubAreaName(region.getSubArea().getName());
                        survey.setLevel(region.getSubArea().getLevel());
                        survey.setCountryId(region.getSubArea().getCountryId());
                        survey.setCityId(region.getSubArea().getCityId());
                        survey.setStateId(region.getSubArea().getStateId());
                        survey.setSuperAreaId(region.getSubArea().getSuperAreaId());
                        survey.setAreaId(region.getSubArea().getAreaId());
                        survey.setSubAreaId(region.getSubArea().getId());
                        survey.setStartTime(Utility.getCurrentDateTime());
                        survey.setProjectId(region.getProjectId());
                        survey.setUserId(user.getId());
                        survey.setSurveyNature("New");
                        survey.setAnswerJSON(getPredefineAnswerJSON());
                        if (project.getProjectClassificationType() != null && project.getProjectClassificationType().getName().equalsIgnoreCase("Tracking")) {
                            String json = keyValueDB.getValue("currentWave", "");

                            ProjectWaveSetting wave = ProjectWaveSetting.getCurrentWave(json);
                            if (wave != null) {
                                survey.setVisitMonth(wave.getWaveName());
                                survey.setFieldStartDate(wave.getWaveStartDate());
                                survey.setFieldEndDate(wave.getWaveEndDate());
                            }
                        } else {

                            String json = keyValueDB.getValue("user" + user.getId() + "_project" + project.getId() + "_OneOff", "");
                            ProjectOneOffSettings oneOffSetting = ProjectOneOffSettings.getOneOfSettings(json);
                            if (oneOffSetting != null) {
                                survey.setVisitMonth(oneOffSetting.getName());
                                survey.setFieldStartDate(oneOffSetting.getFieldStartDate());
                                survey.setFieldEndDate(oneOffSetting.getFieldEndDate());
                            }
                        }
                        try {
                            survey.setGuid(Utility.createTransactionID());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLocationConfirmationDialog("Alert!!!", "\n" +
                                "کیا اپ کو یقین ہے" +
                                "آپ کے انٹرویو کا مقام درست ہے ورنہ آپ کا انٹرویو مسترد کر دیا جائے گا۔", survey);

                    } else {
                        //   shoOutOFBoundaryDialog("Alert!","Your are out of boundary please check your location",region);
                        Toast.makeText(SurveyActivityList.this, "You are out of boundary", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    enableDialogSettings();
                }

            }
        });

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent intent = new Intent(SurveyActivityList.this,RegionActivity.class);
                finish();
            }
        });
//        binding.back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        binding.surveyRec.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.surveyRec.setHasFixedSize(true);

        getUser();
        getSurveysFromServer();

    }


    public void showLocationConfirmationDialog(String title, String message, Survey survey) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(SurveyActivityList.this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                Intent intent = new Intent(SurveyActivityList.this, SurveyActivity.class);
                intent.putExtra("survey", survey);
                intent.putExtra("region", region);
                startActivity(intent);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.cancel();
            }
        });

        builder.show();
    }

    public void enableDialogSettings() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS Error");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Location is not enabled. Do you want to go to settings?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private String getPredefineAnswerJSON() {
        String val = keyValueDB.getValue("permutation", "");
        String[] arr = null;
        if (!val.isEmpty()) {
            arr = val.split(",");

        } else {
            keyValueDB.save("permutation", "1,2");
            val = "1,2";
            arr = val.split(",");
        }

        AnswerJSON answerJSON = new AnswerJSON();
        answerJSON.setV3(Integer.parseInt(arr[0]));
        answerJSON.setV4(Integer.parseInt(arr[1]));


        return new Gson().toJson(answerJSON);

    }

    private void getSurveysFromServer() {

        new AsyncHandler(REQUEST_GET_SURVEY_FROM_LOCAL_DB).execute();
    }


    private boolean checkLocationIsOn() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            return false;
        }
        return true;
    }

    private void initialize() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void getCurrentLocationRequest() {
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
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        Toast.makeText(SurveyActivityList.this, "Current Location : Lat" + location.getLatitude()
                                + "\n Lng: " + location.getLongitude()
                                + "\n Accuracy: " + location.getAccuracy(), Toast.LENGTH_SHORT).show();
                        //     if (location.getAccuracy() <= 30) {
                        latitudeBest = location.getLatitude();
                        longitudeBest = location.getLongitude();
                        keyValueDB.save("lat", String.valueOf(latitudeBest));
                        keyValueDB.save("lng", String.valueOf(longitudeBest));
//                            keyValueDB.save("lat",String.valueOf(latitudeBest));
//                            keyValueDB.save("lng",String.valueOf(longitudeBest));
//                            keyValueDB.save("acc",String.valueOf(location.getAccuracy()));
                        mFusedLocationClient.removeLocationUpdates(locationCallback);

                        //      }
                    }
                }
            }

        };
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private void getUser() {
        user = UserDecode.ConvertToUserEntityUser(keyValueDB.getValue("token", ""));
    }

    private void renderSurveyList(List<Survey> surveyList) {

        surveyAdapter = new SurveyAdapter(this, surveyList);
        binding.surveyRec.setAdapter(surveyAdapter);
    }

    private ProjectModel getProject(int projectId) {
        ProjectModel project = null;
        List<ProjectModel> projectList = ProjectModel.ConvertToProjectsEntity(keyValueDB.getValue("projectJSON", ""));
        for (int i = 0; i < projectList.size(); i++) {
            if (projectList.get(i).getId() == projectId) {
                project = projectList.get(i);
                break;
            }
        }
        return project;
    }

    private Survey getSurveyFromLocalDBSQLite(long shopCode) {
        SurveyDAO surveyDAO = new SurveyDAO(SurveyActivityList.this);
        return surveyDAO.getSurvey(shopCode);
    }


    public class AsyncHandler extends AsyncTask<Object, Void, String> {

        ProgressDialog pDialog = null;
        String _request;


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(SurveyActivityList.this);
            pDialog.setMessage("getting surveys");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        AsyncHandler(String request) {
            _request = request;
        }

        @Override
        protected void onPostExecute(String result) {

            if (pDialog.isShowing()) {

                pDialog.dismiss();
            }
            try {


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(Object... params) {
            String responseObject = null;


            try {

                if (_request.equalsIgnoreCase(REQUEST_GET_SURVEY_FROM_LOCAL_DB)) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                SurveyDAO surveyDAO = new SurveyDAO(SurveyActivityList.this);
                                List<Survey> surveys = surveyDAO.getwSurveysByProjectId(project.getId());
//                                String json =   keyValueDB.getValue("user"+user.getId()+"_region"+region.getId()+"_surveys","");
//                                List<Survey>  surveyList =    Survey.ConvertToEntity(json.toString());
//                                String json =  keyValueDB.getValue("user"+user.getId()+"_region"+region.getId()+"_surveys","");
//                                List<Survey>  surveyList =    Survey.ConvertToEntity(json);
//                                surveyList.addAll(surveys);
                                renderSurveyList(surveys);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseObject;
        }
    }

}