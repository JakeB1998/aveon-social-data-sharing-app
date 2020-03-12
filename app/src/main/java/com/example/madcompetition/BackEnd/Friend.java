package com.example.madcompetition.BackEnd;

import com.example.madcompetition.BackEnd.Account;

import java.io.Serializable;

public class Friend implements Serializable {

    private Account friendAccount;

    public Friend()
    {

    }

    public Friend(Account account)
    {
        this.friendAccount = account;
    }


}
