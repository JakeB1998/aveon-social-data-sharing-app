package com.example.madcompetition.Activties;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.Account;
import com.example.madcompetition.BackEnd.AccountDatabaseInterface;
import com.example.madcompetition.R;
import com.example.madcompetition.ServerConnectInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityImportAccount extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button importBtn;
    private Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_account);
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
