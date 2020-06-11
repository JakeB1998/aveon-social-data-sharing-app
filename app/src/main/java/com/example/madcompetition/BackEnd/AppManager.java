package com.example.madcompetition.BackEnd;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import com.example.madcompetition.BackEnd.account.Account;
import com.example.madcompetition.BackEnd.server.ServerConnectInterface;
import com.example.madcompetition.activties.ActivityCreateAnAccountFinal;
import com.example.madcompetition.activties.ActivityCreateAnAccountSetupPart1;
import com.example.madcompetition.activties.ActivityProfileSelection;
import com.example.madcompetition.BackEnd.Databases.AccountDatabaseInterface;
import com.example.madcompetition.BackEnd.Interfaces.MyTimeUpdate;
import com.example.madcompetition.activties.LoginScreenActivity;

import java.io.IOException;
import java.net.Inet4Address;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class AppManager {
    private static final AppManager ourInstance = new AppManager();
   private Context appContext;
   private final int UPDATE_TIME_RECCURENCE = 2000;



   public static String deviceId = "";
    public static final String NULLABLE_STRING = "--als";
    public static Calendar todaysDate  = Calendar.getInstance();
   public  static AppManager getInstance() {
        return ourInstance;
    }

    private Account currentAccountLoggedIn;


   private ArrayList<MyTimeUpdate> updateCallbacks;
    private boolean isConnectedToServer;

    public Location locationsFromLastUpdate;
    private Inet4Address deviceAddress;


    private boolean stopUpdates;
    private Handler myHandler;
    private Runnable myRunnable;

    /**
     *
     */
    private AppManager()
    {
    }

    /**
     *
     * @return
     */
    public static AppManager getOurInstance() {
        return ourInstance;
    }

    /**
     *
     * @return
     */
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

    /**
     *
     * @param account
     * @return
     */
    public boolean isLoggedIn(Account account)
    {
        if (getCurrentAccountLoggedIn().equals(account))
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
            currentAccountLoggedIn = AccountDatabaseInterface.getInstance().getLoggedInAccountFromDatabase(getAppContext());

        }


        if (appContext != null)
        {
            if (currentAccountLoggedIn != null) {
                String uniqueDeviceID = Settings.Secure.getString(appContext.getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                if (currentAccountLoggedIn.getAccountInformation() != null) {
                    currentAccountLoggedIn.getAccountInformation().setUniqueDeviceID(uniqueDeviceID);
                    Log.i("ASS", uniqueDeviceID);
                }
            }

        }

        return currentAccountLoggedIn;
    }

    public void setCurrentAccountLoggedIn(Account currentAccountLoggedIn, Context context)
    {

        Activity activity = (Activity) context;

        if (activity.getClass().getName().equalsIgnoreCase(ActivityProfileSelection.class.getName()) // change to login activity
            || activity.getClass().getName().equalsIgnoreCase(ActivityCreateAnAccountSetupPart1.class.getName())
            || activity.getClass().getName().equalsIgnoreCase(LoginScreenActivity.class.getName()))
        {

            if (getCurrentAccountLoggedIn() != null) {
                AccountDatabaseInterface.getInstance().unCommitAccountToDatabase(getCurrentAccountLoggedIn(), context);
            }
            AccountDatabaseInterface.getInstance().commitLoggedInAccount(currentAccountLoggedIn, context);
            this.currentAccountLoggedIn = currentAccountLoggedIn;
            this.currentAccountLoggedIn.setDateLastAccessed(LocalDate.now());

            Log.e(this.getClass().getName(), "Succesfully set account to logged in");
        }
        else
        {
            Log.e(this.getClass().getName() + "-" + "Security", activity.getClass().getName() + " Tried to change account");
        }

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


    public Bitmap decodeToBitmap(Uri URI, Context context)
    {
        Bitmap bitmap = null;


        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),URI);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Bitmap", e.getMessage());
        }

        return bitmap;

    }

    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    public void registerUpdateListener(MyTimeUpdate callback)
    {
        if (updateCallbacks != null)
        {
            updateCallbacks.add(callback);

        }
        else
        {
            updateCallbacks = new ArrayList<>(0);
            updateCallbacks.add(callback);
        }
    }




}
