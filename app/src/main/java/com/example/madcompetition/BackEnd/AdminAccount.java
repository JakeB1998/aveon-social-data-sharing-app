package com.example.madcompetition.BackEnd;

public class AdminAccount extends Account
{

    private AccountSettings requestedLinkedAccountSettings;

    public AdminAccount()

    {
        super();
        setRequestedLinkedAccountSettings(new AccountSettings());
    }

    public AdminAccount(AdminAccount admin)

    {
        super((Account)admin);
        setRequestedLinkedAccountSettings(admin.getRequestedLinkedAccountSettings());
    }

    public AccountSettings getRequestedLinkedAccountSettings() {
        return requestedLinkedAccountSettings;
    }

    public void setRequestedLinkedAccountSettings(AccountSettings requestedLinkedAccountSettings) {
        this.requestedLinkedAccountSettings = requestedLinkedAccountSettings;
    }
}
