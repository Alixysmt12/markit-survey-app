package com.example.markitsurvey.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.markitsurvey.LoggerLongClickListenerInterface;
import com.example.markitsurvey.R;
import com.example.markitsurvey.databinding.LogFileItemBinding;
import com.example.markitsurvey.helper.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogListAdapter extends RecyclerView.Adapter<LogListAdapter.ViewHolder> {


    Context context;
    List<File> fileList;
    List<File> filteredItemsList;

    private LoggerLongClickListenerInterface loggerLongClickListenerInterface;

    public LogListAdapter(Context context, List<File> fileList,LoggerLongClickListenerInterface loggerLongClickListenerInterface) {
        this.context = context;
        this.fileList = fileList;
        this.filteredItemsList = new ArrayList<File>();
        this.filteredItemsList.addAll(this.fileList);
        this.loggerLongClickListenerInterface = loggerLongClickListenerInterface;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.log_file_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Date lastModDate = new Date(fileList.get(position).lastModified());
        //Utility.DateFormatter(lastModDate.toString());
        holder.binding.logFileName.setText("Name  : " + fileList.get(position).getName());
        holder.binding.createdTime.setText("Created Time  : " + lastModDate.toString());
        holder.binding.logSize.setText("Size: " + Utility.getFilesize(fileList.get(position)));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                loggerLongClickListenerInterface.onItemClick(position,fileList);


                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LogFileItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = LogFileItemBinding.bind(itemView);
        }
    }

    public void filter(CharSequence charText) {
        charText = charText.toString().toLowerCase(Locale.getDefault());
        fileList.clear();
        if (charText.length() == 0) {
            fileList.addAll(filteredItemsList);
        } else {
            for (File c : filteredItemsList) {
                String match = c.getName().toLowerCase();
                if (match.contains(charText)) {
                    fileList.add(c);
                }
            }
        }
        notifyDataSetChanged();
    }

}
