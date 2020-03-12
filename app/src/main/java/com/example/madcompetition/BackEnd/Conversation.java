package com.example.madcompetition.BackEnd;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Conversation implements Serializable
{

    private int conversationID;
    private final int ID_LENGTH = 6;
    private Account[] accounts;
    private ArrayList<Message> messages;



            public Conversation()
            {
                conversationID  = generateAccountID();
                setMessages(new ArrayList<Message>(0));

            }
    public Conversation(Account[] accounts)
    {
        this();
        this.setAccounts(accounts);

    }

    private int generateAccountID()
    {
        if (conversationID == 0)
        {
            String id1 = "";
            int id = 0;
            Random ran = new Random();
            for (int i = 0; i < ID_LENGTH; i++)
            {
                id1 += Integer.toString(ran.nextInt(9));
            }

            Log.i("Mine", "Account Id: " + id1.toString());
            id = Integer.parseInt((id1));
            return id;
        }

        return 0;
    }

    public Account[] getAccounts() {
        return accounts;
    }

    public void setAccounts(Account[] accounts) {
        this.accounts = accounts;
    }



    public int getConversationID() {

                if (conversationID == 0)
                {
                    conversationID = this.generateAccountID();
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
    }


    public static boolean doesExist(ArrayList<Conversation> accountConversations, Account[] recipeints)
    {
        Account[] recipeintsNew = null;
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
                            Log.i("Check", Integer.toString(recipeints[j].getAccountID()) + " - -" + Integer.toString(recipeintsNew[i].getAccountID() ) + " True");
                            params[index] = true;
                            index++;
                            continue;
                        }
                        else
                        {
                            Log.i("Check", Integer.toString(recipeints[j].getAccountID()) + " - -" + Integer.toString(recipeintsNew[i].getAccountID())+ " False");
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




}
