package com.example.madcompetition.activties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.madcompetition.R;

public class StartUpScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_screen);
        loadNextActivity();
    }


    public void loadNextActivity()
    {
        startActivity(new Intent(this, ActivityProfileSelection.class));


    }

}
