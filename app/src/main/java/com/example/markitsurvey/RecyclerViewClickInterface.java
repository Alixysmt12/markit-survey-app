package com.example.markitsurvey;

import com.example.markitsurvey.models.Region;

import java.util.List;

public interface RecyclerViewClickInterface {

    void onItemClick(int position, List<Region> regionList);
}
