/*
 * File name:  Account.java
 *
 * Programmer : Jake Botka
 * ULID: JMBOTKA
 *
 * Date: Feb 11, 2020
 *
 * Out Of Class Personal Program
 */
package com.example.madcompetition.BackEnd.account;

import java.io.Serializable;
import java.net.Inet4Address;
import java.security.PublicKey;
import java.time.LocalDate;
import java.util.Random;

import com.example.madcompetition.BackEnd.security.Credentials;
import com.example.madcompetition.BackEnd.utils.SerializationOperations;
import com.example.madcompetition.BackEnd.utils.StringUtils;

/**
 * <insert class description here>
 *
 * @author Jake Botka
 *
 */
public class AccountInformation implements Serializable, Comparable{



    private final int NUMBER_OF_CHARACTERS = 6;

    private transient PublicKey publicKey;
    private Inet4Address addressInfo;
    private  String uniqueDeviceID;
    private String userName;
    private String hashedPassword;

    private int accountId;
    private int friendRequestId;


    private byte[] savedSettings;
    private byte[] friendList;
    private PersonalInformation personalInformation;
    private String profilePicture;

    private static final long serialVersionUID = 1L;


    /**
     * @return the accountCred

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




    public Inet4Address getAdressInfo() {
        return addressInfo;
    }

    public void setAdressInfo(Inet4Address adressInfo) {
        this.addressInfo = adressInfo;
    }



    public String getUniqueDeviceID() {
        return uniqueDeviceID;
    }


   

    public void setFriendRequestId(int friendRequestId) {
        this.friendRequestId = friendRequestId;
    }

    public PersonalInformation getPersonalInformation() {
        return personalInformation;
    }

    public void setPersonalInformation(PersonalInformation personalInformation) {
        this.personalInformation = personalInformation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String uri) {
        this.profilePicture = uri;
    }


    public byte[] getSavedSettings() {
        return savedSettings;
    }

    public void setSavedSettings(byte[] savedSettings) {
        this.savedSettings = SerializationOperations.serializeObjectToBtyeArray(savedSettings);

    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
@Override
    public boolean equals(Object info)
    {
        AccountInformation newInfo = (AccountInformation) info;

        if (newInfo.getAccountId() == this.getAccountId())
        {
            return true;
        }
        else {
            return false;
        }
    }

    public String toString()
    {

        if (addressInfo == null)
        {
            String str = "IP Address : null"
                    + "\nAccount ID : " + accountId;
            return str;
        }
        else {

            String str = "IP Address : " + addressInfo.getHostAddress()
                    + "\nAccount ID : " + accountId;
            return str;
        }



    }

    public byte[] getFriendList() {
        return friendList;
    }

    public void setFriendList(byte[] friendList) {
        this.friendList = SerializationOperations.serializeObjectToBtyeArray(friendList);
    }
	@Override
	public int compareTo(Object arg0)
	{
		// TODO Auto-generated method stub
		return 0;
	}
    
}
