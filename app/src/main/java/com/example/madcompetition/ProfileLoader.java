package com.example.madcompetition;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.madcompetition.BackEnd.Account;
import com.example.madcompetition.BackEnd.Profile;

public class ProfileLoader extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);


        return inflater.inflate(R.layout.activity_profile_viewer, container, false);
    }

    public ProfileLoader() {
    }

    public void loadProfile(Profile profile)
    {

    }

    public void loadProfile(Account account)
    {

    }



}
