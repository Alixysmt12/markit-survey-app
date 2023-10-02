package com.example.markitsurvey.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.markitsurvey.R;
import com.example.markitsurvey.RecyclerViewClickInterface;
import com.example.markitsurvey.activities.SurveyActivityList;
import com.example.markitsurvey.databinding.RegionItemBinding;
import com.example.markitsurvey.helper.KeyValueDB;
import com.example.markitsurvey.helper.Utility;
import com.example.markitsurvey.models.Region;
import com.google.gson.Gson;

import java.util.List;

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.ViewHolder> {

    Context context;
    List<Region> list;
    KeyValueDB keyValueDB;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public RegionAdapter(Context context, List<Region> list, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.region_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        keyValueDB  = new KeyValueDB(context.getSharedPreferences(context.getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));

        holder.binding.textView9.setText(list.get(position).getSubArea().getName());
        holder.binding.textView10.setText(Utility.DateFormatter(list.get(position).getUpdatedAt()));

        holder.binding.showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  SubArea subArea = (SubArea)view.getTag();
                AppLogger.i("Clicked MAP",new Gson().toJson(subArea));
                Intent i = new Intent(context, ActivityMap.class);
                i.putExtra("subarea",subArea);
                context.startActivity(i);*/

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickInterface.onItemClick(holder.getAdapterPosition(),list);
                /*Region region = list.get(holder.getAdapterPosition());
                keyValueDB.save("region",new Gson().toJson(region));
                Intent intent = new Intent(context, SurveyActivityList.class);
                intent.putExtra("project",list.get(holder.getAdapterPosition()));
                context.startActivity(intent);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RegionItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = RegionItemBinding.bind(itemView);
        }
    }
}
