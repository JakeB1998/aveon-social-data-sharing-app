package com.example.madcompetition.activties;

import android.content.Intent;
import android.os.Bundle;

import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.account.Account;
import com.example.madcompetition.BackEnd.Databases.AccountDatabaseInterface;
import com.example.madcompetition.BackEnd.account.AccountSettings;
import com.example.madcompetition.BackEnd.security.Credentials;
import com.example.madcompetition.BackEnd.security.KeyContract;
import com.example.madcompetition.BackEnd.server.ClientServerMessage;
import com.example.madcompetition.BackEnd.server.MessageSubType;
import com.example.madcompetition.BackEnd.server.MessageType;
import com.example.madcompetition.BackEnd.server.ServerConnectInterface;
import com.example.madcompetition.BackEnd.utils.SerializationOperations;
import com.example.madcompetition.R;
import com.example.madcompetition.activties.Fragments.FragmentTags;
import com.example.madcompetition.activties.Fragments.SwipeTorefreshFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 *
 */
public class ActivityCreateAnAccountFinal extends AppCompatActivity
{

    private Account passedAccount;
    private AccountSettings settings;
    private Credentials cred;

    private Button continueBtn;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(KeyContract.ACCOUNT_KEY)) {
            passedAccount = (Account) getIntent().getExtras().get(KeyContract.ACCOUNT_KEY);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_an_account_final);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        continueBtn = findViewById(R.id.ContinueBtn);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizeAccount(passedAccount);
            }
        });
    }

    /**
     *
     * @param account
     */
    public void finalizeAccount(Account account)
    {
        Account finalAccount = account;
        AccountDatabaseInterface.getInstance().addAccountToDatabase(getApplicationContext(), finalAccount);
        AppManager.getInstance().setCurrentAccountLoggedIn(finalAccount, this);
        Toast.makeText(this, "Finalizing Account", Toast.LENGTH_LONG).show();

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

        /**
         * Called on refresh
         */
        @Override
        public void onRefresh ()
        {
            Log.i(this.getClass().getName(), "Swipe called back overridden here");
        }
    }


}
