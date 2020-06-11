package com.example.madcompetition.BackEnd.server;

import android.util.Log;

import com.example.madcompetition.BackEnd.account.AccountInformation;

import java.util.ArrayList;

public class AccountInformationTemporaryDataContainer {
    private static final AccountInformationTemporaryDataContainer ourInstance = new AccountInformationTemporaryDataContainer();


    private ArrayList<AccountInformation> dataContainer;

    public static AccountInformationTemporaryDataContainer getInstance() {
        return getOurInstance();
    }

    private AccountInformationTemporaryDataContainer() {
        setDataContainer(new ArrayList<AccountInformation>(0));
    }

    public static AccountInformationTemporaryDataContainer getOurInstance() {
        return ourInstance;
    }


    public void addData(AccountInformation data)
    {
        if (data != null)
        {
            if (getDataContainer().contains(data) == false) { // already in data container
                getDataContainer().add(data);
            }
            else
            {
                Log.i(this.getClass().getName(), "Data already in temp database");
            }
        }

    }

    public AccountInformation getData(AccountInformation information)
    {
        if (getDataContainer().contains(information))
        {
            int index = getDataContainer().indexOf(information);
            if (index != -1 && index > 0)
            {
                return getDataContainer().get(index);
            }
        }
        return null;
    }

    public AccountInformation getData(String data)
    {
        for (AccountInformation account : getDataContainer())
        {
            if (data.equals(account.getUserName()))
            {
                return account;
            }
        }
        return  null;
    }


    public ArrayList<AccountInformation> getDataContainer() {
        return dataContainer;
    }

    public void setDataContainer(ArrayList<AccountInformation> dataContainer) {
        this.dataContainer = dataContainer;
    }
}
