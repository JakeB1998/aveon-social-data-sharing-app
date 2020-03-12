package com.example.madcompetition.BackEnd;

import android.util.Log;

import java.util.ArrayList;

public class MessageHandler
{

    public static void handleIncomingDataMessage(Message message)
    {
        Account currentAccount = AppManager.getInstance().getCurrentAccountLoggedIn();

        ArrayList<Conversation> conversations =  currentAccount.getConversations();
        if (message instanceof TextMessage)
        {
            TextMessage text = (TextMessage)message;

            Conversation targetConvo = findConversation(text);

        }
    }

    public static boolean addConversationToAccount(Conversation convo, Account account)
    {
        boolean valid = false;

        if (account.equals(AppManager.getInstance().getCurrentAccountLoggedIn())) {
            if (Conversation.doesExist(account.getConversations(), convo.getAccounts()) == false) {
                account.getConversations().add(convo);

                if (account.getConversations().contains(convo)) {
                    valid = true;
                }
            }
            else
            {
                Log.i("Application", "Conversation already exists, Can not add");
            }
        }
        else
        {
            Log.e("Security", "User trying to add a conversation to an account they do not have access to");
        }



       return valid;
    }


    public static Conversation createConversation(Message message)
    {
        Conversation createdConvo = null;
        createdConvo = new Conversation();


        createdConvo.setAccounts(message.getRecipeints());
        createdConvo.addMessage(message);
        createdConvo.getConversationID();
        return createdConvo;
    }
    public static Conversation createConversation(Account[] recepients)
    {
        Conversation createdConvo = null;
        createdConvo = new Conversation();


        createdConvo.setAccounts(recepients);

        createdConvo.getConversationID();
        return createdConvo;
    }



    public static Conversation findConversation(Message message)
    {
        Conversation convo = null;
        Account currentAccount = AppManager.getInstance().getCurrentAccountLoggedIn();

        ArrayList<Conversation> conversations =  currentAccount.getConversations();

        for (Conversation c : conversations)
        {
           if (c.getConversationID() == message.getConversationId())
           {
               convo = c;
               break;
           }

        }

        if (convo == null)
        {
            convo = createConversation(message);
        }

        currentAccount = null;
        conversations = null;
        return convo;

    }



}
