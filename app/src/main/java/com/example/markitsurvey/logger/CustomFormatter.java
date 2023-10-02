package com.example.markitsurvey.logger;

import android.os.Process;

import com.bosphere.filelogger.FileFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomFormatter implements FileFormatter {

    private String id = "";
    public CustomFormatter(String uniqueId){
        id = uniqueId;
    }
    private final ThreadLocal<SimpleDateFormat> mTimeFmt = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        }
    };

    private final ThreadLocal<SimpleDateFormat> mFileNameFmt = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM_dd_HH", Locale.ENGLISH);
        }
    };

    private final ThreadLocal<Date> mDate = new ThreadLocal<Date>() {
        @Override
        protected Date initialValue() {
            return new Date();
        }
    };

    // 09-23 12:31:53.839 PROCESS_ID-THREAD_ID LEVEL/TAG: LOG
    private final String mLineFmt = "%s %d-%d %s/%s: %s";

    @Override
    public String formatLine(long timeInMillis, String level, String tag, String log) {
        mDate.get().setTime(timeInMillis);
        String timestamp = mTimeFmt.get().format(mDate.get());
        int processId = Process.myPid();
        int threadId = Process.myTid();
        return String.format(Locale.ENGLISH, mLineFmt, timestamp, processId, threadId, level,
                tag, log);
    }

    @Override
    public String formatFileName(long timeInMillis) {
        mDate.get().setTime(timeInMillis);
        //return mFileNameFmt.get().format(mDate.get().getTime())+ "_00.txt";
        return mFileNameFmt.get().format(mDate.get().getTime())+ "_"+id +".txt";
        //return timeInMillis+ "_00.txt";
    }

}
