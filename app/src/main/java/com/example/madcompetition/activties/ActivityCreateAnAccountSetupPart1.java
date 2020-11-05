package com.example.madcompetition.activties;

import android.content.Intent;
import android.os.Bundle;

import com.example.madcompetition.backend.AppManager;
import com.example.madcompetition.backend.databases.AccountDatabaseInterface;
import com.example.madcompetition.backend.account.Account;
import com.example.madcompetition.backend.security.Credentials;
import com.example.madcompetition.backend.security.Encryption;
import com.example.madcompetition.backend.messaging.system.Message;
import com.example.madcompetition.backend.security.KeyContract;
import com.example.madcompetition.backend.server.ClientServerMessage;
import com.example.madcompetition.backend.server.MessageSubType;
import com.example.madcompetition.backend.server.MessageType;
import com.example.madcompetition.backend.server.ServerConnectInterface;
import com.example.madcompetition.backend.utils.SerializationOperations;
import com.example.madcompetition.R;
import com.example.madcompetition.activties.fragments.FragmentTags;
import com.example.madcompetition.activties.fragments.SwipeTorefreshFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class ActivityCreateAnAccountSetupPart1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button continuebtn;
    private Spinner dropDown;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private ImageButton mBackBtn;

    private Credentials userCred;
    private Account workingAccount;

    private boolean nothingSelected;

    private Object selectedItem;

    private Message[] messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nothingSelected = false;
        userCred = null;
        workingAccount = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_an_account_setup_part1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            if (extras.containsKey(KeyContract.ACCOUNT_KEY))
            {
                workingAccount = (Account)  extras.getSerializable(KeyContract.ACCOUNT_KEY);
            }

            if (extras.containsKey(KeyContract.CRED_KEY))
            {
                userCred = (Credentials) extras.getSerializable(KeyContract.CRED_KEY);
            }
        }

        continuebtn = findViewById(R.id.ContinueBtn);
        usernameEditText = findViewById(R.id.UsernameEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);
        mBackBtn = findViewById(R.id.BackBtn);



      //  usernameEditText.setError("Text cant be left blank");

        usernameEditText.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.i("Mine", "text changed");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                textDataValidation(usernameEditText);
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                textDataValidation(passwordEditText);
            }
        });

        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData())
                {
                    Toast.makeText(ActivityCreateAnAccountSetupPart1.this, "Creating credentialsr", Toast.LENGTH_LONG).show();
                    createCredentials();
                    loadNextActivity();

                }
                else
                {
                    Toast.makeText(ActivityCreateAnAccountSetupPart1.this, "Check data error", Toast.LENGTH_LONG).show();
                }
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

      String android_id = Settings.Secure.getString(this.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.i("Cred", "Device ID : " + android_id);

    }

    private void textDataValidation(EditText text)
    {
        String textS = text.getText().toString();
        if (text.getText().toString().length()<= 0) {
            text.setError("Cannot leave blank");

        }
        else
        {
            char[] x = textS.toCharArray();
            boolean q1 = false;
            for (char z : x)
            {
                if (Character.isLetterOrDigit(z) == false)
                {
                   text.setError("Must be only characters and digits only");
                   q1 = true;
                }
            }

            if (q1 == false) {
                text.setError(null);
            }
        }

    }


    private void createCredentials()
    {
        String android_id = Settings.Secure.getString(this.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.i("Cred", "Device ID : " + android_id);

        Log.i(this.getClass().getName(),  Encryption.hashMessage(passwordEditText.getText().toString()));
        userCred = new Credentials(usernameEditText.getText().toString(), Encryption.hashMessage(passwordEditText.getText().toString()));
        userCred.setUniqueId(android_id);
        workingAccount.setAccountCredentials(userCred);
    }


    private boolean checkData() {
        final int NUMOFCHARACTERS = 6;

        Log.i("CreateAccountActivity", "Check data started");
        boolean check1 = false;

        boolean check2 = false;

        boolean notValid = false;

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.length() < NUMOFCHARACTERS) {
            usernameEditText.setError("Must have atleast " + NUMOFCHARACTERS + " characters");
            notValid = true;

        }

        if (password.length() < NUMOFCHARACTERS)
        {
            notValid = true;
        }

       char[] usernameChars =  username.toCharArray();
       char[] passwordChars = password.toCharArray();

       boolean z1 = false;
       boolean z2 = false;
       for (char x: passwordChars)
       {
           if (Character.isLetterOrDigit(x)) {
               if (Character.isUpperCase(x))
               {
                   z1 = true;
                   Log.i(this.getClass().getName(), "Letter is upercase");

               } else if (Character.isLowerCase(x)) {
                   z2 = true;
                   Log.i(this.getClass().getName(), "Letter is lowerCase");
               }

               if (z1 == true && z2 == true) {
                   check1 = true;
                  passwordEditText.setError(null);
               }
               else if (z1 == true && z2 == false)
               {
                   passwordEditText.setError("Must have atleast one lowercaseletter");

               }
               else if (z1 == false && z2 == true)
               {
                   passwordEditText.setError("Must have atleast one uppercase letter");
               }
           }
           else
           {
               // is special character
               Log.i(this.getClass().getName(), "illegeal character");
               passwordEditText.setError("Only characters and digits allowed");
               return false;
           }
       }



        for (char x: usernameChars)
        {
           if (Character.isLetterOrDigit(x))
           {
               check2 = true;

           }
           else
           {
               usernameEditText.setError("Only characters and digits are allowed");
               check2 = false;
               return false;
           }
        }

        Log.i("CreateAccountActivity","Check data finished");

        return check1 == true && check2 == true && notValid == false;
    }

    private void loadNextActivity()
    {
        Intent intent = new Intent(this, ActivityCreateAnAccountFinal.class);
        this.finalizeAccount(workingAccount);
        if (workingAccount != null && userCred != null) {
            workingAccount.setAccountCredentials(userCred);
            intent.putExtra(KeyContract.ACCOUNT_KEY, workingAccount);
            intent.putExtra(KeyContract.CRED_KEY, userCred);

           // startActivity(intent);
        }
        else
        {
            Log.e("Activity", "User Credentials variable is null");
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        nothingSelected = false;
        Toast.makeText(this, dropDown.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        nothingSelected = true;
    }

    public void finalizeAccount(Account account)
    {
        Account finalAccount = account;
        AccountDatabaseInterface.getInstance().addAccountToDatabase(getApplicationContext(), finalAccount);
        AppManager.getInstance().setCurrentAccountLoggedIn(finalAccount, this);


        ClientServerMessage m = new ClientServerMessage(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation(),null, MessageType.DataLogToServer,
                SerializationOperations.serializeObjectToBtyeArray(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation()));
        m.setMessageSubType(MessageSubType.CreateAccount);
        ServerConnectInterface.getInstance().addClientServerMessageToQue(m);
        Intent intent = new Intent(this, ActivityProfileSelection.class);
        startActivity(intent);


    }


    /**
     * Inner class
     */
    private class SwipeRefreshFeature implements SwipeRefreshLayout.OnRefreshListener
    {
        private SwipeTorefreshFragment mSwipeRefresh;

        public void addSwipeFeature ()
        {

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (fragmentManager.findFragmentByTag(FragmentTags.SWIPE_UPDATE_FRAGMENT) == null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SwipeTorefreshFragment fragment = new SwipeTorefreshFragment();
                fragmentTransaction.add(R.id.main, fragment);
                fragmentTransaction.commit();

                fragment.registerOnRefreshListener(this);

                mSwipeRefresh = fragment;
            }
            else
            {
                Log.e(this.getClass().getName(), "Attempted to open swipe to refresh fragment when one is already on");
            }

        }





        @Override
        public void onRefresh ()
        {

            Log.i(this.getClass().getName(), "Swipe called back overridden here");
        }
    }
}
