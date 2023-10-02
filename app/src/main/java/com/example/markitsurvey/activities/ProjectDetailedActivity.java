package com.example.markitsurvey.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.markitsurvey.R;
import com.example.markitsurvey.adapters.WaverAdapter;
import com.example.markitsurvey.controller.DetailedProjectController;
import com.example.markitsurvey.controller.LoginController;
import com.example.markitsurvey.databinding.ActivityDetailedBinding;
import com.example.markitsurvey.helper.KeyValueDB;
import com.example.markitsurvey.helper.UIHelper;
import com.example.markitsurvey.helper.Utility;
import com.example.markitsurvey.logger.AppLogger;
import com.example.markitsurvey.models.AppVersion;
import com.example.markitsurvey.models.DataQue;
import com.example.markitsurvey.models.ProjectMetaDataModel;
import com.example.markitsurvey.models.ProjectModel;
import com.example.markitsurvey.models.ProjectOneOffSettings;
import com.example.markitsurvey.models.ProjectOneOffSettingsModel;
import com.example.markitsurvey.models.ProjectSettingsModels;
import com.example.markitsurvey.models.ProjectWaveSetting;
import com.example.markitsurvey.models.QAStatusByUserId;
import com.example.markitsurvey.models.QAStatusByUserIdModel;
import com.example.markitsurvey.models.Questionnaire;
import com.example.markitsurvey.models.QuestionnaireModel;
import com.example.markitsurvey.models.Region;
import com.example.markitsurvey.models.RegionModel;
import com.example.markitsurvey.models.Section;
import com.example.markitsurvey.models.UserDecode;
import com.example.markitsurvey.models.WaveModelResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailedActivity extends AppCompatActivity {

    ActivityDetailedBinding binding;
    ProjectModel projectModel;

    KeyValueDB keyValueDB;
    UserDecode user;
    DetailedProjectController detailedProjectController;
    LoginController loginController;


    QuestionnaireModel questionnaireVersion;
    DataQue dataQue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        keyValueDB = new KeyValueDB(getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));


        //===================================================================
        IntentFilter get_que_Intent = new IntentFilter("my.own.do_get_guestionnaire_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_get_que_broadcastReceiver, get_que_Intent);

        IntentFilter get_que_id_Intent = new IntentFilter("my.own.do_get_que_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_get_que_id_broadcastReceiver, get_que_id_Intent);


        IntentFilter get_region_Intent = new IntentFilter("my.own.do_get_region_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_get_region_broadcastReceiver, get_region_Intent);

        IntentFilter get_wave_Intent = new IntentFilter("my.own.do_get_wave_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_get_wave_broadcastReceiver, get_wave_Intent);


        IntentFilter get_one_off_Intent = new IntentFilter("my.own.do_get_one_off_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_get_one_off_broadcastReceiver, get_one_off_Intent);

        IntentFilter get_pass_fail_Intent = new IntentFilter("my.own.do_get_pass_fail_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_get_pass_fail_broadcastReceiver, get_pass_fail_Intent);

        IntentFilter get_app_version = new IntentFilter("my.own.do_app_version_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_check_version_broadcastReceiver, get_app_version);

        IntentFilter get_app__project_meta_data = new IntentFilter("my.own.do_get_meta_data_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_get_meta_data_broadcastReceiver, get_app__project_meta_data);

        IntentFilter get_app_project_meta_data = new IntentFilter("my.own.do_get_project_setting_by_project_id_broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(fetch_get_project_setting_by_project_id_broadcastReceiver, get_app_project_meta_data);

        //====================================================================

        //////////////---------------....Controller initialize here...........
        detailedProjectController = new DetailedProjectController(this);
        loginController = new LoginController(this);


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //////////////////Animation
        binding.cardView.setTranslationX(-300);
        binding.linearLayout.setTranslationX(-300);
        binding.linearLayout2.setTranslationX(-300);

        binding.cardView.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(200).start();
        binding.linearLayout.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        binding.linearLayout2.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();


        final Object obj = getIntent().getSerializableExtra("projectData");
        if (obj instanceof ProjectModel) {

            projectModel = (ProjectModel) obj;

        }

        if (projectModel != null) {
            binding.lblProjectName.setText(projectModel.getProjectName());
            binding.createdBy.setText("" + projectModel.getCreatedBy());
            binding.startDate.setText(Utility.DateFormatter(projectModel.getStartingDate()));
            binding.createdDate.setText(Utility.DateFormatter(projectModel.getCreatedAt()));


            if (projectModel.getProjectClassificationType() != null && projectModel.getProjectClassificationType().getName().equalsIgnoreCase("Tracking")) {
                binding.waveLayout.setVisibility(View.VISIBLE);
            } else {
                binding.waveLayout.setVisibility(View.INVISIBLE);
            }
        }

        binding.spinnerWave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ProjectWaveSetting wave = (ProjectWaveSetting) parent.getItemAtPosition(position);
                    keyValueDB.save("executionMonth", wave.getWaveName());
                    keyValueDB.save("fieldStartDate", wave.getWaveStartDate());
                    keyValueDB.save("fieldEndDate", wave.getWaveEndDate());
                    keyValueDB.save("currentWave", new Gson().toJson(wave));

                    if (Utility.isNetworkAvailable(ProjectDetailedActivity.this)) {
                        getStatsPassFailByUserId(wave.getWaveName());
                    } else {
                        setQAStatusByUserId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.animationLogo.setVisibility(View.VISIBLE);
        binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bg));
        binding.linearLayout.setVisibility(View.INVISIBLE);
        binding.linearLayout2.setVisibility(View.INVISIBLE);


        /////////////////////================>>>>>>>>>>>>>>>>>
        getUser();
        getUpdatedQuestionnaireVersion();

        //getRegions();
        /////////////////////================>>>>>>>>>>>>>>>>>

        //////////////////////////////////////////////////////////
        binding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (projectModel.getProjectClassificationType() != null &&
                        projectModel.getProjectClassificationType().getName().equalsIgnoreCase("Tracking")) {
                    if (binding.spinnerWave.getSelectedItemPosition() != 0 && binding.spinnerWave.getSelectedItemPosition() != -1) {
                        String regions = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_Region", "");

                        List<Region> regionList = Region.ConvertToRegions(regions);
                        if (regionList != null && regionList.get(0).getSubArea() != null) {
                            if (checkQuestionnaireVersion()) {
                                Intent i = new Intent(ProjectDetailedActivity.this, RegionActivity.class);
                                i.putExtra("project", projectModel);
                                startActivity(i);
                            } else {
                                Toast.makeText(ProjectDetailedActivity.this, "Please Update the questionnaire", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ProjectDetailedActivity.this, "Please assign the region", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ProjectDetailedActivity.this, "Please Select wave", Toast.LENGTH_SHORT).show();
                    }
                } else if (projectModel.getProjectClassificationType() != null && projectModel.getProjectClassificationType().getName().equalsIgnoreCase("One-Off")) {
                    String oneOfSettings = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_OneOff", "");
                    if (!oneOfSettings.isEmpty()) {
                        String regions = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_Region", "");
                        List<Region> regionList = Region.ConvertToRegions(regions);
                        if (regionList != null && regionList.get(0).getSubArea() != null) {
                            ProjectOneOffSettings oneOffSetting = ProjectOneOffSettings.getOneOfSettings(oneOfSettings);
                            if (oneOffSetting != null) {
                                keyValueDB.save("executionMonth", oneOffSetting.getName());
                                keyValueDB.save("fieldStartDate", oneOffSetting.getFieldStartDate());
                                keyValueDB.save("fieldEndDate", oneOffSetting.getFieldEndDate());
                            }
                            if (checkQuestionnaireVersion()) {

                                Intent mapIntent = new Intent(ProjectDetailedActivity.this, MapActivity.class);
                                mapIntent.putExtra("project", projectModel);
                                startActivity(mapIntent);

                                Intent i = new Intent(ProjectDetailedActivity.this, RegionActivity.class);
                                i.putExtra("project", projectModel);
                                startActivity(i);
                            } else {
                                Toast.makeText(ProjectDetailedActivity.this, "Please Update the questionnaire", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ProjectDetailedActivity.this, "Please assign the region", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(ProjectDetailedActivity.this, "Please click Refresh button to download updated settings", Toast.LENGTH_SHORT).show();

                }


            }
        });

        binding.btnFresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Utility.isNetworkAvailable(ProjectDetailedActivity.this)) {

                    binding.animationLogo.setVisibility(View.VISIBLE);
                    binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bg));
                    binding.linearLayout.setVisibility(View.INVISIBLE);
                    binding.linearLayout2.setVisibility(View.INVISIBLE);


                    keyValueDB.clearValue("user" + user.getId() + "_project" + projectModel.getId() + "_Region");
                    keyValueDB.clearValue("user" + user.getId() + "_project" + projectModel.getId() + "_Questionnaire");
                    keyValueDB.clearValue("user" + user.getId() + "_project" + projectModel.getId() + "_waves");
                    keyValueDB.clearValue("user" + user.getId() + "_project" + projectModel.getId() + "_OneOff");
                    keyValueDB.clearValue("user" + user.getId() + "_project" + projectModel.getId() + "_Stats");


                    checkApplicationVersion();
                } else {
                    UIHelper.showError("Network Error", "Please check your internet connection", ProjectDetailedActivity.this);
                }
            }
        });


    }

    private void getRegions() {

        String json = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_Region", "");
        if (json.isEmpty()) {
            if (Utility.isNetworkAvailable(ProjectDetailedActivity.this)) {

                detailedProjectController.getRegion(user.getId(), projectModel.getId());

            } else {
                UIHelper.showError("Alert!", "Please check Internet connection", ProjectDetailedActivity.this);
                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);
            }
        } else {
            getSectionQuestionnaire();
        }

    }


    private boolean checkQuestionnaireVersion() {
        Questionnaire questionnaire = getQuestionnaire();
        if (questionnaire != null) {
            if (!dataQue.getVersion().equalsIgnoreCase(questionnaire.getVersion())) {
                return false;
            }
        }
        return true;

    }


    private Questionnaire getQuestionnaire() {
        UserDecode user = UserDecode.ConvertToUserEntityUser(keyValueDB.getValue("token", ""));
        String json = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_Questionnaire", "");
        if (json.isEmpty()) {
            return null;
        }
        List<Section> sections = Section.ConvertToProjectsEntity(json);

        Questionnaire questionnaire = sections.get(0).getQuestionnaire();

        return questionnaire;
    }

    private void getSectionQuestionnaire() {

        String json = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_Questionnaire", "");

        if (json.isEmpty()) {
            if (Utility.isNetworkAvailable(ProjectDetailedActivity.this)) {

                detailedProjectController.getQuestionnaire(user.getId(), projectModel.getId());

            } else {
                UIHelper.showError("Alert!", "Please check Internet connection", ProjectDetailedActivity.this);

                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);
            }
        } else {
            getProjectSettings();
        }
    }


    private void getProjectOneOffSettings() {


        String json = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_OneOff", "");
        if (json.isEmpty()) {
            if (Utility.isNetworkAvailable(ProjectDetailedActivity.this)) {

                detailedProjectController.getProjectOneOffSettings(projectModel.getId());

            } else {
                UIHelper.showError("Alert!", "Please check Internet connection", ProjectDetailedActivity.this);

                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);
            }
        } else {
            getProjectMetaData();
            //getUpdatedQuestionnaireVersion();
            // renderWaveDropDown();
        }


    }

    private void getUpdatedQuestionnaireVersion() {

        if (Utility.isNetworkAvailable(ProjectDetailedActivity.this)) {

            detailedProjectController.getQuestionnaireVersion(projectModel.getId());

        } else {
            String json = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_questionnaireVersion", "");

            Log.d("TAG", "getUpdatedQuestionnaireVersion: " + json);
            if (!json.isEmpty()) {
                questionnaireVersion = QuestionnaireModel.convertToQuestionnaireVersion(json);
                checkApplicationVersion();
            } else {
                checkApplicationVersion();
                UIHelper.showError("Alert!",
                        "please update project using internet only for the first time",
                        ProjectDetailedActivity.this);
            }
        }

    }

    private void checkApplicationVersion() {

        if (Utility.isNetworkAvailable(ProjectDetailedActivity.this)) {

            loginController.checkVersion(getString(R.string.applicationId));

        } else {
            getRegions();
        }

    }

    private void getUser() {
        user = UserDecode.ConvertToUserEntityUser(keyValueDB.getValue("token", ""));
    }


    private void getWaves() {


        String json = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_waves", "");
        if (json.isEmpty()) {
            if (Utility.isNetworkAvailable(ProjectDetailedActivity.this)) {

                detailedProjectController.getWave(projectModel.getId());

            } else {
                UIHelper.showError("Alert!", "Please check Internet connection", ProjectDetailedActivity.this);

                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);
            }
        } else {
            getProjectMetaData();
            renderWaveDropDown();
        }


    }

    //////////////////////////////////////////////////////////
    private void getStatsPassFailByUserId(String waveName) {

        String json = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_Stats", "");
        if (Utility.isNetworkAvailable(ProjectDetailedActivity.this)) {
            json = "";
            if (json.isEmpty()) {

                detailedProjectController.getQAStatusPassFail(user.getId(), projectModel.getId(), waveName);

            }
        } else {
            if (!json.isEmpty()) {
                setQAStatusByUserId();
            }
            UIHelper.showError("Alert!", "Please check Internet connection", ProjectDetailedActivity.this);

            binding.animationLogo.setVisibility(View.GONE);
            binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            binding.linearLayout.setVisibility(View.VISIBLE);
            binding.linearLayout2.setVisibility(View.VISIBLE);
        }

    }

    private void setQAStatusByUserId() {

  /*      binding.animationLogo.setVisibility(View.VISIBLE);
        binding.linearLayout.setVisibility(View.INVISIBLE);
        binding.linearLayout2.setVisibility(View.INVISIBLE);
        binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bg));*/
        String json = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_Stats", "");
        QAStatusByUserId baseQAStatus = QAStatusByUserId.ConvertToBaseRetailAuditAppFlow(json);

        String totalCount = String.valueOf(baseQAStatus.getTotal());

        if (!totalCount.equalsIgnoreCase("0")) {

            binding.animationLogo.setVisibility(View.GONE);
            binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            binding.linearLayout.setVisibility(View.VISIBLE);
            binding.linearLayout2.setVisibility(View.VISIBLE);

            binding.lblCount.setText(String.valueOf(baseQAStatus.getTotal()));
            binding.lblPendingCount.setText(baseQAStatus.getPending());
            binding.lblPassCount.setText(baseQAStatus.getPass());
            binding.lblFailCount.setText(baseQAStatus.getFail());

        } else {

            binding.animationLogo.setVisibility(View.GONE);
            binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            binding.linearLayout.setVisibility(View.VISIBLE);
            binding.linearLayout2.setVisibility(View.VISIBLE);

            binding.lblCount.setText("0");
            binding.lblPendingCount.setText("0");
            binding.lblPassCount.setText("0");
            binding.lblFailCount.setText("0");
        }
    }


    private void getProjectMetaData() {

        String json = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_ProjectMetaData", "");

        if (json.isEmpty()) {
            if (Utility.isNetworkAvailable(ProjectDetailedActivity.this)) {

                detailedProjectController.getProjectMetaData(projectModel.getId());

            } else {
                UIHelper.showError("Alert!", "Please check Internet connection", ProjectDetailedActivity.this);

                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);
            }
        } else {
            if (projectModel.getProjectClassificationType().getName().equalsIgnoreCase("One-Off")) {
                String oneOfJSON = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_OneOff", "");
                ProjectOneOffSettings oneOffSetting = ProjectOneOffSettings.getOneOfSettings(oneOfJSON);
                if (oneOffSetting != null) {
                    getStatsPassFailByUserId(oneOffSetting.getName());
                }

            }
        }

    }

    private void renderWaveDropDown() {

        String json = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_waves", "");
        ArrayList<ProjectWaveSetting> waveArrayList = ProjectWaveSetting.ConvertToWavesList(json);
        ProjectWaveSetting wave = new ProjectWaveSetting();
        wave.setActiveWave(true);
        wave.setCurrentWave(false);
        wave.setWaveName("Select Wave");
        wave.setProjectId(projectModel.getId());
        waveArrayList.add(0, wave);
        ProjectWaveSetting[] waves = waveArrayList.toArray(new ProjectWaveSetting[0]);
        WaverAdapter waverAdapter = new WaverAdapter(ProjectDetailedActivity.this, R.layout.spinner_item, waves);
        binding.spinnerWave.setAdapter(waverAdapter);

        binding.animationLogo.setVisibility(View.GONE);
        binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        binding.linearLayout.setVisibility(View.VISIBLE);
        binding.linearLayout2.setVisibility(View.VISIBLE);
    }


    private void getProjectSettings() {

        String json = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_Settings", "");
        if (json.isEmpty()) {
            if (Utility.isNetworkAvailable(ProjectDetailedActivity.this)) {

                detailedProjectController.getProjectSettingByProjectId(projectModel.getId());

            } else {
                UIHelper.showError("Alert!", "Please check Internet connection", ProjectDetailedActivity.this);

                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);
            }
        } else {
            if (projectModel.getProjectClassificationType() != null && projectModel.getProjectClassificationType().getName().equalsIgnoreCase("Tracking")) {
                getWaves();
            } else {
                getProjectOneOffSettings();
            }
        }
    }


    ////////////////////////////////////////BroadCast Receiver
    private BroadcastReceiver fetch_get_que_id_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            QuestionnaireModel data = (QuestionnaireModel) intent.getSerializableExtra("result");
            if (data != null) {

                dataQue = DataQue.convertToQuestionnaireVersion(new Gson().toJson(data.getData()));
                keyValueDB.save("user" + user.getId() + "_project" + projectModel.getId() + "_questionnaireVersion", new Gson().toJson(data.getData()));
                // Toast.makeText(ProjectDetailedActivity.this, "fetch_get_que_id_broadcastReceiver", Toast.LENGTH_SHORT).show();
                if (!checkQuestionnaireVersion()) {
                    UIHelper.showError("Questionnaire Update!",
                            "Please click refresh button to get updated questionnaire",
                            ProjectDetailedActivity.this);

                    binding.animationLogo.setVisibility(View.GONE);
                    binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    binding.linearLayout.setVisibility(View.VISIBLE);
                    binding.linearLayout2.setVisibility(View.VISIBLE);
                } else {

                    checkApplicationVersion();
                }

            } else {

                Toast.makeText(getApplicationContext(), "error log id", Toast.LENGTH_SHORT).show();
                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);
            }

        }
    };


    private BroadcastReceiver fetch_get_region_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            RegionModel data = (RegionModel) intent.getSerializableExtra("result");
            if (data != null) {
                try {

                    if (data.getRegions() != null && !data.getRegions().isEmpty()) {

                        keyValueDB.save("user" + user.getId() + "_project" + projectModel.getId() + "_Region", new Gson().toJson(data.getRegions()));

                        getSectionQuestionnaire();
                    } else {
                        binding.animationLogo.setVisibility(View.GONE);
                        binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        binding.linearLayout.setVisibility(View.VISIBLE);
                        binding.linearLayout2.setVisibility(View.VISIBLE);
                        UIHelper.showError("Region", "Please assign region to this user", ProjectDetailedActivity.this);
                    }
                } catch (Exception e) {
                    binding.animationLogo.setVisibility(View.GONE);
                    binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    binding.linearLayout.setVisibility(View.VISIBLE);
                    binding.linearLayout2.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                //   Toast.makeText(ProjectDetailedActivity.this, "fetch_get_region_broadcastReceiver", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(getApplicationContext(), "error log id", Toast.LENGTH_SHORT).show();
                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);
            }

        }
    };

    private BroadcastReceiver fetch_get_que_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String data = (String) intent.getSerializableExtra("result");

            if (data != null) {

                try {

                    JSONObject object = new JSONObject(data);
                    keyValueDB.save("user" + user.getId() + "_project" + projectModel.getId() + "_Questionnaire", object.getJSONArray("sections").toString());
                    getProjectSettings();

                } catch (JSONException e) {

                    Toast.makeText(ProjectDetailedActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.animationLogo.setVisibility(View.GONE);
                    binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    binding.linearLayout.setVisibility(View.VISIBLE);
                    binding.linearLayout2.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(getApplicationContext(), "error log id", Toast.LENGTH_SHORT).show();
                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);
            }
        }
    };


    private BroadcastReceiver fetch_get_wave_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            WaveModelResponse data = (WaveModelResponse) intent.getSerializableExtra("result");
            if (data != null) {

                try {
                    keyValueDB.save("user" + user.getId() + "_project" + projectModel.getId() + "_waves", new Gson().toJson(data.getProjectWaveSettings()));
                    AppLogger.i("wave", data.getProjectWaveSettings().toString());
                    getProjectMetaData();
                    renderWaveDropDown();
                } catch (Exception e) {
                    e.printStackTrace();
                    binding.animationLogo.setVisibility(View.GONE);
                    binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    binding.linearLayout.setVisibility(View.VISIBLE);
                    binding.linearLayout2.setVisibility(View.VISIBLE);
                }
                //   Toast.makeText(ProjectDetailedActivity.this, "fetch_get_wave_broadcastReceiver", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(getApplicationContext(), "error wave", Toast.LENGTH_SHORT).show();
                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);
            }

        }
    };


    private BroadcastReceiver fetch_get_one_off_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ProjectOneOffSettingsModel data = (ProjectOneOffSettingsModel) intent.getSerializableExtra("result");
            if (data != null) {

                keyValueDB.save("user" + user.getId() + "_project" + projectModel.getId() + "_OneOff", new Gson().toJson(data.getProjectOneOffSettings()));
                //    Toast.makeText(ProjectDetailedActivity.this, "fetch_get_one_off_broadcastReceiver", Toast.LENGTH_SHORT).show();
                //getUpdatedQuestionnaireVersion();
                getProjectMetaData();

            } else {

                Toast.makeText(getApplicationContext(), "error log id", Toast.LENGTH_SHORT).show();
                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);

            }

        }
    };


    private BroadcastReceiver fetch_get_pass_fail_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

                QAStatusByUserIdModel data = (QAStatusByUserIdModel) intent.getSerializableExtra("result");
            if (data != null) {

                keyValueDB.save("user" + user.getId() + "_project" + projectModel.getId() + "_Stats", new Gson().toJson(data.getQAStatusByUserId().get(0)));
                setQAStatusByUserId();

            } else {

                Toast.makeText(getApplicationContext(), "error log id", Toast.LENGTH_SHORT).show();
                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);

            }

        }
    };


    private BroadcastReceiver fetch_get_meta_data_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String error = intent.getStringExtra("resultError");

            if (error != null){

                if (projectModel.getProjectClassificationType().getName().equalsIgnoreCase("One-Off")) {
                    String oneOfJSON = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_OneOff", "");
                    ProjectOneOffSettings oneOffSetting = ProjectOneOffSettings.getOneOfSettings(oneOfJSON);
                    binding.animationLogo.setVisibility(View.GONE);
                    binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    binding.linearLayout.setVisibility(View.VISIBLE);
                    binding.linearLayout2.setVisibility(View.VISIBLE);
                    if (oneOffSetting != null) {
                        getStatsPassFailByUserId(oneOffSetting.getName());
                    }

                }

                return;
            }

            ProjectMetaDataModel data = (ProjectMetaDataModel) intent.getSerializableExtra("result");
            if (data != null) {

                keyValueDB.save("user" + user.getId() + "_project" + projectModel.getId() + "_ProjectMetaData", new Gson().toJson(data.getProjectMetaData()));
                AppLogger.i("projectMetaData", data.getProjectMetaData().toString());

                Toast.makeText(getApplicationContext(), "meta data", Toast.LENGTH_SHORT).show();
            }
            if (projectModel.getProjectClassificationType().getName().equalsIgnoreCase("One-Off")) {
                String oneOfJSON = keyValueDB.getValue("user" + user.getId() + "_project" + projectModel.getId() + "_OneOff", "");
                ProjectOneOffSettings oneOffSetting = ProjectOneOffSettings.getOneOfSettings(oneOfJSON);
                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);
                if (oneOffSetting != null) {
                    getStatsPassFailByUserId(oneOffSetting.getName());
                }

            } else {

                Toast.makeText(getApplicationContext(), "meta data error", Toast.LENGTH_SHORT).show();
                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);

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
                Toast.makeText(getApplicationContext(), "Version", Toast.LENGTH_SHORT).show();

                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);
                if (!appVersion.getVersion().equalsIgnoreCase(getString(R.string.applicationVersion))) {

                    UIHelper.applicationUpdateDialog("Alert!", "Your application version is old Please update your application", ProjectDetailedActivity.this);
                    binding.animationLogo.setVisibility(View.GONE);
                    binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    binding.linearLayout.setVisibility(View.VISIBLE);
                    binding.linearLayout2.setVisibility(View.VISIBLE);
                } else {

                    getRegions();
                }
            } else {

                Toast.makeText(getApplicationContext(), "version error", Toast.LENGTH_SHORT).show();
                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);
            }
        }
    };


    private BroadcastReceiver fetch_get_project_setting_by_project_id_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String error = intent.getStringExtra("resultError");

            if (error != null){
                if (projectModel.getProjectClassificationType() != null && projectModel.getProjectClassificationType().getName().equalsIgnoreCase("Tracking")) {
                    getWaves();
                } else {
                    getProjectOneOffSettings();
                }
                return;
            }
            ProjectSettingsModels data = (ProjectSettingsModels) intent.getSerializableExtra("result");
            if (data != null) {

                keyValueDB.save("user" + user.getId() + "_project" + projectModel.getId() + "_Settings", new Gson().toJson(data.getProjectSettings()));
                AppLogger.i("user" + user.getId() + "_project" + projectModel.getId() + "_Settings");

                if (projectModel.getProjectClassificationType() != null && projectModel.getProjectClassificationType().getName().equalsIgnoreCase("Tracking")) {
                    getWaves();
                } else {
                    getProjectOneOffSettings();
                }
            } else {

                Toast.makeText(getApplicationContext(), "version error", Toast.LENGTH_SHORT).show();
                binding.animationLogo.setVisibility(View.GONE);
                binding.scrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.linearLayout2.setVisibility(View.VISIBLE);
            }
        }
    };

  /*  private void startBroadCastReceiverDateAndTime() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(ProjectDetailedActivity.this, DateReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(ProjectDetailedActivity.this, 234324243, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000, alarmIntent);

    }
*/


    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_que_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_que_id_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_region_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_wave_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_one_off_broadcastReceiver);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_pass_fail_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_check_version_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_meta_data_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_project_setting_by_project_id_broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_que_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_que_id_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_region_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_wave_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_one_off_broadcastReceiver);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_pass_fail_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_check_version_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_meta_data_broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fetch_get_project_setting_by_project_id_broadcastReceiver);
    }
}