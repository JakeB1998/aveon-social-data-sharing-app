package com.example.madcompetition.activties.Fragments.settings;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.madcompetition.BackEnd.Interfaces.FragmentDestroyedCallback;
import com.example.madcompetition.R;

public class AccountSettingsFragment extends Fragment {


    private ImageButton mBackBtn;
    private FrameLayout mLayout;
    private FragmentDestroyedCallback fragmentDestroyedCallback;

    public AccountSettingsFragment(FragmentDestroyedCallback fragmentDestroyedCallback) {
        this.fragmentDestroyedCallback = fragmentDestroyedCallback;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayout = (FrameLayout) inflater.inflate(R.layout.fragment_account_settings, container, false);
        mBackBtn = mLayout.findViewById(R.id.BackBtn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(AccountSettingsFragment.this).commit();
            }
        });
        return mLayout;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (fragmentDestroyedCallback != null)
        {
            fragmentDestroyedCallback.onFragmentDestroyed(this);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
