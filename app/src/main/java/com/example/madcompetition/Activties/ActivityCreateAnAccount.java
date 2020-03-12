package com.example.madcompetition.Activties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.madcompetition.AccountSubType;
import com.example.madcompetition.AccountType;

import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.Account;
import com.example.madcompetition.BackEnd.AccountDatabaseInterface;
import com.example.madcompetition.R;
import com.example.madcompetition.ServerConnectInterface;

import java.io.CharArrayWriter;

public class ActivityCreateAnAccount extends AppCompatActivity
{

    public Button createBtn;

    public EditText firstName;
    public EditText lastName;
    public EditText phoneNumberInput;
    public EditText emailInput;

    private Account newAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_an_account);

        createBtn = (Button)findViewById(R.id.CreateBtn);

        firstName = (EditText) findViewById(R.id.FirstNameEditText);
        lastName = (EditText)findViewById(R.id.LastNameEditText);
        phoneNumberInput = (EditText) findViewById(R.id.PhoneEditText);
        emailInput = (EditText) findViewById(R.id.EmailEditText);


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (checkData())
                {
                    loadNextActivity();
                }

            }
        });
    }


    public boolean checkData()
    {
        final int NUMOFCHARATERS = 3;
        boolean notValid = false;

        String firstNameS = AppManager.NULLABLE_STRING;
        String lastNameS = AppManager.NULLABLE_STRING;
        String phoneNumber = AppManager.NULLABLE_STRING;
        String email = AppManager.NULLABLE_STRING;

            if (emailInput.getText().length() > 0) {

                if (checkIfValidEmail(emailInput.getText().toString())) {
                    email = emailInput.getText().toString();
                } else {
                    notValid = true;
                }
            }


            if (phoneNumberInput.getText().length() > 0) {


                if (checkIfPhoneValid(phoneNumberInput.getText().toString())) {
                    phoneNumber = phoneNumberInput.getText().toString();
                    notValid = true;

                } else {
                    // not valid email format
                }
            }


            if (firstName.getText().length() > 0)
            {
                if (firstNameS.length() >= NUMOFCHARATERS) {
                    firstNameS = firstName.getText().toString();
                    char[] x = firstNameS.toCharArray();
                    for (char z : x) {
                        if (Character.isLetter(z) == false) {
                            notValid = true;
                            break;
                        }
                    }
                }
                else
                {
                    firstName.setError("Must have atleast " + NUMOFCHARATERS + " characters");
                }
            }



            if (lastName.getText().length() > 0) {
                lastNameS = firstName.getText().toString();
                if (lastNameS.length() >= NUMOFCHARATERS) {
                    char[] x = firstNameS.toCharArray();
                    for (char z : x) {
                        if (Character.isLetter(z) == false) {
                            notValid = true;
                            break;
                        }
                    }
                }
                else
                {
                    lastName.setError("Must have atleast " + NUMOFCHARATERS + " characters");
                }
            }



        if (notValid)
        {
            newAccount = null;
            return false;
        }
        else
        {
             newAccount = new Account(firstNameS + " " +  lastNameS, false,
                    AccountType.User, AccountSubType.Personal,phoneNumber,email);
            return true;
        }
    }



    public boolean checkIfValidEmail(String email)
    {
        String emailS = emailInput.getText().toString();
        emailS = emailS.trim();

        boolean valid1 = false;
        String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        valid1 = email.matches(EMAIL_REGEX);

        if (emailS.length() > 0)
        {
            if (emailS.contains("@") && emailS.contains(".com")) {
                int x = emailS.length() - 4;
                String temp = emailS.substring(x);
                if (temp.equalsIgnoreCase(".com")) {
                    int z1 = emailS.indexOf("@");
                    int z2 = emailS.lastIndexOf(".");
                    if (z1 < z2) {
                        boolean valid = false;
                        String temp1 = emailS.substring(z1 + 1, z2);
                        temp1 = temp1.trim();
                        switch (temp1) {
                            case "gmail":
                                valid = true;
                                break;
                            case "outlook":
                                valid = true;
                                break;
                            case "yahoo":
                                valid = true;
                                break;
                            default:
                        }

                        if (valid) {
                            return true;
                        }
                        else
                            emailInput.setError("email provider not supported. Suppoerted providers include : gmail, outlook, yahoo");
                    }
                    else
                        emailInput.setError("the @ symbol must be before .com");

                }
                else
                    emailInput.setError("Email does not end with: .com");

            } else
           {
               if (emailS.contains("@") == false && emailS.contains(".com") == true)
               {
                   emailInput.setError("Email does not contain @ symbol");
               }
               else if (emailS.contains(".com") == false && emailS.contains("@") == true)
               {
                   emailInput.setError("Email does not contain: .com");
               }
               else
               {
                   emailInput.setError("Email does not contain: @ , .com");
               }
           }

        }


        if (emailInput.getError() == null)
        {
            emailInput.setError("Email is not properly formated");
        }
        return false;
    }
    public boolean checkIfPhoneValid(String phone)
    {
        final int numberOf = 10;

        phone = phone.trim();
        if (phone.length() == numberOf)
        {
            return true;
        }
        else
        {
            phoneNumberInput.setError("Phone number is does not contain 8 numbers");
            return false;

        }


    }

    public void loadNextActivity()
    {
        Intent intent = new Intent(this, ActivityCreateAnAccountSetupPart1.class);
        if (newAccount != null) {
            intent.putExtra("Account", newAccount);
            startActivity(intent);
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
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
