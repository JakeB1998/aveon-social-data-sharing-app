package com.example.madcompetition.Activties;

import android.app.Activity;
import android.os.Bundle;

import com.example.madcompetition.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityDebug extends AppCompatActivity
{

    TextView locationDebug;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
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

        locationDebug = findViewById(R.id.Debug);
        backBtn = findViewById(R.id.BackBTN);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        locationDebug.setText(savedInstanceState.getString("DebugText"));
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(!TextUtils.isEmpty(locationDebug.getText())){
            outState.putString("DebugText", locationDebug.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }



}
