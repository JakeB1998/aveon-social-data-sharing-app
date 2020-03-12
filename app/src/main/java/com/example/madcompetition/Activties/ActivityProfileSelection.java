package com.example.madcompetition.Activties;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.madcompetition.BackEnd.Account;
import com.example.madcompetition.BackEnd.AccountDatabaseInterface;
import com.example.madcompetition.BackEnd.AccountInformation;
import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.FeedReaderDatabase;
import com.example.madcompetition.R;
import com.example.madcompetition.ServerConnectInterface;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class ActivityProfileSelection extends AppCompatActivity implements FragmentOptions.OnFragmentInteractionListener {


    private Button importBtn;
    private Button createAccountBtn;
    private ImageButton[] profileBtns;
    private LinearLayout layout;
    private Account[] profiles;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        Log.i("Application", this.getClass().getName() + " : Activtiy oncreate called");
      //this.deleteDatabase(FeedReaderDatabase.DATABASE_NAME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_selection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = new Intent(this, ActivityConversationInterface.class);
        //startActivity(intent);



        createAccountBtn = findViewById(R.id.CreateAccountBtn);
        layout = findViewById(R.id.main);


        importBtn = findViewById(R.id.ImportBtn);
        layout.removeView(createAccountBtn);
        layout.removeView(importBtn);
        db = AccountDatabaseInterface.getInstance().getReadableDatabase(this);
        //LocationManager.getInstance().startLocationUpdates(this);

        readInProfiles();
        String android_id = Settings.Secure.getString(this.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.i("Cred", "Device ID : " + android_id);





        // test

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        FragmentOptions fragment = new FragmentOptions();

        fragmentTransaction.add(R.id.main, fragment);
        fragmentTransaction.commit();

        fragmentTransaction.hide(fragment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_account_part1, menu);
        return true;
    }


    private void readInProfiles()
    {

        /*
        Account c = new Account(this,"Jake", LocalDate.now().minusDays(3));
        c.generateAccountID();


            c.setAccountInformation(new AccountInformation(AppManager.getInstance().getDeviceAddress(), c.getAccountID(), c.getAccountCredentials()));

       AccountDatabaseInterface.getInstance().addAccountToDatabase(this, c );

         */



        profiles = AccountDatabaseInterface.getInstance().readData(this);
        ArrayList<Account> temp = new ArrayList<>(0);


        //temp.add(new Account(this,"2", LocalDate.now().minusDays(3)));
        //temp.add(new Account(this,"1", LocalDate.now().minusDays(2)));
       // temp.add(new Account(this,"4", LocalDate.now().minusDays(10)));
      // temp.add(new Account(this,"3", LocalDate.now().minusDays(5)));

        /*
        profiles = new Account[temp.size()];
        for (int i = 0; i < temp.size(); i++)
        {
            profiles[i] = temp.get(i);
        }

         */



            if (profiles.length > 0) {

                Log.i("Account", "The number of  accounts read from database : " + Integer.toString(profiles.length));
                Log.i("Account", "The number of  accounts read from database : " + Integer.toString(profiles[0].getAccountID()));

                //sortProfiles();// error
                handleGui();
            }
    }

    private void handleGui()
    {
        ImageButton profilebutton = null;
        LinearLayout x = null;
        int index = 0;
        int num =  1;
        for (Account c : profiles) {
         x = new LinearLayout(this);
            x.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300));
            x.setOrientation(LinearLayout.HORIZONTAL);
           // x.setTranslationY(50 * num );
            x.setY(x.getY() + 100);
            num++;

            profilebutton = new ImageButton(this);
            profilebutton.setContentDescription(Integer.toString(index));
            profilebutton.setMinimumWidth(250);
            profilebutton.setMinimumHeight(250);



            if (c.getProfilePicture() == null)
            {
               //profilebutton.setBackground(getResources().getDrawable(R.mipmap.default_profile_picture, getTheme()));
               // profilebutton.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.default_profile_picture));
                profilebutton.setBackground(getDrawable(R.mipmap.default_profile_picture));

            }
            else {


                //profilebutton.setImageBitmap(c.getProfilePicture());
            }
            Drawable d = this.getDrawable(R.drawable.ic_android_black_24dp);
            profilebutton.setBackground(getDrawable(R.mipmap.default_profile_picture));

            Bitmap m = c.getProfilePicture();
       //Log.i("Bitmap", c.getProfilePicture().toString());
            //profilebutton.setImageResource(R.drawable.ic_android_black_24dp);

            int pixels = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
            profilebutton.setTranslationX((float)(pixels / 2) - profilebutton.getMinimumWidth() / 2);
            profilebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                     String contentent =  v.getContentDescription().toString();
                     int index = Integer.parseInt(contentent);
                     Log.i("Click", contentent);
                     loadLoginActivity(profiles[index]);



                }
            });

            x.addView(profilebutton);
            layout.addView(x);
            Log.i("UI", profilebutton.toString() + " was added sucessfully");
            index++;

        }


        importBtn.setY(importBtn.getY() + 100);
        createAccountBtn.setY(createAccountBtn.getY() + 100);

        layout.addView(importBtn);
        layout.addView(createAccountBtn);
    }

    private void loadLoginActivity(Account account)
    {
        //startActivity(new Intent(this, ActivityCreateAnAccount.class));
        AppManager.getInstance().setCurrentAccountLoggedIn(account);
        ServerConnectInterface i = ServerConnectInterface.getInstance();

        Thread d = new Thread(i);
        d.start();
        startActivity(new Intent(this, ActivityMessagingInterface.class));

        Log.i("Account", Integer.toString(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountID()));

        /*
        Intent intent = new Intent(this, LoginScreenActivity.class);
        intent.putExtra("Account", account);
        startActivity(intent);

         */
    }


    private void handleClik()
    {}




    private void sortProfiles()
    {
        ArrayList<Account> sortedArray = new ArrayList<>(0);





        ArrayList<Account> temp = new ArrayList<>(0);

        for (Account c : profiles)
        {
            temp.add(c);

        }


        int size = temp.size();

        for (int i = 0 ; i < size; i++)
        {

           Account first = null;
           int index = 0;

            for (int j = 0; j < temp.size(); j++)
            {
                if (j == 0)
                {
                    first = temp.get(0);
                }

                Log.i("Debug", "Size" + Integer.toString(temp.size()) + " - " + Integer.toString(j + 1));


                    int Daysnew = (int) ChronoUnit.DAYS.between(temp.get(j).getDateLastAccessed(), LocalDate.now());
                    int daysPlus1 = (int) ChronoUnit.DAYS.between(first.getDateLastAccessed(), LocalDate.now());

                    Log.i("Debug", Integer.toString(Daysnew) + " - " + Integer.toString(daysPlus1));


                    if (Daysnew <= daysPlus1) {
                        first = temp.get(j);
                        index = j;
                    }





            }

            sortedArray.add(first);
            temp.remove(index);


        }



        for (int i = 0; i <  sortedArray.size(); i++)
        {
            profiles[i] = sortedArray.get(i);
        }

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("Application", "Activity state restored");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Log.i("Application", "Activity state Saved");
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
