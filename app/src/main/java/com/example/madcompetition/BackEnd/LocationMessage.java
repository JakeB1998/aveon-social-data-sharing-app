package com.example.madcompetition.BackEnd;

import android.location.Location;

import androidx.annotation.NonNull;

import com.example.madcompetition.MediaType;

public class LocationMessage extends Message {

    public LocationMessage()
    {
        super();
    }

    private double longitude;
    private double latitude;



    public LocationMessage(Location location, Account sender, Account[] recepients)
    {

        super(sender,recepients,MediaType.Location, "Long : " +  Double.toString(location.getLongitude()) + "Lat : " + Double.toString(location.getLatitude()));
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
    }


    public LocationMessage(LocationData location, Account sender, Account[] receients)
    {

        super(sender,receients, MediaType.Location, "Long : " +  Double.toString(location.getLongitude()) + "Lat : " + Double.toString(location.getLatitude()));
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
    }




    @NonNull
    @Override
    public String toString() {
        String str = super.getMessage();
        return str;
    }
}
