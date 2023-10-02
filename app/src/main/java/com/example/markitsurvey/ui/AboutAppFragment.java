package com.example.markitsurvey.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.markitsurvey.R;
import com.example.markitsurvey.activities.MainActivity;
import com.example.markitsurvey.databinding.FragmentAboutAppBinding;
import com.example.markitsurvey.helper.KeyValueDB;


public class AboutAppFragment extends Fragment {

    FragmentAboutAppBinding binding;

    KeyValueDB keyValueDB;

    AppCompatActivity _activity;

    TextView network, gps, storagePermission, cameraPermission, audio, lastTimeLogin, batterSaver;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


        if (context instanceof AppCompatActivity) {
            _activity = (AppCompatActivity) context;
        }
    }

    public AboutAppFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAboutAppBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        keyValueDB = new KeyValueDB(_activity.getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));
        network = view.findViewById(R.id.internet_yes_no);
        gps = view.findViewById(R.id.gps_yes_no);
        /*spaceInMobile = view.findViewById(R.id.space_yes_no);
        requiredSpace = view.findViewById(R.id.need_yes_no);*/
        storagePermission = view.findViewById(R.id.storage_yes_no);
        cameraPermission = view.findViewById(R.id.camera_yes_no);
        audio = view.findViewById(R.id.audio_yes_no);
        lastTimeLogin = view.findViewById(R.id.last_time_login);
        batterSaver = view.findViewById(R.id.batter_saver_is_on);
        lastTimeLogin.setText(keyValueDB.getValue("lastTimeLogin", "no Date Found"));


        binding.openNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) getActivity()).openDrawer();

            }
        });

        //////////////////////Animation
        binding.cardViewSurvey.setTranslationX(-700);
        binding.cardViewSurvey1.setTranslationX(-700);
        binding.cardViewSurvey2.setTranslationX(-700);
        binding.cardViewSurvey4.setTranslationX(-700);
        binding.cardViewSurvey5.setTranslationX(-700);
        binding.cardViewSurvey7.setTranslationX(-700);
        binding.cardViewSurvey8.setTranslationX(-700);

        binding.cardViewSurvey.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(200).start();
        binding.cardViewSurvey1.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        binding.cardViewSurvey2.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();
        binding.cardViewSurvey4.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        binding.cardViewSurvey5.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(600).start();
        binding.cardViewSurvey7.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        binding.cardViewSurvey8.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(800).start();
        //////////////////////Animation End

        PowerManager powerManager = (PowerManager)
                getActivity().getSystemService(Context.POWER_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && powerManager.isPowerSaveMode()) {
            // Animations are disabled in power save mode, so just show a toast instead.
            batterSaver.setText("enabled");
        }

        if (ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {


            cameraPermission.setText("Camera permission is allowed");
        }


        if (ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {


            storagePermission.setText("Storage permission is allowed");
        }

        if (ContextCompat.checkSelfPermission(_activity, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {


            audio.setText("Audio permission is allowed");
        }


        ////nternal storage sizes
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long blockSize = statFs.getBlockSize();
        long totalSize = statFs.getBlockCount() * blockSize;
        long availableSize = statFs.getAvailableBlocks() * blockSize;
        long freeSize = statFs.getFreeBlocks() * blockSize;

        long megAvailable = availableSize / 1048576;

        Log.d("TAG", "blockSize: " + blockSize + "totalSize" + totalSize + "availableSize" + availableSize + "freeSize" + freeSize);
        Log.d("TAG", "blockSize2: " + megAvailable + "totalSize" + totalSize + "availableSize" + availableSize + "freeSize" + freeSize);

        // spaceInMobile.setText("" + freeSize);


        LocationManager lm = (LocationManager) _activity.getSystemService(Context.LOCATION_SERVICE);
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
       /*     new AlertDialog.Builder(_activity)
                    .setMessage("gps_network_not_enabled")
                    .setPositiveButton("open_location_settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            _activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .show();*/

            gps.setText("location is off");
        }


/*
        if (ActivityCompat.checkSelfPermission(_activity
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gps.setText("permission granted");

        }else {
            ActivityCompat.requestPermissions(_activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }*/

        checkConnection();


        return view;
    }

    public void checkConnection() {

        ConnectivityManager manager = (ConnectivityManager) _activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (null != networkInfo) {

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                network.setText("Yes/WIFI");
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                network.setText("Yes/Data Connection");
            }

        } else {
            network.setText("You are not Connected");
        }
    }
}
