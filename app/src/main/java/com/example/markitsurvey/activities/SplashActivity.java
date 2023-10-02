package com.example.markitsurvey.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.markitsurvey.R;
import com.example.markitsurvey.helper.KeyValueDB;

public class SplashActivity extends AppCompatActivity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    KeyValueDB keyValueDB;
    TextView lblApplicationVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        lblApplicationVersion = (TextView) findViewById(R.id.lblApplicationVersion);
        lblApplicationVersion.setText("");

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            lblApplicationVersion.setText("App Version " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        keyValueDB = new KeyValueDB(getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                String token = keyValueDB.getValue("token", "");
////                int isRemember = Integer.parseInt(keyValueDB.getValue("isRemember","").isEmpty()?"0":keyValueDB.getValue("isRemember",""));
                if (token.isEmpty()) {
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Intent mainIntent2 = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent2);
                    finish();
                }


            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}