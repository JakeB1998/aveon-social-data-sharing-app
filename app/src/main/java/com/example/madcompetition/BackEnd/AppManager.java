package com.example.madcompetition.BackEnd;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.Inet4Address;
import java.time.LocalDate;
import java.util.Calendar;

public class AppManager {
    private static final AppManager ourInstance = new AppManager();

    public static final String NULLABLE_STRING = "--als";

    public static Calendar todaysDate  = Calendar.getInstance();

   public  static AppManager getInstance() {
        return getOurInstance();
    }

    private Account currentAccountLoggedIn;

   private Account accountLoggedInBackLog;

    private boolean isConnectedToServer;

    public Location locationsFromLastUpdate;
    private Inet4Address deviceAddress;


    private AppManager()
    {
        setConnectedToServer(false);

    }

    public static AppManager getOurInstance() {
        return ourInstance;
    }


    public boolean isLoggedIn()
    {
        if (getCurrentAccountLoggedIn() != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isLoggedIn(Account account)
    {
        if (getCurrentAccountLoggedIn() == account)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Account getCurrentAccountLoggedIn() {

        if (currentAccountLoggedIn == null)
        {
            if (accountLoggedInBackLog != null)
            {
                currentAccountLoggedIn = accountLoggedInBackLog;
            }
            else
            {

            }
        }

        return currentAccountLoggedIn;
    }

    public void setCurrentAccountLoggedIn(Account currentAccountLoggedIn) {
        this.currentAccountLoggedIn = currentAccountLoggedIn;
        this.accountLoggedInBackLog = currentAccountLoggedIn;
        this.currentAccountLoggedIn.setDateLastAccessed(LocalDate.now());
    }

    public boolean checkPermission(String permission)
    {
        boolean bool = false;
        //check permission


        return bool;
    }

    public boolean isConnectedToServer() {
        return isConnectedToServer;
    }

    public void setConnectedToServer(boolean connectedToServer) {
        isConnectedToServer = connectedToServer;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public Inet4Address getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(Inet4Address deviceAddress) {
        this.deviceAddress = deviceAddress;
    }
}
