package com.example.madcompetition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;

public class LocationActivityTransitionBroadcaster extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("Mine", "Broadcast Recieved");

        if (ActivityTransitionResult.hasResult(intent))
        {
            ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);
            for (ActivityTransitionEvent event : result.getTransitionEvents())
            {
                // chronological sequence of events....
            }
        }
    }
}
