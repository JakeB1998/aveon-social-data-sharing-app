package com.example.madcompetition.activties;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.madcompetition.backend.account.profile.Profile;
import com.example.madcompetition.R;

public class ActivityProfileViewer extends AppCompatActivity {
    private Profile profileInView;

    private TextView name;
    private TextView profileName;
    private ImageButton profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_viewer);

        Bundle extras = getIntent().getExtras();

        name = findViewById(R.id.ProfileUsernameView);

        if (extras != null)
        {
            if (extras.containsKey("Profile"))
            {
                profileInView = (Profile)extras.getSerializable("Profile");
                name.setText(profileInView.getProfileAccount().getPersonalInformation().getFullName());

            }
        }
    }
}
