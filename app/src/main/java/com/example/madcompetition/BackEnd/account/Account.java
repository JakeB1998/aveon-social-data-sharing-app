package com.example.madcompetition.BackEnd.account;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.account.friend.Friend;
import com.example.madcompetition.BackEnd.account.friend.FriendList;
import com.example.madcompetition.BackEnd.account.profile.Profile;
import com.example.madcompetition.BackEnd.security.Credentials;
import com.example.madcompetition.BackEnd.Databases.AccountDatabaseInterface;
import com.example.madcompetition.BackEnd.LocationData;
import com.example.madcompetition.BackEnd.server.ClientServerMessage;
import com.example.madcompetition.BackEnd.messaging.system.Conversation;
import com.example.madcompetition.BackEnd.settings.SavedSettings;
import com.example.madcompetition.BackEnd.utils.SerializationOperations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class Account implements Cloneable, Serializable
{
    private static final long serialVersionUID = 1L;
    private final int Null_Code = -6;

    private transient Profile accountProfile;

    private transient Bitmap profileBitmap;
    private File bitmapFile;
    private AccountInformation accountInformation;
    private PersonalInformation personalInformation;

    private boolean accountlocked;


    private final int ID_LENGTH = 8;
    private LocalDate creationDate;
    private LocalDate dateLastAccessed;
    private LocalDate lastLoggedIn;

    private LocationData[] locationPings;
    private LocationData lastKnowLocation;

    private ArrayList<Conversation> conversations;
    private FriendList friendList;

    private Credentials accountCredentials;
    private Credentials accessCredentials;

    private ArrayList<ClientServerMessage> serverMessagesQue;
    private ArrayList<ClientServerMessage> ftpMessageQue;


    private AccountType accountType;
    private AccountSubType accountSubType;
    private AccountSettings accountSettings;
    private SavedSettings savedSettings;


    private  int accountID;

    public Account(AccountInformation information)
    {
        conversations = new ArrayList<>(0);
        serverMessagesQue = new ArrayList<>(0);
        setFtpMessageQue(new ArrayList<ClientServerMessage>(0));
        setAccountInformation(information);
        //setProfilePicture(information.getProfilePicture());
        setAccountID(information.getFriendRequestId());
        setPersonalInformation(information.getPersonalInformation());
        setAccountCredentials(new Credentials(information.getUserName(), information.getHashedPassword()));
        friendList = (FriendList) SerializationOperations.deserializeToObject(accountInformation.getFriendList());
        if (information.getSavedSettings() != null) {
            setSavedSettings((SavedSettings) SerializationOperations.deserializeToObject(information.getSavedSettings()));
        }




    }


    public Account(String fullName, Context context)  // delete
    {

        if (getAccountID() == 0)
        {
            this.generateAccountID();
        }

        if (friendList == null)
        {
            friendList = new FriendList(0);
        }

        //personalInformation = new PersonalInformation(null,null,null,null);
       setSavedSettings(new SavedSettings());

        conversations = new ArrayList<>(0);
        setServerMessagesQue(new ArrayList<ClientServerMessage>(0));
        creationDate = LocalDate.now();
        dateLastAccessed = LocalDate.now();

        bitmapFile = new File(context.getFilesDir(),getAccountID() + ".png");
        try {
            getBitmapFile().createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        personalInformation.setFullName(fullName);

    }

    public Account(Account account)
    {


        /*
        this.setFullName(account.getFullName());
        this.setAccountID(account.getAccountID());
        this.setAccountType(account.getAccountType());
        this.setAccountLinked(account.isAccountLinked());
        this.setAccountSubType(account.getAccountSubType());

         */

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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





    public ArrayList<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<Conversation> conversations) {
        this.conversations = conversations;
    }

    public Bitmap getProfilePicture()
    {


        if (accountProfile != null) {
            return accountProfile.getProfileImage();

        }
        else
        {

            /*
            if (getAccountInformation().getProfilePicture() != null)
            {
                //accountProfile.setProfileImage(getAccountInformation().getProfilePicture());
                //return BitmapFactory.getAccountInformation().getProfilePicture();
            }

             */
        }
        return  null;
    }

    public void setProfilePicture(Bitmap profilePicture) {


        if (accountProfile != null) {
            accountProfile.setProfileImage(profilePicture);
        }
        if (getAccountInformation() != null) {
//            getAccountInformation().setProfilePicture(profilePicture);
        }




    }

    public AccountSettings getAccountSettings() {
        return accountSettings;
    }

    public void setAccountSettings(AccountSettings accountSettings) {
        this.accountSettings = accountSettings;
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


    public Profile getAccountProfile() {
        return accountProfile;
    }

    public void setAccountProfile(Profile accountProfile) {

        this.accountProfile = accountProfile;
        setProfilePicture(accountProfile.getProfileImage());
    }

    public boolean saveAccount(Context context)
    {
        if (context == null)
        {
            AppManager manager = AppManager.getInstance();
            if (manager.getAppContext() != null) {
                context = null;
                context = manager.getAppContext();
            }
        }
        if (AccountDatabaseInterface.getInstance().updateAccountToDatabase(context,this))
        {
            dateLastAccessed = LocalDate.now();
            Log.i("Account", "Save to database successful");
            return true;
        }
        else
        {
            Log.i("Account", "Save to database failed");
            return false;
        }



    }
    public AccountInformation getAccountInformation() {



        return accountInformation;
    }

    public void setAccountInformation(AccountInformation accountInformation) {
        this.accountInformation = accountInformation;
        accountInformation.setUserName(this.accountInformation.getUserName());
    }

    public void FillFriendsList(Friend[] friends)
    {
        if (friends != null) {
            for (Friend friend : friends) {
                getFRIEND_LIST().addFriend(friend);
            }
        }
    }

    public FriendList getFRIEND_LIST() {
        if (friendList == null)
        {
            friendList = new FriendList(0);
        }
        return friendList;
    }

    public SavedSettings getSavedSettings() {

        if (savedSettings == null)
        {
            savedSettings = new SavedSettings();
        }
        return savedSettings;
    }

    public void setSavedSettings(SavedSettings savedSettings) {
        this.savedSettings = savedSettings;
    }


    public static Account[] getAllRecieverRecipeints(Account[] recipients)
    {
        Account[] xx = new Account[recipients.length];
        int index = 0;

        for (int i = 0; i < xx.length; i++)
        {
            if (recipients[i].equals(AppManager.getInstance().getCurrentAccountLoggedIn()) == false)
            {
                xx[index] = recipients[i];
                index++;
                Log.i("ASS", "check");
            }
        }

        return xx;
    }

    public PersonalInformation getPersonalInformation() {
        return personalInformation;
    }

    public void setPersonalInformation(PersonalInformation personalInformation) {
        this.personalInformation = personalInformation;
        if (getAccountInformation() != null)
        {
            getAccountInformation().setPersonalInformation(personalInformation);
        }
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
       String str = "";
        try {
            str = "Account ID: " + this.getAccountID()


                    // +"\nBitmap byte ccount: " + profilePicture.getByteCount()
                    + "\n";
        } catch (NullPointerException e)
        {
            e.printStackTrace();
            str= "the object was null";
        }
        return str;
    }


    public int getNull_Code() {
        return Null_Code;
    }

    public ArrayList<ClientServerMessage> getServerMessagesQue() {
        return serverMessagesQue;
    }

    public void setServerMessagesQue(ArrayList<ClientServerMessage> serverMessagesQue) {
        this.serverMessagesQue = serverMessagesQue;
    }

    public ArrayList<ClientServerMessage> getFtpMessageQue() {
        return ftpMessageQue;
    }

    public void setFtpMessageQue(ArrayList<ClientServerMessage> ftpMessageQue) {
        this.ftpMessageQue = ftpMessageQue;
    }

    public Bitmap getProfileBitmap() {
        if (profileBitmap == null)
        {
            if (getBitmapFile() != null) {
                profileBitmap = BitmapFactory.decodeFile(getBitmapFile().toPath().toString());
            }
        }
        return profileBitmap;
    }

    public void setProfileBitmap(Bitmap profileBitmap, Context context) {
        this.profileBitmap = profileBitmap;
        FileOutputStream out = null;
        if (getBitmapFile() == null)
        {
            this.bitmapFile = new File(context.getFilesDir(), getAccountID() + ".png");
        }
        try {
            out = new FileOutputStream(getBitmapFile());
            this.profileBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public File getBitmapFile() {
        return bitmapFile;
    }

}
