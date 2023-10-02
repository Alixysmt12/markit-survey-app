package com.example.markitsurvey.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.markitsurvey.R;
import com.example.markitsurvey.activities.MainActivity;
import com.example.markitsurvey.databinding.FragmentSettingBinding;
import com.example.markitsurvey.helper.KeyValueDB;

import java.util.Arrays;

public class SettingFragment extends Fragment {


    FragmentSettingBinding binding;
    AppCompatActivity _activity;
    Switch textAlignmentASwitch,fontASwitch,switchLocation;
    KeyValueDB keyValueDB;
    Spinner spinnerFontSize;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof AppCompatActivity){
            _activity=(AppCompatActivity) context;
        }
    }


    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        binding = FragmentSettingBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        textAlignmentASwitch = (Switch)root.findViewById(R.id.switchRightAlignment);
        fontASwitch = (Switch)root.findViewById(R.id.switchFont);
        spinnerFontSize = (Spinner)root.findViewById(R.id.spinnerFontSize);
        switchLocation = (Switch)root.findViewById(R.id.switchLocation);
        keyValueDB = new KeyValueDB(_activity.getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));

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
        binding.cardViewSurvey3.setTranslationX(-700);

        binding.cardViewSurvey.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(200).start();
        binding.cardViewSurvey1.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        binding.cardViewSurvey2.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();
        binding.cardViewSurvey3.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();

        String[] fontSizes = getResources().getStringArray(R.array.arrayFontSize);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(_activity, R.layout.spinner_item,R.id.lblName, fontSizes);//selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerFontSize.setAdapter(spinnerArrayAdapter);
        String fontSize =  keyValueDB.getValue("fontSize","").isEmpty()?"14": keyValueDB.getValue("fontSize","");
        int index =  Arrays.asList(fontSizes).indexOf(fontSize);
        spinnerFontSize.setSelection(index);
        if (Boolean.parseBoolean(keyValueDB.getValue("textAlignment", "")))
        {
            textAlignmentASwitch.setChecked(true);
        }
        else
        {
            textAlignmentASwitch.setChecked(false);
        }
        if (Boolean.parseBoolean(keyValueDB.getValue("urduFont", "")))
        {
            fontASwitch.setChecked(true);
        }
        else
        {
            fontASwitch.setChecked(false);
        }

        if (Boolean.parseBoolean(keyValueDB.getValue("googleLocation", "").isEmpty()?"false":keyValueDB.getValue("googleLocation", "")))
        {
            switchLocation.setChecked(true);
        }
        else
        {
            switchLocation.setChecked(false);
        }
        textAlignmentASwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    keyValueDB.save("textAlignment", "true");
                }
                else
                {
                    keyValueDB.save("textAlignment", "false");
                }
            }
        });
        fontASwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    keyValueDB.save("urduFont", "true");
                }
                else
                {
                    keyValueDB.save("urduFont", "false");
                }
            }
        });

        spinnerFontSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    keyValueDB.save("fontSize", (String) parent.getItemAtPosition(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    keyValueDB.save("googleLocation", "true");
                }
                else
                {
                    keyValueDB.save("googleLocation", "false");
                }
            }
        });

        return root;
    }
}