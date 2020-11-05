package com.example.madcompetition.backend.Interfaces;

import androidx.fragment.app.Fragment;

public interface FragmentDestroyedCallback {
    void onCall();

    void onFragmentDestroyed(Fragment fragment);
}
