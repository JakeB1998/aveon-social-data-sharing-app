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

/**
 * Manager class that ovesees application operations
 */
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
     * Default constructor
     */
    private AppManager()
    {
    }

    /**
     * Retrieves singleton instance of this manager class
     * @return This istnace
     */
    public static AppManager getOurInstance() {
        return ourInstance;
    }

    /**
     * Determines if there is an account currently logged in.
     * @return true if there is an account  that is logged otherwise false
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
     * Query's if a specific account is logged into the application.
     * @param account the account to see if logged in
     * @return true if specified account is logged in
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

    /**
     *
     * @return the account that is logged in, null if no account is logged in
     */
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

    /**
     * Registers the current account that is logged in.
     * This method has security checks to ensure that only activity classes with the propper permisssions can execute this method.
     * @param currentAccountLoggedIn The account that is to be set as the logged in account
     * @param context The application context
     */
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

    /**
     * Checks application permission.
     * @param permission
     * @return true if specified permission is granted. Otherwise false.
     */
    public boolean checkPermission(String permission)
    {
        boolean bool = false;
        //check permission
        return bool;
    }

    /**
     *
     * @return true if application is connected to private application server
     */
    public boolean isConnectedToServer() {
        return isConnectedToServer;
    }

    /**
     *
     * @param connectedToServer
     */
    public void setConnectedToServer(boolean connectedToServer) {
        isConnectedToServer = connectedToServer;
    }

    /**
     * Check to see if a network outside the local bounds is avaliable.
     * @param context The application context
     * @return true if device is connected to the internet
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /**
     *
     * @return
     */
    public Inet4Address getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(Inet4Address deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    /**
     * This allows multiple activites to register its callback listeners
     * @param callback Callbacj interface
     */
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
