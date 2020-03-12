package com.example.madcompetition.Activties;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.Profile;
import com.example.madcompetition.ProfileInfoWindowMap;
import com.example.madcompetition.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ActivityUserMap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker currentLocationMarker;
    private Circle currentLocationCircle;
    private ArrayList<Marker> myMarkers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myMarkers = new ArrayList<>(0);
        setContentView(R.layout.activity_user_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);



        // Add a marker in Sydney and move the camera
        Location m = AppManager.getInstance().locationsFromLastUpdate;
        LatLng x = new LatLng(m.getLatitude(),m.getLongitude());
        LatLng sydney = new LatLng(-34, 151);


        addMarker(sydney);
        addMarker(new LatLng(41.88425, -87.63245));
        addMarker(new LatLng(40.514777, -88.952473));

       currentLocationMarker =  addMarker(x, "My locatiion");
        CircleOptions circleOptions = new CircleOptions()
                .center(currentLocationMarker.getPosition())
                .radius(50)
                .strokeWidth(2)
                .strokeColor(Color.BLUE)
                .fillColor(Color.parseColor("#500084d3")
                );


       currentLocationCircle=  mMap.addCircle(circleOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(x));

        CameraPosition camPos = new CameraPosition.Builder()
                .target(x)
                .zoom(18)
                .bearing(m.getBearing())
                .tilt(70)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);

        mMap.setInfoWindowAdapter(new ProfileInfoWindowMap(this));



        updateMap();
    }

    public Marker addMarker(LatLng latLng)
    {
        MarkerOptions marker = new MarkerOptions()
                .position(latLng).title("")
                .snippet("Username")
                .rotation((float) 3.5);


        marker.icon(null);
        marker.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.getResources(),R.mipmap.default_profile_picture)));



        Marker m =  mMap.addMarker(marker);
        m.setTag(new Profile());
       return m;

    }
    public Marker addMarker(LatLng latLng, String title)
    {
        MarkerOptions marker = new MarkerOptions()
                .position(latLng).title("")
                .snippet("Username")
                .rotation((float) 3.5);

        marker.icon(null);
        marker.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.getResources(),R.mipmap.default_profile_picture)));




        Marker m =  mMap.addMarker(marker);
        m.setTag(new Profile());
        m.hideInfoWindow();

        return m;


    }



    public void updateMap()
    {
        Log.i("Map", "Updated");

        Location newLocation = AppManager.getInstance().locationsFromLastUpdate;
       LatLng newLatLng =  new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
        if (currentLocationMarker.getPosition().equals(newLatLng))
        {
            currentLocationMarker.remove();
            currentLocationMarker = null;
            currentLocationMarker = addMarker(newLatLng);
            Log.i("Map", "Current location marker has been moved");



        }

        Thread update = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(3000);
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {

                            updateMap();

                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
        update.start();


    }


    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Toast.makeText(this,
                marker.getTitle() +
                        " Marker Clicked",
                Toast.LENGTH_SHORT).show();
        Integer clickCount = (Integer) marker.getTag();


        //marker.showInfoWindow();
        currentLocationCircle.setRadius(50);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

        CameraPosition camPos = new CameraPosition.Builder()
                .target(marker.getPosition())
                .zoom(18)
              //  .bearing(marker.getPosition())
                .tilt(70)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);


        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.i("Application", this.getClass().getName() + " has been Paused in lifecycle");
    }

    @Override
    public void onStop()
    {
        super.onStop();
        Log.i("Application", this.getClass().getName() + " has Stopped in lifecycle");
    }


    @Override
    public void onResume()
    {
        super.onResume();
        Log.i("Application", this.getClass().getName() + " has Resumed in lifecycle");
        // put your code here...

    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("Application", this.getClass().getName() + " has been Destroyed in lifecycle");
    }

    public Bitmap resizeBitmap(Bitmap bitmap)
    {
        int height = 100;
        int width = 100;

        Bitmap b = BitmapFactory.decodeResource(this.getResources(), R.mipmap.default_profile_picture);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        return smallMarker;
    }

}
