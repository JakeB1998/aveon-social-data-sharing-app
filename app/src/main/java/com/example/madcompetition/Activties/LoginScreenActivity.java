package com.example.madcompetition.Activties;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.example.madcompetition.AccountType;
import com.example.madcompetition.BackEnd.Account;
import com.example.madcompetition.BackEnd.AccountDatabaseInterface;
import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.Encryption;
import com.example.madcompetition.BackEnd.LocationData;
import com.example.madcompetition.LocationManager;
import com.example.madcompetition.R;
import com.example.madcompetition.ServerConnectInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.madcompetition.FeedReaderDatabase.DATABASE_NAME;

public class LoginScreenActivity extends AppCompatActivity
{
    private Button loginBtn;
    private Button createAccountBtn;
    private Button notYouBtn;
    private EditText userName;
    private EditText passwordInput;

    public static int PICK_IMAGE_REQUEST_CODE = 1;
    private AppManager appManager;
    private Account selectedAccount;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        if (getIntent().hasExtra("Account")) {
            selectedAccount = (Account) getIntent().getSerializableExtra("Account");
        }




        appManager = AppManager.getInstance();
        appManager.setCurrentAccountLoggedIn(selectedAccount);
        getApplicationContext().deleteDatabase(DATABASE_NAME);


        //appManager = AppManager.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (AppManager.getInstance().isConnectedToServer() == false)
        {
            handleServerErrorMessage();

        }

        passwordInput = (EditText) findViewById(R.id.PasswordEditText);
        userName = findViewById(R.id.UsernameEditText);
        userName.setFocusable(false);
        userName.setClickable(false);
        if (selectedAccount.getAccountCredentials().getUsername() == null) {
            userName.setText("Username is null");
        }
        else {
           userName.setText(selectedAccount.getAccountCredentials().getUsername());
        }



        LocationManager manager = LocationManager.getInstance();
        //manager.startLocationUpdates(this);

        //getLastLocation();

        Log.i("Permission", "Fine Location : " +  Integer.toString(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        loginBtn = (Button)findViewById(R.id.LoginBtn);
        notYouBtn = (Button)findViewById(R.id.NotYouBtn);
        notYouBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back to profile selection
            }
        });
        Log.i("Mine", "Init");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(),"Logging in", Toast.LENGTH_SHORT).show();
                checkCredentials();
               // RequestUserToPickPicture();
                Log.i("Mine", "Clicked");
                if (ServerConnectInterface.getInstance().isConnected()) {
                    ServerConnectInterface.getInstance().queSocketClose();
                   // Toast.makeText(getApplicationContext(),"Server Discconected", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Log.i("Mine", "Cannot disconnect from server as device is not connected");
                }


            }
        });



        Account c = new Account();
        SQLiteDatabase db = AccountDatabaseInterface.getInstance().getReadableDatabase(this);



               // Log.i("Mine", Long.toString(newRowId));
        db = AccountDatabaseInterface.getInstance().getReadableDatabase(getApplicationContext());




        ArrayList<Account> accounts = new ArrayList<Account>(0);
        String s = "";
        Log.i("Mine", "Started");

            //AccountDatabaseInterface.getInstance().readData(getApplicationContext());

        Log.i("Mine", "Finished");
    }

    private void checkCredentials()
    {
        String hashedPassword = selectedAccount.getAccountCredentials().getHashedPassword();
        TextView passwordUI = (TextView) findViewById(R.id.PasswordEditText);

       String password =  passwordUI.getText().toString();

        Log.i("Mine",password + "--" + hashedPassword);


       if (hashedPassword.equals(Encryption.hashMessage(password)) == true)
       {

           this.loadProfile();
           this.loadNextActivity();


       }
       else
       {
           Toast.makeText(getApplicationContext(),"Invalid credentials", Toast.LENGTH_SHORT).show();


       }
    }


    private void loadNextActivity()
    {
        if (appManager.getCurrentAccountLoggedIn().getAccountType() == AccountType.User) {
            //Intent intent = new Intent(this, UserMainScreenActivity.class);
            Intent intent = new Intent(this, UserMainScreenActivity.class);
            //EditText editText = (EditText) findViewById(R.id.editText);
            // String message = editText.getText().toString();
            // intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
        else if (appManager.getCurrentAccountLoggedIn().getAccountType() == AccountType.Admin)
        {
            Intent intent = new Intent(this, AdminMainScreenActivity.class);
            //EditText editText = (EditText) findViewById(R.id.editText);
            // String message = editText.getText().toString();
            // intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, UserMainScreenActivity.class);
            //EditText editText = (EditText) findViewById(R.id.editText);
            // String message = editText.getText().toString();
            // intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);

        }

    }

    private void loadAccountData()
    {

        AppManager.getInstance().setCurrentAccountLoggedIn(selectedAccount);
        Account account = AppManager.getInstance().getCurrentAccountLoggedIn();

        // load all databases

    }


    private void loadProfileSelection()
    {
        Intent intent = new Intent(this, ActivityCreateAnAccount.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        // String message = editText.getText().toString();
        // intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


    private void loadProfile()
    {

        if (selectedAccount != null) {
            appManager.setCurrentAccountLoggedIn(selectedAccount);
        }

    }

    private void selectAccount(ImageButton source)
    {


    }



    private void RequestUserToPickPicture()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == PICK_IMAGE_REQUEST_CODE && data != null) {
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                Bitmap pic = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                Log.e("Application", e.getMessage());
            }
        }
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

    private void getLastLocation()
    {
        boolean permissionAccessCoarseLocationApproved =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        // App doesn't have access to the device's location at all. Make full request
        // for permission.
        if (permissionAccessCoarseLocationApproved)
        {
            boolean backgroundLocationPermissionApproved =
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            == PackageManager.PERMISSION_GRANTED;

            if (backgroundLocationPermissionApproved)
            {

            }
            else
            {

                ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        LocationManager.LOCATION_REQUEST_CODE);
            }
        }
        else ActivityCompat.requestPermissions(this, new String[]
                        {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        },
                69);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            //  selectedAccount.setLastLocation(location);
                            // appManager.getCurrentAccountLoggedIn().setLastLocation(location);
                            Geocoder geo =  new Geocoder(getApplicationContext(), Locale.getDefault());
                            try
                            {

                                String adress = geo.getFromLocation(location.getLatitude(),location.getLongitude(),1).get(0).getLocality().toString();
                                Log.i("Mine", adress.toString());

                                LocationData locationData = new LocationData(location, geo);


                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.i("Application", e.getMessage() + "From : " + LoginScreenActivity.class.getName());


                            }
                            Log.i("Mine", location.getLatitude() + " -" + location.getLongitude() + " - " + location.getAccuracy());


                        }
                        else {

                            Log.i("Location", "Location is null" + "From : " + LoginScreenActivity.class.getName());
                        }
                    }
                });
    }


    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                requestingLocationUpdates);
        // ...
        super.onSaveInstanceState(outState);
    }
    */

    private void handleServerErrorMessage()
    {
        Log.i("Application", "Dialog should shoqw");
        new AlertDialog.Builder(this)
                .setTitle("Server Connection Errror")
                .setMessage("You are not connected to the server")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Thread T = new Thread(ServerConnectInterface.getInstance());
                        T.start();
                        Toast.makeText(getApplicationContext(),"Connecting to server", Toast.LENGTH_LONG).show();
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    Thread.sleep(3000);
                                    if (AppManager.getInstance().isConnectedToServer() == false) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                handleServerErrorMessage();

                                            }
                                        });
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        t.start();

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Ok", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }







}
