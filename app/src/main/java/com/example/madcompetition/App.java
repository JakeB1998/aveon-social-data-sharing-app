package com.example.madcompetition;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.example.madcompetition.BackEnd.Account;
import com.example.madcompetition.BackEnd.AccountDatabaseInterface;
import com.example.madcompetition.BackEnd.AppManager;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static com.example.madcompetition.FeedReaderDatabase.DATABASE_NAME;

public class App extends Application
{
    public static boolean validated = false;

    public static boolean connectedToServer = false;




    public void onCreate() {
        super.onCreate();

       // startService(new Intent(this, AlwaysRunService.class));
        Log.i("Application", "Application class called");

        init();
        // test if have an internet connection
        connectToServer();
        Intent intent = new Intent("android.net.wifi.WIFI_STATE_CHANGED");
        intent.setClass(this, BackroundBroadcastReciever.class);
    }

    public void connectToServer()
    { Log.i("Application", "Attempting to connect to server");

    if (AppManager.getInstance().isConnectedToServer() == false)
    {



        Log.i("Application", "Thread Started");
    }






    }

    public void init()
    {

       // getApplicationContext().deleteDatabase(DATABASE_NAME);

       // AccountDatabaseInterface.getInstance().addAccountToDatabase(getApplicationContext(), new Account("Alice"));
        Account[] x = AccountDatabaseInterface.getInstance().readData(this);

        for (Account z : x) {



            //Log.i("Database", z.toString());
        }

        //startService(new Intent(getApplicationContext(), LocationService.class));

        Intent intent = new Intent(this, App.class);
        PendingIntent pendingIntent;
        List<ActivityTransition> transitions = new ArrayList<>();

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.IN_VEHICLE)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.IN_VEHICLE)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.WALKING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        ActivityTransitionRequest request = new ActivityTransitionRequest(transitions);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);

        Task<Void> task = ActivityRecognition.getClient(this)
                .requestActivityTransitionUpdates(request, pendingIntent);


        task.addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                       Log.i("Application"," Request trnasition was sucessful");
                    }
                }
        );

        task.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("Application"," Request trnasition was unsuccessful");
                        Log.e("Application",  e.getMessage());



                    }
                }
        );

    }




}
