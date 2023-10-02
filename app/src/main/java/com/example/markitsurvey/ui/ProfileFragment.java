package com.example.markitsurvey.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.markitsurvey.R;
import com.example.markitsurvey.activities.MainActivity;
import com.example.markitsurvey.databinding.FragmentProfileBinding;
import com.example.markitsurvey.ui.myprojects.MyProjectsFragment;

public class ProfileFragment extends Fragment {


    FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) getActivity()).openDrawer();

                ((MainActivity) getActivity()).toolbarHideShow();
                ((MainActivity) getActivity()).hide_Appbar();
            }
        });

        return view;
    }


}