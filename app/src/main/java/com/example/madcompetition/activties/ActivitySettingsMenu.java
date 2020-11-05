package com.example.madcompetition.activties;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.madcompetition.R;
import com.example.madcompetition.activties.fragments.settings.AssessibilitySettingsFragment;
import com.example.madcompetition.activties.fragments.settings.ProfileSettingsFragment;

public class ActivitySettingsMenu extends AppCompatActivity {

    private View assessiblityBtn;
    private View accountSettings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_settings_menu);
        assessiblityBtn = findViewById(R.id.AssessiblityBtn);
        accountSettings = findViewById(R.id.AccountSettingsBtn);
        accountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ProfileSettingsFragment fragment = new ProfileSettingsFragment(null);
                fragmentTransaction.add(R.id.Main, fragment);
                fragmentTransaction.commit();
            }
        });
        assessiblityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
               AssessibilitySettingsFragment fragment = new AssessibilitySettingsFragment();
                fragmentTransaction.add(R.id.Main, fragment);
                fragmentTransaction.commit();

            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();
        //startActivity(getIntent());
    }
}
