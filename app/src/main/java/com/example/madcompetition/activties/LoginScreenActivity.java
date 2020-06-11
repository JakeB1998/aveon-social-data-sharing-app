package com.example.madcompetition.activties;

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
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.madcompetition.BackEnd.account.AccountInformation;
import com.example.madcompetition.BackEnd.account.AccountType;
import com.example.madcompetition.BackEnd.account.Account;
import com.example.madcompetition.BackEnd.Databases.AccountDatabaseInterface;
import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.security.Credentials;
import com.example.madcompetition.BackEnd.security.Encryption;
import com.example.madcompetition.BackEnd.LocationData;
import com.example.madcompetition.BackEnd.server.ClientServerMessage;
import com.example.madcompetition.BackEnd.server.CredentialsRequest;
import com.example.madcompetition.BackEnd.server.ServerContract;
import com.example.madcompetition.BackEnd.utils.SerializationOperations;
import com.example.madcompetition.LocationManager;
import com.example.madcompetition.R;
import com.example.madcompetition.BackEnd.server.ServerConnectInterface;
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.madcompetition.BackEnd.Databases.FeedReaderDatabase.DATABASE_NAME;

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
//


        //appManager = AppManager.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        passwordInput = (EditText) findViewById(R.id.PasswordEditText);
        userName = findViewById(R.id.UsernameEditText);

        if (selectedAccount != null) {
            userName.setFocusable(false);
            userName.setClickable(false);
            if (selectedAccount.getAccountCredentials().getUsername() == null) {
                userName.setText("Username is null");
            } else {
                userName.setText(selectedAccount.getAccountCredentials().getUsername());
            }
        }


        Log.i("Permission", "Fine Location : " +  Integer.toString(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)));

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

                Log.i("Mine", "Clicked");



            }
        });


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

        TextView passwordUI = (TextView) findViewById(R.id.PasswordEditText);

       String password =  passwordUI.getText().toString();
       String hashedPasswordNew = Encryption.hashMessage(password);
       LoginServerConnect task = new LoginServerConnect();
       String[] params = {userName.getText().toString(),hashedPasswordNew};
       task.execute(params);

    }


    private void loadNextActivity()
    {
        startActivity(new Intent(this, ActivityMessagingInterface.class));


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




    private class LoginServerConnect extends AsyncTask<String, Boolean, Credentials>
    {


        Socket clientSocket;

        @Override
        protected Credentials doInBackground(String... strings) {
            String username = strings[0];
            String hashedPassword = strings[1];

            if (connect())
            {
                if (username.length() > 0) {
                    Credentials returnCred;
                    CredentialsRequest request = new CredentialsRequest(username);
                    request.setHashedPassword(hashedPassword);

                    try {

                        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                        out.writeObject(request);
                        out.flush();
                        while (clientSocket.getInputStream().available() < 4)
                        {
                            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                           Object obj =  in.readObject();
                           if (obj instanceof CredentialsRequest)
                           {
                               CredentialsRequest requestNew = (CredentialsRequest) obj;

                               if (requestNew.getAccountInformation().length > 0) {
                                   AccountInformation info = (AccountInformation) SerializationOperations.deserializeToObject(requestNew.getAccountInformation());

                                   if (info != null) {

                                       Account account = new Account(info);
                                       AccountDatabaseInterface.getInstance().addAccountToDatabase(LoginScreenActivity.this, account);
                                       AppManager.getInstance().setCurrentAccountLoggedIn(account, LoginScreenActivity.this);
                                       publishProgress(true);


                                   }
                               }
                               else
                               {
                                   Log.i(this.getClass().getName(), "Invalid credentials");

                               }



                           }
                           else
                           {
                               Log.i(this.getClass().getName(),"Invalid Object");
                           }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {

            if (values[0])
            {
                ServerConnectInterface.getInstance().startRepeatingTask();
                loadNextActivity();
            }
            else
            {

            }

            super.onProgressUpdate(values);
        }

        public boolean connect()
        {
            boolean bool;
            clientSocket = null;

            try
            {

                clientSocket = new Socket(ServerContract.SERVER_IP_ADRESS, ServerContract.SERVER_PORT_NUMBER);
                clientSocket.setTcpNoDelay( true );

                bool = true;
                //inFromServer = new DataInputStream( clientSocket.getInputStream());
                //outToServer = new DataOutputStream(  clientSocket.getOutputStream());
                AppManager.getInstance().setConnectedToServer(true);

            } catch (UnknownHostException e1)
            {

                AppManager.getInstance().setConnectedToServer(false);

                // TODO Auto-generated catch block
                e1.printStackTrace();
                Log.e("Server", e1.getCause().toString());
                return  false;
            } catch (IOException e1)
            {

                AppManager.getInstance().setConnectedToServer(false);
                bool = false;
                // TODO Auto-generated catch block
                e1.printStackTrace();
                Log.e("Server", e1.getCause().toString());
                return false;
            }

            return  bool;

        }
    }








}
