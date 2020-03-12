package com.example.madcompetition.BackEnd;

import java.io.Serializable;
import java.net.Inet4Address;

public class AccountInformation implements Serializable {

    private byte[] accountDataPayload;
    private transient Credentials accountCred;
    private Inet4Address addressInfo;

    private int accountId;

    private static final long serialVersionUID = 1L;


    public AccountInformation()
    {

    }

    public AccountInformation(Inet4Address address, int accountID, Credentials accountCred)
    {
        this.addressInfo = address;
        this.accountId = accountID;
        this.accountCred = accountCred;
    }
    /**
     * @return the accountCred
     */
    public Credentials getAccountCred()
    {
        return accountCred;
    }
    /**
     * @param accountCred the accountCred to set
     */
    public void setAccountCred(Credentials accountCred)
    {
        this.accountCred = accountCred;
    }
    /**
     * @return the accountId
     */
    public int getAccountId()
    {
        return accountId;
    }
    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }


    public byte[] getAccountDataPayload() {
        return accountDataPayload;
    }

    public void setAccountDataPayload(byte[] accountDataPayload) {
        this.accountDataPayload = accountDataPayload;
    }

    public Credentials getAccountcred() {
        return accountCred;
    }

    public void setAccountcred(Credentials accountcred) {
        this.accountCred = accountcred;
    }

    public Inet4Address getAdressInfo() {
        return addressInfo;
    }

    public void setAdressInfo(Inet4Address adressInfo) {
        this.addressInfo = adressInfo;
    }


    public String toString()
    {

        String str = "IP Address : " + addressInfo.getHostAddress()
                + "\nAccount ID : " + accountId
                + "\nAccount Credentials : " + accountCred.toString(); ;
        return str;
    }

}
