package com.example.markitsurvey.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.markitsurvey.R;
import com.example.markitsurvey.activities.ProjectDetailedActivity;
import com.example.markitsurvey.databinding.ProjectsItemBinding;
import com.example.markitsurvey.helper.Utility;
import com.example.markitsurvey.models.ProjectModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyProjectAdapter extends RecyclerView.Adapter<MyProjectAdapter.ViewHolder> {

    Context context;
    List<ProjectModel> list;
    List<ProjectModel> filterList;

    public MyProjectAdapter(Context context, List<ProjectModel> list) {
        this.context = context;
        this.list = list;
        this.filterList = new ArrayList<>();
        this.filterList.addAll(this.list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.projects_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.binding.heading.setText(list.get(position).getProjectName());
        holder.binding.dateTime.setText(Utility.DateFormatter(list.get(position).getCreatedAt()));

        holder.binding.createdEmail.setText("Created By: " + list.get(position).getUsers().get(0).getUserName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_click));
                Intent intent = new Intent(context, ProjectDetailedActivity.class);
                intent.putExtra("projectData",list.get(holder.getAdapterPosition()));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ProjectsItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ProjectsItemBinding.bind(itemView);
        }
    }

    public void filter(CharSequence charText) {
        charText = charText.toString().toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(filterList);
        } else {
            for (ProjectModel c : filterList) {
                String match = c.getProjectName().toLowerCase();
                if (match.contains(charText)) {
                    list.add(c);
                }
            }
        }
        notifyDataSetChanged();
    }
}
