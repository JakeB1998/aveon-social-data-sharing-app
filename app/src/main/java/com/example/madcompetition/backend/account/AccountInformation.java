package com.example.madcompetition.backend.account;

import android.util.Log;

import com.example.madcompetition.backend.AppManager;
import com.example.madcompetition.backend.account.friend.Friend;
import com.example.madcompetition.backend.security.Credentials;
import com.example.madcompetition.backend.settings.SavedSettings;
import com.example.madcompetition.backend.utils.SerializationOperations;
import com.example.madcompetition.backend.utils.StringUtils;

import java.io.Serializable;
import java.net.Inet4Address;
import java.security.PublicKey;

public class AccountInformation implements Serializable , Comparable<AccountInformation>{

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


    public AccountInformation(Inet4Address address, int accountID, Credentials accountCred, String uniqueDeviceID)
    {
        this.addressInfo = address;
        this.accountId = accountID;
        savedSettings = new byte[0];


        userName = accountCred.getUsername();
        this.setHashedPassword(accountCred.getHashedPassword());
        this.setPublicKey(accountCred.getPublicKey());
        this.friendRequestId = Integer.parseInt(StringUtils.generateID(NUMBER_OF_CHARACTERS).trim());
        this.setUniqueDeviceID(uniqueDeviceID);
    }
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


    public int getFriendRequestId() {
        if (friendRequestId == 0 || friendRequestId == -1 || Integer.toString(friendRequestId).length() != NUMBER_OF_CHARACTERS)
        {
            this.friendRequestId = Integer.parseInt(StringUtils.generateID(NUMBER_OF_CHARACTERS).trim());
        }
        return friendRequestId;
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

    public String getTextRepresentation() {
        Friend friend = null;

        if ((friend = AppManager.getInstance().getCurrentAccountLoggedIn().getFRIEND_LIST().getFriend(this)) != null) {
            if (friend.getNickName().length() <= 0) {
                Log.i(this.getClass().getName(), "Attemted to use null nickname, was auto corrected by program.");
                return getUserName();
            }
            return friend.getNickName();
        } else {
            return getUserName();
        }
    }

    public byte[] getSavedSettings() {
        return savedSettings;
    }

    public void setSavedSettings(SavedSettings savedSettings) {
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

        return newInfo.getAccountId() == this.getAccountId();
    }
    @Override
    public int compareTo(AccountInformation o) {
        int result = 0;

        if (o != null) {
        }


        return this.getUserName().compareToIgnoreCase(o.getUserName());

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
        if (friendList == null)
        {
            friendList = new byte[0];
            Log.i("ASS", "Friend list array byte is null");
        }
        return friendList;
    }

    public void setFriendList(byte[] friendList) {
        this.friendList = friendList;
    }

    public void setUniqueDeviceID(String uniqueDeviceID) {
        this.uniqueDeviceID = uniqueDeviceID;
    }
}
