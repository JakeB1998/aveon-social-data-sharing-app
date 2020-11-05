package com.example.madcompetition;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.madcompetition.activties.LoginScreenActivity;
import com.example.madcompetition.backend.AppManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class LocationManager extends Service {
    private static final LocationManager ourInstance = new LocationManager();
    private FusedLocationProviderClient fusedLocationClient;

    private Activity currentActivity;
    Timer t = new Timer();

    public static LocationManager getInstance() {


        return ourInstance;
    }

    private LocationCallback locationCallback;

    public static final int LOCATION_REQUEST_CODE = 1;
    public static final int LOCATION_FINE_REQUEST_CODE = 2;

    private LocationManager locationManager;

    private LocationManager()
    {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startLocationUpdates(final Activity activity)
    {

        Log.i("Location", "Update Startyed");
    currentActivity = activity;
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.i("Location", "Error in locationManager");
                    return;
                }
                Geocoder geo =  new Geocoder(activity.getApplicationContext(), Locale.getDefault());
                AppManager.getInstance().locationsFromLastUpdate = locationResult.getLastLocation();

                for (Location location : locationResult.getLocations()) {
                    try
                    {
                        Log.i("Location", "adress :  " + geo.getFromLocation(location.getLatitude(), location.getLongitude(),1).get(0).getAddressLine(0));
                    } catch (IOException e)
                    {
                        Log.i("Error", e.getMessage() +  "From : " + LoginScreenActivity.class.getName());
                        e.printStackTrace();
                    }
                    Log.i("Location", location.toString());
                }
                Log.i("Location", "Location Updated");
            }
        };

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(4000);
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());

        //startUpdtae();
        Log.i("Location", "Location Updates started");
    }



    private void startUpdtae()
    {

        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (currentActivity != null) {
                    LocationManager.this.startLocationUpdates(currentActivity);
                }
                else
                {
                    this.cancel();
                }
            }

        }, 0, 1000);
    }


    private void getLastLocation(final Context context, Activity activity)
    {
         Location locationNew = null;
        boolean permissionAccessCoarseLocationApproved =
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        // App doesn't have access to the device's location at all. Make full request
        // for permission.
        if (permissionAccessCoarseLocationApproved)
        {
            boolean backgroundLocationPermissionApproved =
                    ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            == PackageManager.PERMISSION_GRANTED;

            if (backgroundLocationPermissionApproved)
            {

            }
            else
            {

                ActivityCompat.requestPermissions(activity, new String[]{
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        LocationManager.LOCATION_REQUEST_CODE);
            }
        }
        else ActivityCompat.requestPermissions(activity, new String[]
                        {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        },
                69);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {







                        }
                        else {

                            Log.i("Location", "Location is null" + "From : " + LoginScreenActivity.class.getName());
                        }
                    }
                });




    }


}
