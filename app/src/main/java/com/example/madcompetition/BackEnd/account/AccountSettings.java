package com.example.madcompetition.BackEnd.account;

import com.example.madcompetition.BackEnd.AppFeatures;

import java.io.Serializable;
import java.util.ArrayList;

public class AccountSettings implements Serializable
{
    private ArrayList<AppFeatures> enabledFeatures;
    private ArrayList<AppFeatures> LinkedAccountsFeatures;
    private SettingsType settingsType;

    private boolean runInBackround;

    public AccountSettings()
    {

    }

    public AccountSettings(AppFeatures[] appFeatures, SettingsType settingsType)
    {

        this.setFeatures(appFeatures);

    }

    public AccountSettings(AppFeatures[] appFeatures, AppFeatures[] linkedAccountFeatures, SettingsType settingsType)
    {

        this.setFeatures(appFeatures);
        this.setLinkedFeatures(linkedAccountFeatures);
        this.setSettingsType(settingsType);

    }


    public boolean compareFeatures(AppFeatures[] features)
    {
        return getEnabledFeatures().contains(features);

    }
    public boolean compareFeature(AppFeatures feature)
    {
        return getEnabledFeatures().contains(feature);

    }


    public void setFeatures(AppFeatures[] appFeatures)
    {
        setEnabledFeatures(new ArrayList<AppFeatures>(0));
        for(AppFeatures f : appFeatures)
        {
            getEnabledFeatures().add(f);
        }

    }

    public void setLinkedFeatures(AppFeatures[] appFeatures)
    {
        setEnabledFeatures(new ArrayList<AppFeatures>(0));
        for(AppFeatures f : appFeatures)
        {
            getLinkedAccountsFeatures().add(f);
        }

    }


    public void alterFeature(AppFeatures feature , boolean active)
    {
        if (active == false)
        {
            if (getEnabledFeatures().contains(feature))
            {
                getEnabledFeatures().remove(getEnabledFeatures().indexOf(feature));
            }
        }
        else
        {
            if (getEnabledFeatures().contains(feature) == false)
            {
                getEnabledFeatures().add(feature);
            }

        }
    }


    public ArrayList<AppFeatures> getEnabledFeatures() {
        return enabledFeatures;
    }

    public void setEnabledFeatures(ArrayList<AppFeatures> enabledFeatures) {
        this.enabledFeatures = enabledFeatures;
    }

    public ArrayList<AppFeatures> getLinkedAccountsFeatures() {
        return LinkedAccountsFeatures;
    }

    public void setLinkedAccountsFeatures(ArrayList<AppFeatures> linkedAccountsFeatures) {
        LinkedAccountsFeatures = linkedAccountsFeatures;
    }

    public SettingsType getSettingsType() {
        return settingsType;
    }

    public void setSettingsType(SettingsType settingsType) {
        this.settingsType = settingsType;
    }

    public boolean isRunInBackround() {
        return runInBackround;
    }

    public void setRunInBackround(boolean runInBackround) {
        this.runInBackround = runInBackround;
    }
}
