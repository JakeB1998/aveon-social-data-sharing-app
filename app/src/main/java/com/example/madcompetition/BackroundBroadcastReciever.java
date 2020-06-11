package com.example.madcompetition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BackroundBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {

        Log.i("Backround", "Received broadcast");
        Log.i("Mine", "Recieved broadcast");


    }
}
