package com.example.madcompetition.Activties;

import android.os.Bundle;

import com.example.madcompetition.BackEnd.Account;
import com.example.madcompetition.BackEnd.AccountDatabaseInterface;
import com.example.madcompetition.BackEnd.AccountSettings;
import com.example.madcompetition.BackEnd.Credentials;
import com.example.madcompetition.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class ActivityCreateAnAccountFinal extends AppCompatActivity
{

    private Account passedAccount;
    private AccountSettings settings;
    private Credentials cred;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("Account")) {
            passedAccount = (Account) getIntent().getExtras().get("Account");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_an_account_final);
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
    }


    public void finalizeAccount(Account account)
    {
        Account finalAccount = account;
        AccountDatabaseInterface.getInstance().addAccountToDatabase(getApplicationContext(), finalAccount);
    }


}
