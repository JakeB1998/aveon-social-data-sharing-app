package com.example.madcompetition.BackEnd;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.madcompetition.AccountSubType;
import com.example.madcompetition.AccountType;
import com.example.madcompetition.SerializationOperations;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class Account implements Cloneable, Serializable
{
    public final int Null_Code = -6;


    private transient Profile accountProfile;
    private AccountInformation accountInformation;


    private final int ID_LENGTH = 8;

    private transient Bitmap profilePicture;
    private File fileForBitmapImage;

    private LocalDate creationDate;
    private LocalDate dateLastAccessed;

    private LocationData[] locationPings;
    private ArrayList<Conversation> conversations;
    private ArrayList<Friend> friends;

    private Credentials accountCredentials = new Credentials("Alex", "Tech");
    private Credentials accessCredentials;
    private LocationData lastKnowLocation;

    private AccountType accountType;
    private AccountSubType accountSubType;
    private AccountSettings accountSettings;

    private String fullName;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private  int accountID;
    private boolean accountLinked;

    public Account()
    {





    }
    public Account(String s)  // delete
    {

        if (accountID == 0)
        {
            this.generateAccountID();
        }
        setAccountSettings(new AccountSettings());




        conversations = new ArrayList<>(0);
        fileForBitmapImage = new File(Integer.toString(this.getAccountID()));
        try {
            fileForBitmapImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("File", e.getMessage());
        }

        fullName = s;

    }
    public Account(String s, LocalDate lastDateAccessed)
    {
        this(s);


        dateLastAccessed = lastDateAccessed;
    }
    public Account(Context context,String s, LocalDate lastDateAccessed) // delete this
    {
        this(s);
        fullName = s;
        dateLastAccessed = lastDateAccessed;
    }
    public Account(Context context,String fullName, boolean accountLinked, AccountType accountType, AccountSubType accountSubType,
                   String PhoneNumber, String Email, Credentials accountCred, AccountInformation info)
    {

        this(fullName);


        this.setAccountInformation(info);

        setAccessCredentials(accountCred);

        setAccountSettings(new AccountSettings());


        setDateLastAccessed(LocalDate.now());

        this.fullName = fullName;
        this.accountLinked = accountLinked;
        this.accountType = accountType;
        this.accountSubType = accountSubType;
        this.phoneNumber = PhoneNumber;
        this.email = Email;


    }
    public Account(String fullName, boolean accountLinked, AccountType accountType, AccountSubType accountSubType, String PhoneNumber, String Email)
    {


        this(fullName);
        setAccountSettings(new AccountSettings());

        setDateLastAccessed(LocalDate.now());

        this.fullName = fullName;
        this.accountLinked = accountLinked;
        this.accountType = accountType;
        this.accountSubType = accountSubType;
        this.phoneNumber = PhoneNumber;
        this.email = Email;


    }

    public Account(int accountID, String fullName, boolean accountLinked, AccountType accountType, AccountSubType accountSubType, String PhoneNumber, String Email)
    {
        this(fullName);
        setAccountID(accountID);
        setAccountSettings(new AccountSettings());
        if (getAccountID() == 0)
        {
            setAccountID(generateAccountID());

        }

        this.fullName = fullName;
        this.accountLinked = accountLinked;
        this.accountType = accountType;
        this.accountSubType = accountSubType;
        this.phoneNumber = PhoneNumber;
        this.email = Email;


    }

    public Account(Account account)
    {

        this();
        this.setFullName(account.getFullName());
        this.setAccountID(account.getAccountID());
        this.setAccountType(account.getAccountType());
        this.setAccountLinked(account.isAccountLinked());
        this.setAccountSubType(account.getAccountSubType());

    }

    public  void setAccountData(String[] data)
    {

    }

    public void linkAccount(Account otherAccount)
    {

    }





    public int generateAccountID()
    {

            String id1 = "";
            int id = 0;
            Random ran = new Random();
            for (int i = 0; i < getID_LENGTH(); i++)
            {
                id1 += Integer.toString(ran.nextInt(9));
            }

            Log.i("Mine", "Account Id: " + id1.toString());
            id = Integer.parseInt((id1));
        setAccountID(id);
            return id;




    }




    public int getID_LENGTH() {
        return ID_LENGTH;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public boolean isAccountLinked() {
        return accountLinked;
    }

    public void setAccountLinked(boolean accountLinked) {
        this.accountLinked = accountLinked;
    }

    public AccountSubType getAccountSubType() {
        return accountSubType;
    }

    public void setAccountSubType(AccountSubType accountSubType) {
        this.accountSubType = accountSubType;
    }

    public AccountSettings getAccountSettings() {
        return accountSettings;
    }

    public void setAccountSettings(AccountSettings accountSettings) {
        this.accountSettings = accountSettings;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }






    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<Conversation> conversations) {
        this.conversations = conversations;
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public Bitmap getProfilePicture()
    {


        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) {

        this.profilePicture = profilePicture;

        SerializationOperations.serilizeObjectToFile(fileForBitmapImage, profilePicture);
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getDateLastAccessed() {
        return dateLastAccessed;
    }

    public void setDateLastAccessed(LocalDate dateLastAccessed) {
        this.dateLastAccessed = dateLastAccessed;
    }

    public Credentials getAccountCredentials() {
        return accountCredentials;
    }

    public void setAccountCredentials(Credentials accountCred) {
        this.accountCredentials = new Credentials(accountCred.getUsername(),accountCred.getHashedPassword());
    }

    public Credentials getAccessCredentials() {
        return accessCredentials;
    }

    public void setAccessCredentials(Credentials accessCredentials) {
        this.accessCredentials = accessCredentials;
    }

    public File getFileForBitmapImage() {
        return fileForBitmapImage;
    }

    public void setFileForBitmapImage(File fileForBitmapImage) {
        this.fileForBitmapImage = fileForBitmapImage;
    }

    public Profile getAccountProfile() {
        return accountProfile;
    }

    public void setAccountProfile(Profile accountProfile) {
        this.accountProfile = accountProfile;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if (obj instanceof Account)
        {
            Account account = (Account) obj;
            if (this.getAccountID() == account.getAccountID())
            {
                return  true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    @NonNull
    @Override
    public String toString() {
       String str;
        try {
            str = "Account ID: " + this.accountID
                    + "\n\nFile for bitmap: " + fileForBitmapImage.toString()
                    + "\nFull name: " + this.fullName
                    + "\nCredentials: " + this.accountCredentials.toString()
                    + "\n\nPhone Number: " + this.phoneNumber
                    + "\nEmail: " + this.email
                    // +"\nBitmap byte ccount: " + profilePicture.getByteCount()
                    + "\n";
        } catch (NullPointerException e)
        {
            e.printStackTrace();
            str= "the object was null";
        }
        return str;
    }

    public AccountInformation getAccountInformation() {
        return accountInformation;
    }

    public void setAccountInformation(AccountInformation accountInformation) {
        this.accountInformation = accountInformation;
    }
}
