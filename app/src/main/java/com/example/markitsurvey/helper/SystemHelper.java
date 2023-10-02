package com.example.markitsurvey.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.example.markitsurvey.models.SystemInfo;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class SystemHelper {


    public static SystemInfo getSystemInformation(Activity context) {
        SystemInfo systemInfo = new SystemInfo();
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int mHeightPixels = dm.heightPixels;
        int mWidthPixels = dm.widthPixels;
        double x = Math.pow(mWidthPixels / dm.xdpi, 2);
        double y = Math.pow(mHeightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);

        int densityDpi = (int) (dm.density * 160f);


        final TelephonyManager tm = (TelephonyManager) context.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        //  tmDevice = "" + tm.getDeviceId();
        // tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID androidId_UUID = null;
        try {
            androidId_UUID = UUID
                    .nameUUIDFromBytes(androidId.getBytes("utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String unique_id = androidId_UUID.toString();
        // UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = unique_id.toString();

        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String ScreenResolution = mHeightPixels + " * " + mWidthPixels + " Pixels";

        systemInfo.setManufacturer(Build.MANUFACTURER);
        systemInfo.setBrand(Build.BRAND);
        systemInfo.setModel(Build.MODEL);
        systemInfo.setBoard(Build.BOARD);
        systemInfo.setHardware(Build.HARDWARE);
        systemInfo.setSerialNo(Build.SERIAL);
        systemInfo.setDeviceId(deviceId);
        systemInfo.setAndroidID(androidID);
        systemInfo.setScreenResolution(ScreenResolution);
        systemInfo.setScreenSize(screenInches + " Inches");
        systemInfo.setScreenDensity(String.valueOf(densityDpi) + " dpi");
        systemInfo.setBootLoader(Build.BOOTLOADER);
        systemInfo.setUser(Build.USER);
        systemInfo.setHost(Build.HOST);
        systemInfo.setVersion(Build.VERSION.RELEASE);
        systemInfo.setAPILevel(Build.VERSION.SDK_INT + "");
        systemInfo.setBuildID(Build.ID);
        systemInfo.setBuildTime(Build.TIME + "");
        systemInfo.setFingerprint(Build.FINGERPRINT);
        systemInfo.setExternalMemoryIsAvailable(externalMemoryAvailable() == true ? "Yes" : "No");
        if (externalMemoryAvailable()) {
            systemInfo.setExternalMemory(getAvailableExternalMemorySize());
        }
        systemInfo.setInternalMemory(getTotalInternalMemorySize());
        systemInfo.setRAMInfo(getRamInfo(context));
        systemInfo.setVersionRelease(Build.VERSION.RELEASE);
        return systemInfo;
    }


    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return formatSize(availableBlocks * blockSize);
    }

    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return formatSize(totalBlocks * blockSize);
    }

    public static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return formatSize(availableBlocks * blockSize);
        } else {
            return "ERROR";
        }
    }

    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return formatSize(totalBlocks * blockSize);
        } else {
            return "ERROR";
        }
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public static String getRamInfo(Activity context) {
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem;
        return String.valueOf(totalMemory);
    }


}
