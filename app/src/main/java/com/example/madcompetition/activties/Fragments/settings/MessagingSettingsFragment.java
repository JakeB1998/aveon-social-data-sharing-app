package com.example.madcompetition.activties.Fragments.settings;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.madcompetition.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagingSettingsFragment extends Fragment {

private FrameLayout mLayout;
private ImageButton mBackBtn;
    public MessagingSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayout = (FrameLayout)  inflater.inflate(R.layout.fragment_profile_settings, container, false);
        mBackBtn = mLayout.findViewById(R.id.BackBtn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(MessagingSettingsFragment.this).commit();
            }
        });
        return mLayout;
    }

}
