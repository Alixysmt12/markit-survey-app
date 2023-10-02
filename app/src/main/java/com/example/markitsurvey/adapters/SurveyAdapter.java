package com.example.markitsurvey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.markitsurvey.R;
import com.example.markitsurvey.databinding.SurveyItemBinding;
import com.example.markitsurvey.models.MyProjectModel;
import com.example.markitsurvey.models.Survey;
import com.example.markitsurvey.models.SurveyModel;

import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {

    Context context;
    List<Survey> list;

    public SurveyAdapter(Context context, List<Survey> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.binding.textView9.setText("Survey No   : " + list.get(position).getId());
        holder.binding.textView10.setText("Start Time   : " + list.get(position).getStartTime());
        holder.binding.textView11.setText("End Time   : " + list.get(position).getEndTime());
        holder.binding.loi.setText("LOI   : " + list.get(position).getLOI());
        holder.binding.subArea.setText("SubArea   : " + list.get(position).getSubAreaName());

        int i;
        for (i= 1; i <= position ;i++){

            holder.binding.size.setText(""+i);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SurveyItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = SurveyItemBinding.bind(itemView);


        }
    }
}
