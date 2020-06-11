package com.example.madcompetition.BackEnd.messaging.system;

import android.util.Log;

import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.account.Account;
import com.example.madcompetition.BackEnd.account.AccountInformation;
import com.example.madcompetition.BackEnd.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Conversation implements Serializable
{

    private int conversationID;
    private final int ID_LENGTH = 6;
    private AccountInformation[] accounts;
    private ArrayList<Message> messages;

    public Conversation(AccountInformation[] accounts)
    {
        this.setAccounts(accounts);
        if (conversationID == 0)
        {
            conversationID = Integer.parseInt(StringUtils.generateID(ID_LENGTH));
        }
        if (messages == null) {
            setMessages(new ArrayList<Message>(0));
        }

    }



    public AccountInformation[] getAccounts() {
        return accounts;
    }

    public void setAccounts(AccountInformation[] accounts) {
        this.accounts = accounts;
    }



    public int getConversationID() {

                if (conversationID == 0)
                {
                    conversationID = Integer.parseInt(StringUtils.generateID(ID_LENGTH));
                }
                return conversationID;
    }

    public void setConversationID(int conversationID) {
        this.conversationID = conversationID;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message)
    {
        messages.add(message);
        message.setConversationId(this.conversationID);
    }

    public Message getMostRecentMessage()
    {
        if (messages != null) {
            if (messages.size() > 0) {
                return messages.get(messages.size() - 1);
            }
        }

        return null;
    }



    public static boolean doesExist(ArrayList<Conversation> accountConversations, AccountInformation[] recipeints)
    {
        AccountInformation[] recipeintsNew = null;
        boolean exists = false;



        for (Conversation convo : accountConversations)
        {
            recipeintsNew = convo.getAccounts();
            boolean[] params = new boolean[recipeints.length];


            boolean good = false;
            if (recipeints.length == recipeintsNew.length) {
                int index = 0;
                for (int i = 0; i < recipeintsNew.length; i++)
                {


                    for (int j = 0; j < recipeints.length; j++)
                    {
                        if (recipeints[j].equals(recipeintsNew[i]))
                        {

                            if (index < recipeints.length) {
                                params[index] = true;
                                index++;
                            }


                        }
                        else
                        {
                            Log.i("Check", Integer.toString(recipeints[j].getAccountId()) + " - -" + Integer.toString(recipeintsNew[i].getAccountId())+ " False");
                        }
                    }

                }

                boolean notTrue = false;
                for (int i = 0; i < params.length; i++)
                {
                    if (params[i] != true)
                    {
                        notTrue = true;
                    }
                }

                if (notTrue == false)
                {
                    exists = true;
                    break;

                }
            }
        }

        return exists;
    }
    public static Conversation getConversation(ArrayList<Conversation> accountConversations, AccountInformation[] recipeints)
    {
        AccountInformation[] recipeintsNew = null;
        boolean exists = false;



        for (Conversation convo : accountConversations)
        {
            recipeintsNew = convo.getAccounts();
            boolean[] params = new boolean[recipeints.length];


            boolean good = false;
            if (recipeints.length == recipeintsNew.length) {
                int index = 0;
                for (int i = 0; i < recipeintsNew.length; i++)
                {


                    for (int j = 0; j < recipeints.length; j++)
                    {
                        if (recipeints[j].equals(recipeintsNew[i]))
                        {

                            if (index < recipeints.length) {
                                params[index] = true;
                                index++;
                            }


                        }
                        else
                        {
                            Log.i("Check", Integer.toString(recipeints[j].getAccountId()) + " - -" + Integer.toString(recipeintsNew[i].getAccountId())+ " False");
                        }
                    }

                }

                boolean notTrue = false;
                for (int i = 0; i < params.length; i++)
                {
                    if (params[i] != true)
                    {
                        notTrue = true;
                    }
                }

                if (notTrue == false)
                {
                    exists = true;
                    return convo;

                }
            }
        }

    return null;
    }

public boolean equals(Object object)
{
    if (object instanceof Conversation)
    {
        Conversation convo = (Conversation)object;
        return Conversation.doesExist(AppManager.getInstance().getCurrentAccountLoggedIn().getConversations(), convo.getAccounts());
    }
    return  false;
}



}
