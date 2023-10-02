package com.example.markitsurvey.helper;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.markitsurvey.logger.AppLogger;
import com.example.markitsurvey.models.AudioData;
import com.example.markitsurvey.models.Coordinates;
import com.example.markitsurvey.models.KML;
import com.example.markitsurvey.models.Region;
import com.snatik.polygon.Point;
import com.snatik.polygon.Polygon;

import org.osmdroid.util.GeoPoint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class Utility {


    public static boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
        }

        return hasImage;
    }

    public static List<String> getRandomCategories(List<String> stringList, int noOfElements) {
        List<String> randomStringList = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < noOfElements; i++) {
            int randomIndex = rand.nextInt(stringList.size());
            String randomElement = stringList.get(randomIndex);
            stringList.remove(randomIndex);
            randomStringList.add(randomElement);
        }
        return randomStringList;
    }

    public static String TrimText(String value, int numberOfCharacters, String attachText) {
        String remainingText = value;
        int valueCharacters = value.length();
        if (valueCharacters > numberOfCharacters) {
            remainingText = value.substring(0, value.length() - (valueCharacters - numberOfCharacters)) + " " + attachText;
        }

        return remainingText;
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        ;
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1 minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return " " + diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return "" + diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return "" + diff / DAY_MILLIS + " days ago";
        }
    }

    public static boolean getFileExt(String FileName) {
        String ext = FileName.substring((FileName.lastIndexOf(".") + 1), FileName.length());
        ArrayList<String> listOfExtension = new ArrayList<>();

        listOfExtension.add("pdf");
        listOfExtension.add("doc");
        listOfExtension.add("docx");
        listOfExtension.add("ppt");
        listOfExtension.add("pptx");
        listOfExtension.add("xls");
        listOfExtension.add("xlsx");
        if (listOfExtension.contains(ext)) {
            return true;
        }
        return false;

    }


    public static boolean getVideoExtension(String FileName) {
        boolean isVideo = false;
        String ext = FileName.substring((FileName.lastIndexOf(".") + 1), FileName.length());
        if (ext.equalsIgnoreCase("mp4") || ext.equalsIgnoreCase("3gp") || ext.equalsIgnoreCase("mpeg") || ext.equalsIgnoreCase("flv")) {

            isVideo = true;
        }


        return isVideo;
    }

    public static String getFileExtName(String FileName) {
        String ext = FileName.substring((FileName.lastIndexOf(".") + 1), FileName.length());

        return ext;

    }

    public static long getLongTime(String timeDate) {
        String toParse = timeDate; // Results in 2014-10-28 06:39:56
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss"); // I assume d-M, you may refer to M-d for month-day instead.
        Date date = null;
        try {
            date = formatter.parse(toParse);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // You will need try/catch around this
        long millis = date.getTime();

        return millis;
    }

    public static String getFormattedDate(String dateStr) {
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String newDateStr = curFormater.format(dateObj);
        return newDateStr;
    }

    public static String getNewsType(String type) {
        String value = "";
        if (type.equals("1"))
            value = "London";
        else if (type.equals("2"))
            value = "Birmingham";
        else if (type.equals("3"))
            value = "Manchester";
        else if (type.equals("4"))
            value = "Business";
        else if (type.equals("5"))
            value = "Sports";
        else if (type.equals("6"))
            value = "Entertainment";

        return value;

    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    public static void SharLink(Context context, String Subject, String url) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, Subject);
        i.putExtra(Intent.EXTRA_TEXT, url);
        context.startActivity(Intent.createChooser(i, "Share Link"));
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static long getFilesize(String fileName) {
        File file = new File(fileName);
        // Get length of file in bytes
        long fileSizeInBytes = file.length();
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        long fileSizeInMB = fileSizeInKB / 1024;
        return fileSizeInMB;
    }

    public static double getFilesize(File file) {
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

    public class ImageCompression extends AsyncTask<String, Void, String> {

        private Context context;
        private static final float maxHeight = 1280.0f;
        private static final float maxWidth = 1280.0f;


        public ImageCompression(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0 || strings[0] == null)
                return null;

            return compressImage(strings[0], strings[1], strings[2]);
        }

        protected void onPostExecute(String imagePath) {
            // imagePath is path of new compressed image.
        }


        public String compressImage(String imagePath, String folderPath, String fileName) {
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

            float imgRatio = (float) actualWidth / (float) actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(imagePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

            if (bmp != null) {
                bmp.recycle();
            }

            ExifInterface exif;
            try {
                exif = new ExifInterface(imagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out = null;
            String filepath = getFilename(folderPath, fileName);
            try {
                out = new FileOutputStream(filepath);

                //write the compressed bitmap at the destination specified by filename.
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return filepath;
        }

        public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }

            return inSampleSize;
        }

        public String getFilename(String folderPath, String fileName) {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                    + folderPath);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }

            String mImageName = fileName;
            String uriString = (mediaStorageDir.getAbsolutePath() + "/" + mImageName);
            ;
            return uriString;

        }

    }

    public static void createDirectory(String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + fileName);

        if (!direct.exists()) {
            File wallpaperDirectory = new File(fileName);
            wallpaperDirectory.mkdirs();
        }


    }

    public static File getFile(String folderName, String Path) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/.MarkitSurvey/" + folderName);
        File file = new File(direct + "/" + Path + "");
        if (file.exists()) {

            return file;
        }

        return file;
    }

    public static String getFileName(String Path) {
        File file = new File(Path);

        if (file.exists()) {

            return file.getName();
        }

        return "";
    }

    public static boolean checkFileIsAvailable(String folderName, String Path) {
        //File extStore = Environment.getExternalStorageDirectory();
        File direct = new File(Environment.getExternalStorageDirectory() + "/.RetailAudit/" + folderName);
        File myFile = new File(direct + "/" + Path + "");

        if (myFile.exists()) {

            return true;
        }

        return false;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }


    public static int getAudioFileDuration(Context context, String pathStr) {
        Uri uri = Uri.parse(pathStr);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context, uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int millSecond = Integer.parseInt(durationStr);
        return millSecond;
    }

    public static File createDirectoryAndSaveAudio(String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/.MarkitSurvey/Audio/");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/.MarkitSurvey/Audio/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/.MarkitSurvey/Audio/"), fileName + ".mp3");
        if (file.exists()) {
            file.delete();
        }

        return file;
    }

    public static File createDirectoryAndSaveAudio(String fileName, String folderName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/.MarkitSurvey/" + folderName + "/Audio/");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/.MarkitSurvey/" + folderName + "/Audio/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/.MarkitSurvey/" + folderName + "/Audio/"), fileName + ".txt");
        if (file.exists()) {
            file.delete();
        }

        return file;
    }


    public static File createImageFile() throws IOException {
        String abc = System.getProperty("java.io.tempdir");

        String imageFileName = null;
        try {
            imageFileName = Utility.createTransactionID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        //	storageDir.getParentFile().mkdirs();
        return File.createTempFile(imageFileName, ".jpg", storageDir);

    }


    public static String createTransactionID() throws Exception {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    public static void exportDatabase(Context context, int userId) {
        try {

            File sd = new File(Environment.getExternalStorageDirectory() + "/.MarkitSurvey/DBBackup/"); //Environment.getExternalStorageDirectory();
            if (!sd.exists()) {
                File wallpaperDirectory = new File("/sdcard/.MarkitSurvey/DBBackup/");
                wallpaperDirectory.mkdirs();
            }
            File data = Environment.getDataDirectory();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar c = Calendar.getInstance();
            String date = sdf.format(c.getTime());
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + context.getPackageName() + "//databases//Markit_V2_db";
                String backupDBPath = "Markit_V2_db_" + System.currentTimeMillis() + "_" + userId + ".db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            AppLogger.i("DB Export Exception", e.getMessage());
        }
    }

    public static String getRealPathFromURI(Uri contentUri, Context context) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static Bitmap rotateBitmapOrientation(String photoFilePath) {

        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }

    public static String imageFolderPath(String folderName) {
        folderName = folderName.replaceAll("\\s+", "");
        String folderPath = "/.MarkitSurvey/" + folderName + "/Images/";
        AppLogger.i("Image-Folder", folderPath);
        return folderPath;
    }


    public static String DateFormatter(String strDate) {
        String formattedDate = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(strDate, inputFormatter);
            formattedDate = outputFormatter.format(date);
        } else {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = null;
            try {
                date = fmt.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
            formattedDate = fmtOut.format(date);

        }


        return formattedDate;
    }

    public static long getLOI(String date1, String date2) {
        long LOI = 0;
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        try {
            long diff = df.parse(date1).getTime() - df.parse(date2).getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            LOI = minutes;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return LOI;
    }


    public static String createDirectoryAndSaveFile(String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/.MarkitSurvey/Audio/");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/.MarkitSurvey/Audio/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/.MarkitSurvey/Audio/"), fileName + ".mp3");
        if (file.exists()) {
            file.delete();
        }

        return file.getPath();
    }

    /*    public static void convertToAudio(final Context context, final AudioData audioData, String folderName)
        {

            //Utilities.log("~~~~~~~~ Encoded: ", encoded);

            //byte[] decoded = Base64.decode(audioData.getValue().getBytes(), 0);
            //Utilities.log("~~~~~~~~ Decoded: ", Arrays.toString(decoded));

            try
            {
                folderName = folderName.replaceAll("\\s+","");
                //File file2 = new File(Environment.getExternalStorageDirectory() + "/hello-5.wav");
                File file2 = createDirectoryAndSaveAudio(audioData.getName(),folderName);
                FileOutputStream fis = new FileOutputStream (file2);
                writeToFile(audioData.getValue(),context,fis);
    //			FileOutputStream os = new FileOutputStream(file2, true);
    //			os.write(decoded);
    //	//		os.flush();
    //			os.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }*/
    private static void writeToFile(String data, Context context, FileOutputStream fis) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fis);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(data);
            bufferedWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public static void getPermissionList(Context context) {

        PackageManager pm = context.getPackageManager();
        CharSequence csPermissionGroupLabel;
        CharSequence csPermissionLabel;

        List<PermissionGroupInfo> lstGroups = pm.getAllPermissionGroups(0);
        for (PermissionGroupInfo pgi : lstGroups) {
            csPermissionGroupLabel = pgi.loadLabel(pm);
            Log.e("perm", pgi.name + ": " + csPermissionGroupLabel.toString());
            AppLogger.i("perm", pgi.name + ": " + csPermissionGroupLabel.toString());
            try {
                List<PermissionInfo> lstPermissions = pm.queryPermissionsByGroup(pgi.name, 0);
                for (PermissionInfo pi : lstPermissions) {
                    csPermissionLabel = pi.loadLabel(pm);
                    AppLogger.i("perm", "   " + pi.name + ": " + csPermissionLabel.toString());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    public static boolean checkShopLocation(Region region, double lng, double lat) {
        boolean isLocationCorrect = false;
        KML kml = KML.getKML(region.getSubArea().getCordinates());
        Polygon.Builder polygonBuilder = new Polygon.Builder();
        List<GeoPoint> geoPoints = getGeoPoints(kml);
        for (int i = 0; i < geoPoints.size(); i++) {
            GeoPoint geoPoint = geoPoints.get(i);
            polygonBuilder.addVertex(new Point(geoPoint.getLongitude(), geoPoint.getLatitude()));
        }
        Polygon polygon = polygonBuilder.build();

        isLocationCorrect = polygon.contains(new Point(lng, lat));
        return isLocationCorrect;
    }


    private static List<GeoPoint> getGeoPoints(KML kml) {

        List<Coordinates> coordinatesList = kml.getBoundariesSet().get(0).getCoordinates();
        List<GeoPoint> pts = new ArrayList<>();
        for (int i = 0; i < coordinatesList.size(); i++) {
            Coordinates coordinates = coordinatesList.get(i);
            pts.add(new GeoPoint(coordinates.getLat(), coordinates.getLng()));
        }
        //  endPoint  = new GeoPoint(pts.get(0).getLatitude(), pts.get(0).getLongitude());
        return pts;

    }


    public static void convertToAudio(final Context context, final AudioData audioData, String folderName)
    {

        //Utilities.log("~~~~~~~~ Encoded: ", encoded);

        //byte[] decoded = Base64.decode(audioData.getValue().getBytes(), 0);
        //Utilities.log("~~~~~~~~ Decoded: ", Arrays.toString(decoded));

        try
        {
            folderName = folderName.replaceAll("\\s+","");
            //File file2 = new File(Environment.getExternalStorageDirectory() + "/hello-5.wav");
            File file2 = createDirectoryAndSaveAudio(audioData.getName(),folderName);
            FileOutputStream fis = new FileOutputStream (file2);
            writeToFile(audioData.getValue(),context,fis);
//			FileOutputStream os = new FileOutputStream(file2, true);
//			os.write(decoded);
//	//		os.flush();
//			os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}