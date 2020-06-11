package com.example.madcompetition.BackEnd.settings;

import com.example.madcompetition.BackEnd.account.Account;
import com.example.madcompetition.BackEnd.settings.account.AccessibilitySettings;
import com.example.madcompetition.BackEnd.settings.account.AccountSettings;
import com.example.madcompetition.BackEnd.settings.app.AppSettings;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

public class SavedSettings implements Serializable {


    public SavedSettings(Account account, AppSettings appSettings, AccountSettings accountSettings)
    {
        this.account = account;
        this.appSettings = appSettings;
        this.accountSettings = accountSettings;
    }


    private Account account;
    private AppSettings appSettings;
    private AccountSettings accountSettings;
    private AccessibilitySettings accessibilitySettings;

    private LocalDate dateLastSaved;
    private LocalTime timeLastSaved;

    public SavedSettings()
    {
        appSettings = new AppSettings(null);
        accountSettings = new AccountSettings();
        accessibilitySettings = new AccessibilitySettings(Locale.US);
    }


    public boolean saveSettings(AppSettings appSettings, AccountSettings accountSettings)
    {
        this.appSettings = appSettings;
        this.accountSettings = accountSettings;
        dateLastSaved = LocalDate.now();
        timeLastSaved = java.time.LocalTime.now();

        if (this.save())
        {
            return true;
        }

        return false;
    }

    public boolean save()
    {
        if (account != null)
        {
            account.setSavedSettings(this);
            return true;
        }

            return false;
    }


    public AccessibilitySettings getAccessibilitySettings() {
        return accessibilitySettings;
    }

    public void setAccessibilitySettings(AccessibilitySettings accessibilitySettings) {
        this.accessibilitySettings = accessibilitySettings;
    }
}
