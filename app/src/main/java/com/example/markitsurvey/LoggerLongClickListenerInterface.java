package com.example.markitsurvey;

import com.example.markitsurvey.models.Region;

import java.io.File;
import java.util.List;

public interface LoggerLongClickListenerInterface {

    void onItemClick(int position, List<File> fileList);
}
