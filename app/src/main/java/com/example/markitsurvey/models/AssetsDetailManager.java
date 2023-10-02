package com.example.markitsurvey.models;

import android.util.Log;

import java.io.File;
import java.util.List;

public class AssetsDetailManager {

    public void getFiles(String id, String path, List<FileDetail> fileDetailList) {
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                getFiles(id, files[i].getPath(), fileDetailList);
            } else {
                FileDetail fileDetail = new FileDetail();
                fileDetail.setFileName(files[i].getName());
                fileDetail.setSize(getFilesize(files[i]));
                fileDetail.setPath(files[i].getPath());
                fileDetail.setFileDate(String.valueOf(files[i].lastModified()));
                fileDetail.setId(id); //Id to maintain record as unique
                fileDetailList.add(fileDetail);
            }
        }
    }

    public double getFilesize(File file) {
        double fileSizeInMB = 0;
        if (file != null) {
            // Get length of file in bytes
            long fileSizeInBytes = file.length();
            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
            long fileSizeInKB = fileSizeInBytes / 1024;
            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            fileSizeInMB = ((double) fileSizeInKB / 1024);
        }
        return fileSizeInMB;

    }

    public void getFilesImagesAudios(String id, String path, List<FileDetail> fileDetailList) {
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                if (!files[i].getPath().equalsIgnoreCase("/storage/emulated/0/.MarkitSurvey/AppLogger")) {
                    getFilesImagesAudios(id, files[i].getPath(), fileDetailList);
                }
            } else {
                FileDetail fileDetail = new FileDetail();
                fileDetail.setFileName(files[i].getName());
                fileDetail.setSize(getFilesize(files[i]));
                fileDetail.setPath(files[i].getPath());
                fileDetail.setFileDate(String.valueOf(files[i].lastModified()));
                fileDetail.setId(id); //Id to maintain record as unique
                fileDetailList.add(fileDetail);
            }
        }
    }

}
