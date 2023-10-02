package com.example.markitsurvey.handlingTime;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.markitsurvey.R;
import com.example.markitsurvey.helper.KeyValueDB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GPSTime {

    boolean isDateCorrect = false;
    KeyValueDB keyValueDB;

    @SuppressLint("MissingPermission")
    public boolean InitialiseLocationListener(Context context) {
        keyValueDB = new KeyValueDB(context.getSharedPreferences(context.getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));
        android.location.LocationManager locationManager = (android.location.LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        android.location.LocationListener locationListener = new android.location.LocationListener() {

            public void onLocationChanged(android.location.Location location) {

                //String time = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").format(location.getTime());
                String time = new SimpleDateFormat("dd/MM/yyyy").format(location.getTime());

                if (location.getProvider().equals(android.location.LocationManager.GPS_PROVIDER)) {
                    android.util.Log.d("Location", "Time GPS: " + time); // This is what we want!
                    //timeDate.setText(String.valueOf(time));
                    //Toast.makeText(context, "" + time, Toast.LENGTH_SHORT).show();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());
                    //systemDateTime.setText(currentDateandTime);

                    if (time.equals(currentDateandTime)){

                        isDateCorrect = true;
                        keyValueDB.save("isDateCorrect",String.valueOf(isDateCorrect));
                        /*((ActivityLogin) context).btnLogin.setEnabled(false);

                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Your Date is incorrect!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        //Toast.makeText(context, "date is incorrect", Toast.LENGTH_SHORT).show();
*/
                    }


                }
//                else
//                    android.util.Log.d("Location", "Time Device (" + location.getProvider() + "): " + time);
            }

            public void onStatusChanged(String provider, int status, android.os.Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

/*        if (ActivityCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            android.util.Log.d("Location", "Incorrect 'uses-permission', requires 'ACCESS_FINE_LOCATION'");
            return false;
        }*/

        locationManager.requestLocationUpdates(android.location.LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
        locationManager.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        // Note: To Stop listening use: locationManager.removeUpdates(locationListener)

        return isDateCorrect;
    }

}

