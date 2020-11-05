package com.example.madcompetition.activties;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.madcompetition.backend.AppManager;
import com.example.madcompetition.backend.account.Account;
import com.example.madcompetition.backend.databases.AccountDatabaseInterface;
import com.example.madcompetition.R;
import com.example.madcompetition.activties.fragments.FragmentTags;
import com.example.madcompetition.activties.fragments.SwipeTorefreshFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityImportAccount extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button importBtn;
    private Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_account);
 


        username= findViewById(R.id.UsernameEditText);
        password = findViewById(R.id.PasswordEditText);
        importBtn = findViewById(R.id.ImportBtn);
        backBtn = findViewById(R.id.BackBtn);

        if (AppManager.getInstance().isConnectedToServer())
        {

        }
        else
        {
            handleServerErrorMessage();
        }

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account importAccount = null;
                // check credentials with server



                if (1 == 2)
                {
                    // import account
                    if (importAccount != null) {
                        AccountDatabaseInterface.getInstance().addAccountToDatabase(getApplicationContext(), importAccount);
                    }
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go back
            }
        });


    }

    private void  handleServerErrorMessage()
    {

        Log.i("Application", "Dialog should shoqw");
        new AlertDialog.Builder(this)
                .setTitle("Server Connection Errror")
                .setMessage("You are not connected to the server")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        /*
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

                         */

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Ok", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


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
