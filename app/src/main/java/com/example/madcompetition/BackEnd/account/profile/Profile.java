package com.example.madcompetition.BackEnd.account.profile;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.account.Account;
import com.example.madcompetition.BackEnd.account.ActivityStatus;
import com.example.madcompetition.BackEnd.account.PersonalInformation;

import java.io.File;
import java.io.IOException;

public class Profile
{
    private PersonalInformation personalInfo;

    private transient Bitmap profileImage;
    private File profileImageFile;
    private Account profileAccount;
    private ActivityStatus onlineStatus;
    private Location location;

    private boolean markedOnMap;






    public Profile(PersonalInformation info, Account account)
    {
        this.setPersonalInfo(info);
        this.setProfileAccount(getProfileAccount());

        profileImageFile = new File(Integer.toString(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountID()));
        try {
            if (profileImageFile.exists() == false) {
                profileImageFile.createNewFile();
            }


        } catch (IOException e) {
            e.printStackTrace();
            Log.e("File", e.getMessage());
        }

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

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public File getProfileImageFile() {
        return profileImageFile;
    }

    public void setProfileImageFile(File profileImageFile) {
        this.profileImageFile = profileImageFile;
    }
}
