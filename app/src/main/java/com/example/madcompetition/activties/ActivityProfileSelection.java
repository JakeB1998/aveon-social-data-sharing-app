package com.example.madcompetition.activties;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.madcompetition.BackEnd.App;
import com.example.madcompetition.BackEnd.Databases.FeedReaderContract;
import com.example.madcompetition.BackEnd.account.Account;
import com.example.madcompetition.BackEnd.Databases.AccountDatabaseInterface;
import com.example.madcompetition.BackEnd.account.AccountInformation;
import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.Databases.FeedReaderDatabase;
import com.example.madcompetition.BackEnd.messaging.system.TextMessage;
import com.example.madcompetition.BackEnd.server.ClientServerMessage;
import com.example.madcompetition.BackEnd.server.MessageType;
import com.example.madcompetition.BackEnd.server.ServerConnectInterface;
import com.example.madcompetition.BackEnd.settings.account.AccessibilitySettings;
import com.example.madcompetition.BackEnd.utils.DeviceUtils;
import com.example.madcompetition.BackEnd.utils.KeyboardUtils;
import com.example.madcompetition.BackEnd.utils.SerializationOperations;
import com.example.madcompetition.LocationService;
import com.example.madcompetition.R;
import com.example.madcompetition.activties.Fragments.SwipeTorefreshFragment;

import java.util.Locale;

public class ActivityProfileSelection extends AppCompatActivity implements FragmentOptions.OnFragmentInteractionListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    public final int GALLERY_REQUEST_CODE = 8274;


    private Button importBtn;
    private Button createAccountBtn;
    private ImageButton[] profileBtns;
    private LinearLayout mLayout;
    private Account[] profiles;
    private SQLiteDatabase db;

    private SwipeTorefreshFragment mSwipeRefresh;



    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        KeyboardUtils.hideKeyboard(this);
        Log.i("Application", this.getClass().getName() + " : Activtiy oncreate called");

        //startService(new Intent(this, LocationService.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_selection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //startActivity(intent);
       //this.deleteDatabase(FeedReaderDatabase.DATABASE_NAME);
        createAccountBtn = findViewById(R.id.CreateAccountBtn);
        mLayout = findViewById(R.id.main);


        AnimationDrawable animationDrawable = (AnimationDrawable) mLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();


        importBtn = findViewById(R.id.ImportBtn);
       // mLayout.removeView(createAccountBtn);
       // mLayout.removeView(importBtn);
        db = AccountDatabaseInterface.getInstance().getReadableDatabase(this);


        readInProfiles();

        //importBtn.setY(importBtn.getY() + 100);
        //createAccountBtn.setY(createAccountBtn.getY() + 100);

        //mLayout.addView(importBtn);
       // mLayout.addView(createAccountBtn);
        final Intent intentCreate = new Intent(this,ActivityCreateAnAccount.class);

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityProfileSelection.this, LoginScreenActivity.class));
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (DeviceUtils.isNetworkAvailable(ActivityProfileSelection.this)) {
                    startActivity(intentCreate);
                }
                else
                {
                    DeviceUtils.buildAlert(ActivityProfileSelection.this, "You are not connected to the internet");
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_account_part1, menu);
        return true;
    }


    private void readInProfiles()
    {
        final int accountId = 66666666;
        profiles = AccountDatabaseInterface.getInstance().readData(this);

            if (profiles.length > 0) {

                Log.i("Account", "The number of  accounts read from database : " + Integer.toString(profiles.length));
              //  Log.i("Account", "The number of  accounts read from database : " + Integer.toString(profiles[0].getAccountID()));

                handleGui();
            }

    }

    private void handleGui()
    {

        ImageView profilebutton = null;
        LinearLayout x = null;
        int index = 0;
        int num =  1;
        for (Account c : profiles) {
            if (c != null) {
                x = new LinearLayout(this);
                x.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400));
                x.setOrientation(LinearLayout.HORIZONTAL);
                 x.setTranslationY(50 * num );
                x.setY(x.getY() + 100);
                x.setX(x.getX() - 200);
                num++;

                profilebutton = new ImageView(this);
                profilebutton.setContentDescription(Integer.toString(index));
                profilebutton.setLayoutParams(new ViewGroup.LayoutParams(400,400));



                profilebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


                Log.i("DD", this.getLocalClassName());
                if (bitmap == null)
                {
                    if (c.getProfilePicture() != null) {
                        //profilebutton.setImageBitmap(c.getProfilePicture());


                    } else {

                        if (AppManager.getInstance().getCurrentAccountLoggedIn() != null) {
                            if (AppManager.getInstance().getCurrentAccountLoggedIn().getProfileBitmap() != null) {
                                profilebutton.setImageBitmap(AppManager.getInstance().getCurrentAccountLoggedIn().getProfileBitmap());
                            }
                            else
                            {
                                profilebutton.setBackground(getDrawable(R.mipmap.default_profile_picture));
                            }
                        }



                    }
                } else {
                    profilebutton.setImageBitmap(bitmap);
                    Log.i("Bitmsp", "Bitmap was set from gallary");
                }
                Drawable d = this.getDrawable(R.drawable.ic_android_black_24dp);
                profilebutton.setBackground(getDrawable(R.mipmap.default_profile_picture));

                Bitmap m = c.getProfilePicture();
                //Log.i("Bitmap", c.getProfilePicture().toString());
                //profilebutton.setImageResource(R.drawable.ic_android_black_24dp);

                int pixels = getApplicationContext().getResources().getDisplayMetrics().widthPixels;

                profilebutton.setTranslationX((float) (pixels / 2) - profilebutton.getMinimumWidth() / 2);
                profilebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String contentent = v.getContentDescription().toString();
                        int index = Integer.parseInt(contentent);
                        Log.i("Click", contentent);
                        loadLoginActivity(profiles[index]);


                    }
                });

                profilebutton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ActivityProfileSelection.this.deleteDatabase(FeedReaderDatabase.DATABASE_NAME);
                        finish();
                        startActivity(getIntent());
                        return true;
                    }
                });

                x.addView(profilebutton);
                mLayout.addView(x);
                Log.i("UI", profilebutton.toString() + " was added sucessfully");
                index++;

            }
        }



    }

    private void loadLoginActivity(Account account)
    {
        //startActivity(new Intent(this, ActivityCreateAnAccount.class));
        AppManager.getInstance().setCurrentAccountLoggedIn(account, this);
        this.loadLanguage();
        if (AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation() == null)
        {
            Account c = AppManager.getInstance().getCurrentAccountLoggedIn();
            c.setAccountInformation(new AccountInformation(AppManager.getInstance().getDeviceAddress(), c.getAccountID(), c.getAccountCredentials(), App.getDeviceID()));
            Log.i(this.getClass().getName(), "Account infro was null on logged in account line 241");
        }




        AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(AppManager.getInstance().getAppContext());
       ServerConnectInterface.getInstance().startRepeatingTask();

        if (AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation().getPersonalInformation() == null) {
            AppManager.getInstance().getCurrentAccountLoggedIn().setPersonalInformation(AppManager.getInstance().getCurrentAccountLoggedIn().getPersonalInformation());
            AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(this);
            Log.e(this.getClass().getName(), "Personal info in account info was null. auto corrected");
        }
        AccountInformation[] cc = {AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation()};
        TextMessage message = new TextMessage("ass",AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation(),cc);
        ClientServerMessage message1 = new ClientServerMessage( AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation(),AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation(),
                MessageType.MessageToUser, SerializationOperations.serializeObjectToBtyeArray(message));
        //ServerConnectInterface.getInstance().addClientServerMessageToQue(message1);

        startActivity(new Intent(this, ActivityMessagingInterface.class));

        /*
        Intent intent = new Intent(this, LoginScreenActivity.class);
        intent.putExtra("Account", account);
        startActivity(intent);

         */
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

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        ////
    }

    public void addRefreshFeature()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SwipeTorefreshFragment fragment = new SwipeTorefreshFragment();
        fragmentTransaction.add(R.id.main, fragment);
        fragmentTransaction.commit();

        fragment.registerOnRefreshListener(this);

        mSwipeRefresh = fragment;

    }


    @Override
    public void onRefresh() {
        Log.i(this.getClass().getName(), "Swipe refresh callback called");

        if (mSwipeRefresh != null)
        {
            mSwipeRefresh.refreshFinished();
        }

    }

    private void loadLanguage()
    {
        Locale locale = (AppManager.getInstance().getCurrentAccountLoggedIn().getSavedSettings().getAccessibilitySettings().getSelectedLocale());
        Configuration config = new Configuration(this.getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);
        if (AppManager.getInstance().getCurrentAccountLoggedIn().getSavedSettings().getAccessibilitySettings() == null)
        {
            AppManager.getInstance().getCurrentAccountLoggedIn().getSavedSettings().setAccessibilitySettings(new AccessibilitySettings(Locale.US));
            AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(this);
        }

        AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(this);

        this.getBaseContext().getResources().updateConfiguration(config,
                this.getBaseContext().getResources().getDisplayMetrics());
    }

}
