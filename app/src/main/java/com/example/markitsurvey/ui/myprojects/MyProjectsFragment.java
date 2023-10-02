package com.example.markitsurvey.ui.myprojects;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.markitsurvey.R;
import com.example.markitsurvey.activities.MainActivity;
import com.example.markitsurvey.adapters.MyProjectAdapter;
import com.example.markitsurvey.databinding.FragmentMyProjectsBinding;
import com.example.markitsurvey.helper.KeyValueDB;
import com.example.markitsurvey.models.MyProjectModel;
import com.example.markitsurvey.models.ProjectModel;
import com.example.markitsurvey.models.UserModelsResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyProjectsFragment extends Fragment {

    KeyValueDB keyValueDB;
    AppCompatActivity _activity;

    private FragmentMyProjectsBinding binding;
    MyProjectAdapter myProjectAdapter;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof AppCompatActivity) {
            _activity = (AppCompatActivity) context;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMyProjectsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        keyValueDB = new KeyValueDB(_activity.getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));


        binding.recProject.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.recProject.setHasFixedSize(true);

        String json = keyValueDB.getValue("projectJSON", "");




        binding.openNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) getActivity()).openDrawer();

            }
        });

        binding.searchProject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                myProjectAdapter.filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        List<ProjectModel> projectModels = ProjectModel.ConvertToProjectsEntity(json);
        myProjectAdapter = new MyProjectAdapter(getContext(), projectModels);
        binding.recProject.setAdapter(myProjectAdapter);

        return root;
    }

}