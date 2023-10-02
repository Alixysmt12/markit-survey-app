package com.example.markitsurvey.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.example.markitsurvey.R;
import com.example.markitsurvey.RecyclerViewClickInterface;
import com.example.markitsurvey.adapters.RegionAdapter;
import com.example.markitsurvey.databinding.ActivityRegionBinding;
import com.example.markitsurvey.helper.KeyValueDB;
import com.example.markitsurvey.logger.AppLogger;
import com.example.markitsurvey.models.ProjectModel;
import com.example.markitsurvey.models.Region;
import com.example.markitsurvey.models.RegionModels;
import com.example.markitsurvey.models.User;
import com.example.markitsurvey.models.UserDecode;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RegionActivity extends AppCompatActivity implements RecyclerViewClickInterface {



    KeyValueDB keyValueDB;
    ProjectModel project;
    UserDecode user;

    RegionAdapter regionAdapter;

    ActivityRegionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        keyValueDB  = new KeyValueDB(getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));
        project = (ProjectModel)getIntent().getSerializableExtra("project");
        getUser();
        renderRegions();

/*
        if (user.getRoleGroup().equalsIgnoreCase(getString(R.string.testRole)))
        {
            Typeface font = Typeface.createFromAsset(getAssets(), "urdufont/Jameel_Noori_Nastaleeq.ttf");
            lblTestIDMsg.setTypeface(font);
            lblTestIDMsg.setVisibility(View.VISIBLE);
        }
*/

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.regRec.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        binding.regRec.setHasFixedSize(true);

    }

    private void getUser()
    {
        user =  UserDecode.ConvertToUserEntityUser(keyValueDB.getValue("token",""));
    }


    private void renderRegions()
    {
        String json = keyValueDB.getValue("user"+user.getId()+"_project"+project.getId()+"_Region","");
        List<Region> regionList = Region.ConvertToRegions(json);
        regionAdapter = new RegionAdapter(this,regionList,this);
        binding.regRec.setAdapter(regionAdapter);
        AppLogger.i(getClass().getName(),"Set Regions to list");
    }


    @Override
    public void onItemClick(int position, List<Region> regionList) {


        Region region = regionList.get(position);
        keyValueDB.save("region",new Gson().toJson(region));
        AppLogger.i("Clicked Regions",new Gson().toJson(region));
        Intent i = new Intent(RegionActivity.this, SurveyActivityList.class);
        i.putExtra("project",project);
        startActivity(i);

    }
}