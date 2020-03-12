package com.example.madcompetition.Activties;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import com.example.madcompetition.BackEnd.Account;
import com.example.madcompetition.BackEnd.Credentials;
import com.example.madcompetition.BackEnd.Encryption;
import com.example.madcompetition.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Debug;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ActivityCreateAnAccountSetupPart1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button continuebtn;
    private Spinner dropDown;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private Credentials userCred;
    private Account workingAccount;

    private boolean nothingSelected;

    private Object selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nothingSelected = false;
        userCred = null;
        workingAccount = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_an_account_setup_part1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            if (extras.containsKey("Account"))
            {
                workingAccount = (Account)  extras.getSerializable("Account");
            }

            if (extras.containsKey("Cred"))
            {
                userCred = (Credentials) extras.getSerializable("Cred");
            }
        }

        continuebtn = findViewById(R.id.ContinueBtn);
        usernameEditText = findViewById(R.id.UsernameEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);
        dropDown = findViewById(R.id.SelectionSpinner);
        dropDown.setOnItemSelectedListener(this);


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

                    createCredentials();
                    loadNextActivity();

                }
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
            usernameEditText.setError("Can not leave blank");
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

        userCred = new Credentials(usernameEditText.getText().toString(), Encryption.hashMessage(passwordEditText.getText().toString()));
        userCred.setCredType(Credentials.CredentialsType.Locked);
        userCred.setUniqueId(android_id);
    }


    private boolean checkData() {
        final int NUMOFCHARACTERS = 6;
        usernameEditText.setError("Text cant be left blank");
        Log.i("CreateAccountActivity", "Check data started");
        boolean check1 = false;

        boolean check2 = false;

        boolean notValid = false;

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.length() < NUMOFCHARACTERS) {
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

               } else {
                   z2 = true;
               }

               if (z1 == true && z2 == true) {
                   check1 = true;
                   break;
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
               passwordEditText.setError("Only characters and digits allowed");
               return false;
           }
       }



        for (char x: usernameChars)
        {
           if (Character.isLetterOrDigit(x))
           {

           }
           else
           {
               usernameEditText.setError("Only characters and digits are allowed");
               return false;
           }
        }

        Log.i("CreateAccountActivity","Check data finished");

        if (check1 == true && check2 == true && notValid == false) {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void loadNextActivity()
    {
        Intent intent = new Intent(this, ActivityCreateAnAccountFinal.class);
        if (workingAccount != null && userCred != null) {
            workingAccount.setAccountCredentials(userCred);
            intent.putExtra("Account", workingAccount);
            intent.putExtra("Cred", userCred);

            startActivity(intent);
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
}
