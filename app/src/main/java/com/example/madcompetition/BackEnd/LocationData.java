package com.example.madcompetition.BackEnd;

import android.location.Geocoder;
import android.location.Location;

import java.io.Serializable;

public class LocationData implements Serializable
{

    private double longitude;
    private double latitude;
    private String address;
    private String accuracy;
    private String country;
    private String provider;
    private String postalCode;





    public LocationData()
    {}
    public LocationData(Location location)
    {
        this.setLongitude(location.getLongitude());
        this.setLatitude(location.getLatitude());
        this.setAccuracy(Float.toString(location.getAccuracy()));


    }
    public LocationData(Location location, Geocoder geo)
    {
        this.setLongitude(location.getLongitude());
        this.setLatitude(location.getLatitude());
        this.setAccuracy(Float.toString(location.getAccuracy()));


    }


    public void setLocationData(Geocoder geo)
    {}


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
