package com.example.madcompetition.BackEnd;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.madcompetition.BackEnd.Databases.FeedReaderDatabase;
import com.example.madcompetition.BackEnd.settings.SavedSettings;
import com.example.madcompetition.BackroundBroadcastReciever;
import com.example.madcompetition.BackEnd.Interfaces.MyTimeUpdate;
import com.example.madcompetition.R;

public class App extends Application implements Application.ActivityLifecycleCallbacks, MyTimeUpdate
{
    public static boolean validated = false;
    public static boolean connectedToServer = false;
    private static String uniqueDeviceID = "NULL";



    public final static SavedSettings DEFAULT_SETTINGS = new SavedSettings();

    private AppManager appManager;

    /**
     *
     */
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);
        appManager = AppManager.getInstance();
        //WorkManager.getInstance(this).cancelAllWork();
        //WorkManager.getInstance(this).pruneWork();


        Log.i("Application", "Application class called");
        init();
        // test if have an internet connection

        Intent intent = new Intent("android.net.wifi.WIFI_STATE_CHANGED");
        intent.setClass(this, BackroundBroadcastReciever.class);

        appManager.getInstance().registerUpdateListener(this);
        uniqueDeviceID = Settings.Secure.getString(this.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        /*
        if (AppManager.getInstance().getCurrentAccountLoggedIn() != null) {
             AppManager.getInstance().connectToServer();
        }

         */

        Log.i("Cred", "Device ID : " + uniqueDeviceID );
       // this.deleteDatabase(FeedReaderDatabase.DATABASE_NAME);
        AppManager.getInstance().setAppContext(this.getApplicationContext());
    }


    /**
     * Initializes application instance
     */
    public void init()
    {
        AppManager.getInstance().setAppContext(this.getApplicationContext());
        //startService(new Intent(getApplicationContext(), LocationService.class));
        /*
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

         */

    }

    public void onStop()
    {}


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {


    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {


        final String Tag = "Stopped";
        Log.i("Life", "On stop called");

        /*
        PeriodicWorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(TestWorker.class, 16, TimeUnit.MINUTES)
                        .addTag(Tag)
                        .build();
        WorkManager.getInstance(this).enqueue(saveRequest);

        ListenableFuture<List<WorkInfo>> future =  WorkManager.getInstance(this).getWorkInfosByTag(Tag);
        try
        {
            List<WorkInfo> list = future.get();
            Log.i("Backround", Integer.toString(list.size()));
            if (list.size() > 0) {
                Log.i("Backround", "State : " + (list.get(0).getState().toString()));
                Log.i("Backround", (list.get(0).getProgress().toString()));
            }

            list.clear();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

         */
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    /**
     * Called when an activity is destroyed
     * @param activity The activity that was destroyed
     */
    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

        final String Tag = "Destroy";
        Log.i("Life", "On destroy called");
        /*
        if (activity.getClass().getName().equals(App.class.getName())) {   //only calls if main app is destroyed
           NotificationWorkerFactory.queNotificationJob(this);
        }

         */
    }

    /**
     *
     */
    public class MyDragListener implements View.OnDragListener {

        Drawable enterShape = getResources().getDrawable( R.drawable.shape_droptarget, getTheme());

        Drawable normalShape = getResources().getDrawable(R.drawable.shape, getTheme());

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackground(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackground(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackground(normalShape);
                default:
                    break;
            }
            return true;
        }


    }

    public void update()
    {

        Log.i("App", "Updated");
    }

    public static String getDeviceID()
    {
        return uniqueDeviceID;
    }









}
