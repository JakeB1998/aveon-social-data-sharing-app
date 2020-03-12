package com.example.madcompetition.Activties;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class UserMainScreenActivity extends AppCompatActivity {

    private AppManager appManager;
    private ImageButton messageIconBtn;
    private ImageButton friendsListIconBtn;
    private ImageButton mapIconBtn;
    private ImageButton fileIconBtn;
    private ImageButton cloudIconBtn;
    private ImageButton settingsIconBtn;
    private ImageButton searchIconBtn;
    private ImageButton profileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_screen);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //fab.setImageIcon(Icon.createWithBitmap(BitmapFactory.decodeResource(this.getResources(),R.mipmap.default_settings_icon)));

        messageIconBtn = findViewById(R.id.MessagesBtn);
        friendsListIconBtn = findViewById(R.id.FriendsBtn);
        mapIconBtn = findViewById(R.id.MapBtn);
        fileIconBtn = findViewById(R.id.LocalFilesBtn);
        profileIcon = findViewById(R.id.ProfileIconBtn);

        messageIconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadMessagingActivity();

            }
        });

        friendsListIconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFriendsListActivity();

            }
        });

        mapIconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadMapActivity();


            }
        });

        fileIconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadMapActivity();


            }
        });
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProfileActivity();
            }
        });

        /*
        settingsIconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadMapActivity();


            }
        });

        cloudIconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadMapActivity();


            }
        });

         */

        this.start();
    }

    public void loadProfileActivity()
    {
        Intent intent = new Intent(this, ActivityProfileViewer.class);
        startActivity(intent);
    }

    public void loadMessagingActivity()
    {
        Intent intent = new Intent(this, ActivityMessagingInterface.class);
        startActivity(intent);
    }

    public void loadMapActivity()
    {
        if (AppManager.getInstance().isNetworkAvailable(this)) {
            Intent intent = new Intent(this, ActivityUserMap.class);
            startActivity(intent);
        }
        else
        {
            noNetworkConnectionErrorToUser();

        }
    }

    public void loadFriendsListActivity()
    {
        Intent intent = new Intent(this, ActivityFriendsList.class);
        startActivity(intent);
    }



    public void noNetworkConnectionErrorToUser()
    {
        new AlertDialog.Builder(this)
                .setTitle("No internet Connection")
                .setMessage("You are not connected to the internet")



                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Ok", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }




    public void start()
    {
       // startService(new Intent(this, AlwaysRunService.class));
        appManager = AppManager.getInstance();
        if (appManager.isLoggedIn() == false)
        {
            Log.e("Mine", "No account is logged in");

        }
    }

    public void bounceAnimation(View view)
    {

    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.i("Application", this.getClass().getName() + " has been Paused in lifecycle");
    }

    @Override
    public void onStop()
    {
        super.onStop();
        Log.i("Application", this.getClass().getName() + " has Stopped in lifecycle");
    }


    @Override
    public void onResume()
    {
        super.onResume();
        Log.i("Application", this.getClass().getName() + " has Resumed in lifecycle");
        // put your code here...

    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("Application", this.getClass().getName() + " has been Destroyed in lifecycle");
    }

}
