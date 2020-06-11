package com.example.madcompetition.BackEnd.settings.account;

import java.io.Serializable;
import java.util.Locale;

public class AccessibilitySettings implements Serializable {

    private Locale selectedLocale;
    private boolean mSwipeFeatureEnabled;

    public AccessibilitySettings(Locale us) {
        selectedLocale = us;
        mSwipeFeatureEnabled = true;
    }


    public Locale getSelectedLocale() {
        return selectedLocale;
    }

    public void setSelectedLocale(Locale selectedLocale) {
        this.selectedLocale = selectedLocale;
    }

    public boolean isSwipeFeatureEnabled() {
        return mSwipeFeatureEnabled;
    }

    public void setSwipeFeatureEnabled(boolean mSwipeFeatureEnabled) {
        this.mSwipeFeatureEnabled = mSwipeFeatureEnabled;
    }
}
