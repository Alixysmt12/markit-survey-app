package com.example.markitsurvey.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.example.markitsurvey.R;
import com.example.markitsurvey.helper.KeyValueDB;
import com.example.markitsurvey.helper.Utility;
import com.example.markitsurvey.models.Coordinates;
import com.example.markitsurvey.models.KML;
import com.example.markitsurvey.models.Region;
import com.example.markitsurvey.models.Survey;
import com.example.markitsurvey.models.UserDecode;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityUserLocationMap extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    KeyValueDB keyValueDB;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    boolean isLocationRequest = false;
    double longitudeBest, latitudeBest;
    Region region;
    UserDecode user;
    LatLng latLng;
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location_map);


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        keyValueDB = new KeyValueDB(getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));
        region = Region.ConvertToRegionEntity(keyValueDB.getValue("region", ""));
        latitudeBest = Double.parseDouble(keyValueDB.getValue("lat", ""));
        longitudeBest = Double.parseDouble(keyValueDB.getValue("lng", ""));
        getUser();
        initialize();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);

        String json = keyValueDB.getValue("user" + user.getId() + "_region" + region.getId() + "_surveys", "");
        List<Survey> surveyList = Survey.ConvertToEntity(json);
        KML kml = KML.getKML(region.getSubArea().getCordinates());
        List<LatLng> latLngList = new ArrayList<>();
        LatLngBounds.Builder latLngBounds = LatLngBounds.builder();
        for (int i = 0; i < kml.getBoundariesSet().get(0).getCoordinates().size(); i++) {
            Coordinates coordinates = kml.getBoundariesSet().get(0).getCoordinates().get(i);
            latLngList.add(new LatLng(coordinates.getLat(), coordinates.getLng()));

            latLngBounds.include(new LatLng(coordinates.getLat(), coordinates.getLng()));
            latLngBounds.build();
        }
        Polyline polyline = map.addPolyline(new PolylineOptions()
                .addAll(latLngList));
        polyline.setEndCap(new RoundCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(COLOR_BLACK_ARGB);
        polyline.setJointType(JointType.ROUND);
//        List<Shop> filteredShopList = new ArrayList<>();
//        for (int i = 0; i < shopList.size(); i++) {
//            double v = DistanceCalculator.greatCircleInKilometers(Double.parseDouble(keyValueDB.getValue("lat", "")), Double.parseDouble(keyValueDB.getValue("lng", "")),
//                    shopList.get(i).getLat(), shopList.get(i).getLng());
//
//            if (v <= 0.5) {
//                filteredShopList.add(shopList.get(i));
//            }
//
//        }


        if (surveyList != null) {
            for (int i = 0; i < surveyList.size(); i++) {


                Marker marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(surveyList.get(i).getLat(), surveyList.get(i).getLng()))
                        .title(surveyList.get(i).getGuid())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                marker.setTag(surveyList.get(i));


            }
        }
        if (latLngList.size() != 0) {
            latLng = latLngBounds.build().getCenter();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
        List<LatLng> path = new ArrayList();


        //Execute Directions API request


        //    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zaragoza, 6));


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Survey survey = (Survey) marker.getTag();
                if (survey != null) {
                    if (Utility.checkShopLocation(region, longitudeBest, latitudeBest)) {
                        // Utility.getRandomCategories(CategoryHandler.getSelectedCategoriesInQuestionnaire(survey.getAnswerJSON()),3);

                    } else {
                        Toast.makeText(ActivityUserLocationMap.this, "You are out of boundary", Toast.LENGTH_SHORT).show();
                    }
                }
//                Intent i = new Intent(ActivityMap.this, ActivityShopDetails.class);
//                i.putExtra("shop", shop);
//                startActivity(i);
                return false;
            }
        });

    }

    private void getUser() {
        user = UserDecode.ConvertToUserEntityUser(keyValueDB.getValue("token", ""));
    }


    private void initialize() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        isLocationPermissionGranted();
    }

    private void isLocationPermissionGranted() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission

        } else {
            // already permission granted
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
//                        latitudeBest =  location.getLatitude();
//                        longitudeBest = location.getLongitude();
                        Toast.makeText(ActivityUserLocationMap.this, "Last Location : Lat" + location.getLatitude() + " Lng: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                        getCurrentLocationRequest();
                    } else {
                        getCurrentLocationRequest();
                    }
                }
            });
        }
    }

    private void getCurrentLocationRequest() {
        isLocationRequest = true;
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setSmallestDisplacement(0.0f);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {

                        latitudeBest = location.getLatitude();
                        longitudeBest = location.getLongitude();
//                            keyValueDB.save("lat",String.valueOf(latitudeBest));
//                            keyValueDB.save("lng",String.valueOf(longitudeBest));
//                            keyValueDB.save("acc",String.valueOf(location.getAccuracy()));

                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(latitudeBest, longitudeBest))
                                .title("Me")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        //     getDestinationInfo(latLng);
                        mFusedLocationClient.removeLocationUpdates(locationCallback);


                    }
                }
            }

        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

    }

    @Override
    public void onResume() {
        mapFragment.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapFragment.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }

}