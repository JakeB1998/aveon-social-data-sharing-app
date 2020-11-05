package com.example.madcompetition.backend.server;

import java.io.Serializable;

public class CredentialsRequest implements Serializable {

    private String username;
    private String hashedPassword;
    private byte[] accountInformation;

    private boolean onReturn;

    public CredentialsRequest(String username)
    {
        setOnReturn(false);
        this.setUsername(username);

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public byte[] getAccountInformation() {
        return accountInformation;
    }

    public void setAccountInformation(byte[] accountInformation) {
        this.accountInformation = accountInformation;
    }

    public boolean isOnReturn() {
        return onReturn;
    }

    public void setOnReturn(boolean onReturn) {
        this.onReturn = onReturn;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
