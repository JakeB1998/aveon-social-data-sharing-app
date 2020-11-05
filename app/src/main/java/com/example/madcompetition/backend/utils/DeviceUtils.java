package com.example.madcompetition.backend.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DeviceUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public static void buildAlert(Context context, String message)
    {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setCancelable(true);



        builder1.setNegativeButton(
                "OK",
                new DialogInterface.OnClickListener()
            {
                public void onClick (DialogInterface dialog,int id){
                dialog.cancel();
            }
            });

            AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
