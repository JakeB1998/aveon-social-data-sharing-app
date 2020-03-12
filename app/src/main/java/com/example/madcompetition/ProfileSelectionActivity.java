package com.example.madcompetition;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.madcompetition.BackEnd.Account;
import com.example.madcompetition.BackEnd.AccountDatabaseInterface;

public class ProfileSelectionActivity extends AppCompatActivity {

    private ImageButton[] profileBtns;
    private LinearLayout layout;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_selection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        layout = findViewById(R.id.main);

        Button myButton = new Button(this);
        myButton.setLayoutParams(new ConstraintLayout.LayoutParams(
                100,
                100));


        myButton.layout(1,1,1,1);
        layout.addView(myButton);

        //LocationManager.getInstance().startLocationUpdates(this);
        db = AccountDatabaseInterface.getInstance().getReadableDatabase(this);

        //readInProfiles();





    }

    private void readInProfiles()
    {
        ImageButton profilebutton = null;

        Account[] profiles = null;
        Log.i("Account", "The number of  accounts read from database : " + Integer.toString(profiles.length));
       profiles =  AccountDatabaseInterface.getInstance().readData(this);

       for (Account c : profiles)
       {

           profilebutton = new ImageButton(this);
           profilebutton.setContentDescription(Integer.toString(c.getAccountID()));

           layout.addView(profilebutton);
           Log.i("UI", profilebutton.toString() + " was added sucessfully");

       }
    }


}
