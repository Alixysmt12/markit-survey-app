package com.example.markitsurvey.ui.synchronize;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.markitsurvey.DBDAO.SurveyDAO;
import com.example.markitsurvey.R;
import com.example.markitsurvey.activities.MainActivity;
import com.example.markitsurvey.controller.SyncController;
import com.example.markitsurvey.databinding.FragmentSynchronizeBinding;
import com.example.markitsurvey.helper.KeyValueDB;
import com.example.markitsurvey.helper.UIHelper;
import com.example.markitsurvey.helper.Utility;
import com.example.markitsurvey.logger.AppLogger;
import com.example.markitsurvey.models.AssetsDetailManager;
import com.example.markitsurvey.models.FileDetail;
import com.example.markitsurvey.models.Survey;
import com.example.markitsurvey.models.SyncResponse;
import com.example.markitsurvey.models.UserDecode;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SynchronizeFragment extends Fragment {


    private FragmentSynchronizeBinding binding;

    SyncController syncController;

    AppCompatActivity _activity;
    List<File> imagesList;
    List<File> audioList;
    List<File> DBBackupFiles;
    String REQUEST_DATA = "Data";
    String REQUEST_FILE = "File";
    String REQUEST_IMAGES = "Images";
    String REQUEST_GPS_POINTS = "gpsPoints";
    List<File> LogFiles;
    KeyValueDB keyValueDB;
    List<Survey> questionnaireList;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof AppCompatActivity) {
            _activity = (AppCompatActivity) context;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSynchronizeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        keyValueDB = new KeyValueDB(_activity.getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));
        UserDecode user = UserDecode.ConvertToUserEntityUser(keyValueDB.getValue("token", ""));
        Utility.exportDatabase(_activity, user.getId());


        IntentFilter get_syc_Intent = new IntentFilter("my.own.do_app_sync_data_user_broadcast");
        LocalBroadcastManager.getInstance(_activity).registerReceiver(fetch_post_sync_data_broadcastReceiver, get_syc_Intent);

        syncController = new SyncController(_activity);

        binding.openNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) getActivity()).openDrawer();

            }
        });


        //  gpsPointList =  getGPSPoints();
        setLabels();
        // getDBBackupFiles();
        try {
            PackageInfo pInfo = _activity.getPackageManager().getPackageInfo(_activity.getPackageName(), 0);
            String version = pInfo.versionName;
            binding.lblVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        binding.cardViewQuestionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                syncQuestionnaire();

            }
        });

        //////////////////////Animation
        binding.cardViewQuestionnaire.setTranslationX(-700);
        binding.contributeCard2.setTranslationX(-700);
        binding.contributeCard3.setTranslationX(-700);

        binding.cardViewQuestionnaire.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(200).start();
        binding.contributeCard2.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        binding.contributeCard3.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();


 /*       binding.contributeCard3.animate()
                .rotationX(360).rotationY(360)
                .setDuration(1000)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        binding.contributeCard3.setRotationX(0);
                        binding.contributeCard3.setRotationY(0);
                    }
                });*/

        return root;
    }


    private void syncQuestionnaire() {
        final KeyValueDB keyValueDB = new KeyValueDB(_activity.getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));
        if (questionnaireList.size() > 0) {
            Survey survey = questionnaireList.get(0);

            JSONObject parameters = new JSONObject();

            // Map<String,String> hashMap=new HashMap<>();

            try {
                parameters.put("userId", survey.getUserId());
                parameters.put("projectId", survey.getProjectId());
                parameters.put("sectionId", survey.getSectionId());
                parameters.put("questionnaireId", survey.getQuestionnaireId());
                parameters.put("lat", survey.getLat());
                parameters.put("lng", survey.getLng());
                parameters.put("answerJSON", survey.getAnswerJSON());
                parameters.put("answerResult", survey.getParseJSON());
                parameters.put("startDateTime", survey.getStartTime());
                parameters.put("endDateTime", survey.getEndTime());
                parameters.put("subAreaName", survey.getSubAreaName());
                parameters.put("subAreaId", survey.getSubAreaId());
                parameters.put("countryId", survey.getCountryId());
                parameters.put("stateId", survey.getStateId());
                parameters.put("cityId", survey.getCityId());
                parameters.put("superAreaId", survey.getSuperAreaId());
                parameters.put("areaId", survey.getAreaId());
                parameters.put("level", survey.getLevel());
                parameters.put("gpsPoints", survey.getGpsPoints());
                parameters.put("LOI", survey.getLOI());
                parameters.put("displayMeta", survey.getDisplayMeta());
                parameters.put("surveyNature", survey.getSurveyNature());
                parameters.put("guid", survey.getGuid());
                parameters.put("visitMonth", survey.getVisitMonth());
                parameters.put("fieldStartDate", survey.getFieldStartDate());
                parameters.put("fieldEndDate", survey.getFieldEndDate());
                parameters.put("questionnaireVersion", survey.getQuestionnaireVersion());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AppLogger.i("params", parameters.toString());

            if (Utility.isNetworkAvailable(_activity)) {

                syncController.syncQuestionnaire(parameters.toString());

            } else {
                AppLogger.i("Network Error", "Please check your internet connection");
                UIHelper.showError("Network Error", "Please check your internet connnction", _activity);
            }

        } else {
            setLabels();
            //    syncRecRecords();
            syncAudios();

        }
    }

    private void syncAudios() {
        if (audioList.size() > 0) {
            String projectName = "";
            File file = audioList.get(0);
            if (file.exists()) {
                String[] fileParts = file.getAbsolutePath().split("/");
                for (int i = 0; i < fileParts.length; i++) {
                    if (fileParts[i].equalsIgnoreCase(".MarkitSurvey")) {
                        projectName = fileParts[i + 1];
                    }
                }
                AppLogger.i("File  Exist Audio");

             /*   ProgressDialog pDialog = new ProgressDialog(_activity);
                pDialog.setIndeterminate(true);
                pDialog.setMessage("Uploading Audios");
                pDialog.setCancelable(false);
                pDialog.show();*/
                binding.progressbar.setVisibility(View.VISIBLE);
                Ion.with(_activity)
                        .load(getString(R.string.R3baseURL) + "syncSimpleSurveyAudios")
                        .setMultipartFile("audio", "text/plain", file)
                        .setMultipartParameter("projectName", String.valueOf(projectName))
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {

                                //do stuff with result
                                if (result != null && result.get("status").getAsInt() == 200) {
                                   /* if (pDialog.isShowing()) {
                                        pDialog.dismiss();
                                    }*/

                                    binding.progressbar.setVisibility(View.GONE);
                                    String message = result.get("data").getAsJsonObject().get("message").getAsString();
                                    AppLogger.i(message);
                                    Toast.makeText(_activity, message, Toast.LENGTH_SHORT).show();

                                    file.delete();
                                    audioList.remove(0);
                                    setLabels();
                                    syncAudios();
                                } else if (result != null && result.get("status").getAsInt() == 400) {
                                    binding.progressbar.setVisibility(View.GONE);
                                    String message = result.get("error").getAsString();
                                    Toast.makeText(_activity, message, Toast.LENGTH_SHORT).show();
                                    AppLogger.i("Error", message);
                                } else {
                                   // if (pDialog.isShowing()) {
                                     //   pDialog.dismiss();
                                        AppLogger.i("Exception Invoice", e.getMessage());
                                        binding.progressbar.setVisibility(View.GONE);
                                    //}
                                }
                            }
                        });
            } else {
                AppLogger.i("File Not Exist Invoice");
                file.delete();
                audioList.remove(0);

                setLabels();
                syncAudios();
            }
        } else {
            setLabels();
            syncImages();
        }
    }


    private void syncImages()
    {
        if (imagesList.size() > 0) {
            String projectName= "";
            File file = imagesList.get(0);
            if (file.exists()) {
                String[] fileParts =   file.getAbsolutePath().split("/");
                for (int i =0; i < fileParts.length; i ++)
                {
                    if (fileParts[i].equalsIgnoreCase(".MarkitSurvey"))
                    {
                        projectName = fileParts[i+1];
                    }
                }
                AppLogger.i("File  Exist Image");

            /*    ProgressDialog pDialog = new ProgressDialog(_activity);
                pDialog.setIndeterminate(true);
                pDialog.setMessage("Uploading  Image");
                pDialog.setCancelable(false);
                pDialog.show();*/
                binding.progressbar.setVisibility(View.VISIBLE);
                Ion.with(_activity)
                        .load(getString(R.string.R3baseURL) + "syncSimpleSurveyImages")
                        .setMultipartFile("image", "image/jpeg", file)
                        .setMultipartParameter("projectName",  String.valueOf(projectName))
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {

                                //do stuff with result
                                if (result != null && result.get("status").getAsInt() == 200) {
                                    binding.progressbar.setVisibility(View.GONE);
                                    String message = result.get("data").getAsJsonObject().get("message").getAsString();
                                    AppLogger.i(message);
                                    Toast.makeText(_activity, message, Toast.LENGTH_SHORT).show();

                                    file.delete();
                                    imagesList.remove(0);
                                    setLabels();
                                    syncImages();
                                    // syncAudios();
                                }
                                else if (result != null && result.get("status").getAsInt() == 400){
                                    binding.progressbar.setVisibility(View.GONE);
                                    String message = result.get("error").getAsString();
                                    Toast.makeText(_activity, message, Toast.LENGTH_SHORT).show();
                                    AppLogger.i("Error",message);
                                }
                                else {
                                  //  if (pDialog.isShowing()) {
                                     //   pDialog.dismiss();
                                    binding.progressbar.setVisibility(View.GONE);
                                        AppLogger.i("Exception Invoice",e.getMessage());
                                    //}
                                }
                            }
                        });
            }
            else
            {
                AppLogger.i(" Image File Not Exist");
                file.delete();
                imagesList.remove(0);

                setLabels();
                syncImages();
            }
        }

        else
        {
            setLabels();
        }
    }

    private void setLabels() {

        SurveyDAO questionnaireDAO = new SurveyDAO(_activity);
        questionnaireList = questionnaireDAO.getAllSurvey();
        AppLogger.i("Total Survey :", "" + questionnaireList.size());
        binding.lblQuestionnaireCount.setText("" + questionnaireList.size());
        audioList = new ArrayList<>();
        imagesList = new ArrayList<>();
        int audioCount = 0;
        int imagesCount = 0;
        List<FileDetail> fileDetails = getAssetDetailsForImagesAndAudio();
        if (fileDetails.size() > 0) {

            for (FileDetail fd : fileDetails) {
                if (fd.getFileName().contains(".txt")) {
                    audioList.add(new File(fd.getPath()));
                    audioCount++;
                } else if (fd.getFileName().contains(".jpg")) {
                    imagesList.add(new File(fd.getPath()));
                    imagesCount++;
                }
            }
        }
        if (questionnaireList.size() > 0) {
            //imageViewQuestionnaire.setImageResource(R.drawable.ic_upload_document);
        } else {
            //imageViewQuestionnaire.setImageResource(R.drawable.ic_upload_document_disabled);
        }
        if (audioCount > 0) {
            binding.lblAudioCount.setText("" + audioCount);
            //imageViewFiles.setImageResource(R.drawable.ic_audio_upload);
        } else {
            //imageViewFiles.setImageResource(R.drawable.ic_audio_upload_disabled);
        }
        if (imagesCount > 0) {
            binding.lblImageCount.setText("" + imagesCount);
            //imageViewImages.setImageResource(R.drawable.ic_image_upload);
        } else {
            //imageViewImages.setImageResource(R.drawable.ic_image_upload_disabled);
        }
    }

    private List<FileDetail> getAssetDetailsForImagesAndAudio() {
        AppLogger.i("getAssetDetailsForImagesAndAudio");
        KeyValueDB keyValueDB = new KeyValueDB(_activity.getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));
        String path = Environment.getExternalStorageDirectory().toString() + "/.MarkitSurvey";
        List<FileDetail> fileDetailList = new ArrayList<>();
        AssetsDetailManager assetsDetailManager = new AssetsDetailManager();
        try {
            assetsDetailManager.getFilesImagesAudios(Utility.createTransactionID(), path, fileDetailList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileDetailList;
    }


    private BroadcastReceiver fetch_post_sync_data_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            SyncResponse data = (SyncResponse) intent.getSerializableExtra("result");
            if (data != null) {
                String message = data.getData().getMessage();

                SurveyDAO surveyDAO = new SurveyDAO(_activity);
                surveyDAO.delete(questionnaireList.get(0).getId());


                questionnaireList.remove(0);
                setLabels();
                syncQuestionnaire();
                Toast.makeText(_activity, message, Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(_activity, "Internal error occur", Toast.LENGTH_SHORT).show();
            }

        }
    };

}