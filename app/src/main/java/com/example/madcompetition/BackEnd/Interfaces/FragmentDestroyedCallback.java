package com.example.madcompetition.BackEnd.Interfaces;

import androidx.fragment.app.Fragment;

public interface FragmentDestroyedCallback {
    public void onCall();

    public void onFragmentDestroyed(Fragment fragment);
}
