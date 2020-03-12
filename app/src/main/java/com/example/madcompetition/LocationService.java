package com.example.madcompetition;

import android.app.Service;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.example.madcompetition.Activties.LoginScreenActivity;
import com.example.madcompetition.BackEnd.AppManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.Locale;

public class LocationService extends Service {

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    @Override
    public void onCreate() {

    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLocationUpdates();
       return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }




    public void startLocationUpdates()
    {

        Log.i("Location", "Update Startyed");

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.i("Location", "Error in locationManager");
                    return;
                }
                Geocoder geo =  new Geocoder(getApplicationContext(), Locale.getDefault());
                AppManager.getInstance().locationsFromLastUpdate = locationResult.getLastLocation();

                for (Location location : locationResult.getLocations()) {
                    try
                    {
                        Log.i("Location", "adress :  " + geo.getFromLocation(location.getLatitude(), location.getLongitude(),1).get(0).getAddressLine(0).toString());
                    } catch (IOException e)
                    {
                        Log.i("Error", e.getMessage().toString() +  "From : " + LoginScreenActivity.class.getName());
                        e.printStackTrace();
                    }
                    Log.i("Location", location.toString());
                }
                Log.i("Location", "Location Updated");
            };
        };

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(4000);
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());

        //startUpdtae();
        Log.i("Location", "Location Updates started");
    }


}
