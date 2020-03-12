package com.example.madcompetition.BackEnd;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.example.madcompetition.ActivityStatus;
import com.example.madcompetition.PersonalInformation;

import java.io.File;
import java.io.IOException;

public class Profile
{
    private PersonalInformation personalInfo;

    private Bitmap profileImage;
    private File profileImageFile;
    private Account profileAccount;
    private ActivityStatus onlineStatus;

    private Location location;

    private boolean markedOnMap;


    public Profile()
    {
        setOnlineStatus(ActivityStatus.Offline);
    }



    public Profile(PersonalInformation info, Account account)
    {

        this();

        this.setPersonalInfo(info);
        this.setProfileAccount(getProfileAccount());


    }


    public PersonalInformation getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInformation personalInfo) {
        this.personalInfo = personalInfo;
    }

    public Account getProfileAccount() {
        return profileAccount;
    }

    public void setProfileAccount(Account profileAccount) {
        this.profileAccount = profileAccount;
    }

    public ActivityStatus getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(ActivityStatus onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isMarkedOnMap() {
        return markedOnMap;
    }

    public void setMarkedOnMap(boolean markedOnMap) {
        this.markedOnMap = markedOnMap;
    }
}
