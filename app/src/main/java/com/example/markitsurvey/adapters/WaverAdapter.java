package com.example.markitsurvey.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.markitsurvey.R;
import com.example.markitsurvey.models.ProjectWaveSetting;

public class WaverAdapter extends ArrayAdapter<ProjectWaveSetting> {
    private Context context;
    // Your custom values for the spinner (User)
    private ProjectWaveSetting[] values;
    private LayoutInflater mInflater;

    public WaverAdapter(Context context, int textViewResourceId, ProjectWaveSetting[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount(){
        return values.length;
    }

    public ProjectWaveSetting getItem(int position){
        return values[position];
    }

    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);

        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)

        label.setText(values[position].getWaveName());


        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.spinner_item,parent, false);
        TextView lblName = (TextView)convertView.findViewById(R.id.lblName);

        lblName.setText(values[position].getWaveName());

        return convertView;
    }
}

