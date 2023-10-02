package com.example.markitsurvey.ui.logger;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.markitsurvey.LoggerLongClickListenerInterface;
import com.example.markitsurvey.activities.MainActivity;
import com.example.markitsurvey.adapters.LogListAdapter;
import com.example.markitsurvey.databinding.FragmentSlideshowBinding;

import java.io.File;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LogFileFragment extends Fragment implements LoggerLongClickListenerInterface {

    LogListAdapter logListAdapter;
    AppCompatActivity _activity;

    private FragmentSlideshowBinding binding;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof AppCompatActivity) {
            _activity = (AppCompatActivity) context;
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.recLog.setLayoutManager(new LinearLayoutManager(_activity, RecyclerView.VERTICAL,false));
        binding.recLog.setHasFixedSize(true);

        binding.openNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) getActivity()).openDrawer();

            }
        });


        renderListView();
        return root;
    }

    private void renderListView() {


        List<File> logsFiles = getLogsFile();
        Collections.sort(logsFiles, new Comparator<File>() {

            @Override
            public int compare(File file1, File file2) {
                long k = file1.lastModified() - file2.lastModified();
                if (k > 0) {
                    return 1;
                } else if (k == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        logListAdapter = new LogListAdapter(_activity, logsFiles,this);
        binding.recLog.setAdapter(logListAdapter);
        binding.lblCount.setText("Total Log File: " + logsFiles.size());

    }

    private List<File> getLogsFile() {
        String dirPath = Environment.getExternalStorageDirectory().toString() + "/.MarkitSurvey/AppLogger";
        File dir = new File(dirPath);
        File[] filelist = dir.listFiles();
        if (filelist != null) {
            return Arrays.asList(filelist.clone());
        }
        return null;
    }

    @Override
    public void onItemClick(int position, List<File> fileList) {

        //File file = (File) parent.getItemAtPosition(position);
        File file = fileList.get(position);
        Uri uri = FileProvider.getUriForFile(
                _activity,
                "com.example.markitsurvey.provider", //(use your app signature + ".provider" )
                file);
        ShareCompat.IntentBuilder.from(_activity)
                .setStream(uri)
                .setType(URLConnection.guessContentTypeFromName(file.getName()))
                .startChooser();


    }
}