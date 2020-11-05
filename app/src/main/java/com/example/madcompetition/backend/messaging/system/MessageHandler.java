package com.example.madcompetition.backend.messaging.system;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.madcompetition.backend.Interfaces.FragmentDestroyedCallback;
import com.example.madcompetition.backend.account.Account;
import com.example.madcompetition.backend.AppManager;
import com.example.madcompetition.backend.account.AccountInformation;
import com.example.madcompetition.backend.server.ClientServerMessage;
import com.example.madcompetition.backend.server.MessageType;
import com.example.madcompetition.backend.server.ftp.FtpServerConnectInterface;
import com.example.madcompetition.backend.server.ftp.FileData;
import com.example.madcompetition.backend.utils.SerializationOperations;
import com.example.madcompetition.backend.server.ServerConnectInterface;
import com.example.madcompetition.backend.utils.StringUtils;
import com.example.madcompetition.R;
import com.example.madcompetition.activties.ActivityMessagingInterface;

import java.util.ArrayList;

public class MessageHandler
{
    private static FragmentDestroyedCallback callback1;

    public static void registerForUpdates(FragmentDestroyedCallback callback)
    {
        callback1 = callback;
    }


    /**
     *
     * @param message
     */
    public static void handleIncomingDataMessage(Message message)
    {
        final String CHANNEL_ID = "123545";
        Context context = AppManager.getInstance().getAppContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ("The channel");
            String description = ("description");
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =context. getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Account currentAccount = AppManager.getInstance().getCurrentAccountLoggedIn();
        ArrayList<Conversation> conversations =  currentAccount.getConversations();

        Log.i("Message Handler", "Conversation id from recieved message : " + message.getConversationId());
        Log.i("Message Handler", "message type: " + message.getMessage());

        if (message != null) {

            Conversation targetConvo = findConversation(message);
            if (conversations.contains(targetConvo) == false) {
                //currentAccount.getConversations().add(targetConvo);
                Log.i("Message Handler", "New Conversation added");

            } else {
                targetConvo = conversations.get(conversations.indexOf(targetConvo));
                if (targetConvo.getMessages().contains(message))
                {
                    updateMessageObject(message);
                    Log.i("ASSQ", "Message qued to update");
                }
                else {
                    targetConvo.addMessage(message);
                    targetConvo.setConversationID(message.getConversationId());
                }
                Log.i("Message Handler", "conversation exists after creating ?");
            }
            String messageNotif = "";
            if (message.getMessage() == null)
            {
                messageNotif = message.getMessageFileData().getFile().toPath().toString();
            }
            else
            {
                messageNotif = message.getMessage();
            }
            Intent intent = new Intent(context, ActivityMessagingInterface.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.default_message_icon_foreground)
                    .setContentTitle(message.getSender().getUserName())
                    .setContentText(message.getSender().getUserName())
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message.getMessage()))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(12345, builder.build());
        }

        if (callback1 != null)
        {

            callback1.onCall();
        }


    }

    /**
     *
     * @param convo
     * @param account
     * @return
     */
    public static boolean addConversationToAccount(Conversation convo, Account account)
    {
        boolean valid = false;

        if (account.equals(AppManager.getInstance().getCurrentAccountLoggedIn()))
        {
            if (Conversation.doesExist(account.getConversations(), convo.getAccounts()) == false)
            {
                account.getConversations().add(convo);

                Log.i(MessageHandler.class.getName(), "Conversation added to account");
                if (account.getConversations().contains(convo))
                {
                    valid = true;
                }
            }
            else
            {
                Log.i(MessageHandler.class.getName(), "Conversation already exists, Can not add");
            }
        }
        else
        {
            Log.e(MessageHandler.class.getName() + "- Security", "User trying to add a conversation to an account they do not have access to");
        }



       return valid;
    }


    /**
     *
     * @param message
     * @return
     */
    public static Conversation createConversation(Message message)
    {
        Conversation createdConvo = null;
        createdConvo = new Conversation(null);


        createdConvo.setAccounts(message.getRecipeints());
        createdConvo.addMessage(message);
        createdConvo.getConversationID();
        return createdConvo;
    }

    /**
     *
     * @param recepients
     * @return
     */
    public static Conversation createConversation(AccountInformation[] recepients)
    {
        Conversation createdConvo = null;
        createdConvo = new Conversation(recepients);



        createdConvo.setAccounts(recepients);

        createdConvo.getConversationID();
        return createdConvo;
    }


    /**
     *
     * @param message
     * @return
     */
    public static Conversation findConversation(Message message)
    {
        Conversation convo = null;
        Account currentAccount = AppManager.getInstance().getCurrentAccountLoggedIn();

        ArrayList<Conversation> conversations =  currentAccount.getConversations();

       Conversation convo1 = Conversation.getConversation(conversations, message.getRecipeints());
       if (convo1 != null)
       {
           return convo1;
       }



        if (convo == null)
        {
            Log.i("Ass", "No convo exists");
            convo = createConversation(message);
            convo.setConversationID(message.getConversationId());
            convo.setAccounts(message.getRecipeints());
            AppManager.getInstance().getCurrentAccountLoggedIn().getConversations().add(convo);
            convo.addMessage(message);
        }
        else
        {
            convo.addMessage(message);
        }

        currentAccount = null;
        conversations = null;
        return convo;

    }


    /**
     *
     * @param message
     * @return
     */
    public static boolean updateMessageObject(Message message)
    {
        if (AppManager.getInstance().getCurrentAccountLoggedIn() != null) {
            Account loggedIn = AppManager.getInstance().getCurrentAccountLoggedIn();
            Conversation convo = findConversation(message);

            if (convo != null) {
                int index = loggedIn.getConversations().indexOf(convo);
                int index2 = loggedIn.getConversations().get(index).getMessages().indexOf(message);
                loggedIn.getConversations().get(index).getMessages().get(index2).setMessageObject(message);
               // loggedIn.getConversations().get(index).getMessages().get(index2)
            }
            return true;
        }
        else
        {

        }

        return false;

    }


    /**
     *
     * @param message
     * @param convo
     * @return
     */
    public static boolean sendMessage(Message message, Conversation convo)
    {
        AccountInformation[] rec = message.getRecipeints();
        message.setConversationId(convo.getConversationID());
        Log.i("DICK", "Conversation id : " + message.getConversationId());
        ClientServerMessage csMessage = null;
        if (message instanceof TextMessage || message instanceof LocationMessage) {
            for (int i = 0; i < rec.length; i++) {
                Log.i("DICK", "Convo message sent " + rec[i].getUserName());
                if (rec[i].equals(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation()) == false) {
                    csMessage = new ClientServerMessage(message.getSender(), rec[i], MessageType.MessageToUser, SerializationOperations.serializeObjectToBtyeArray(message));
                    ServerConnectInterface.getInstance().addClientServerMessageToQue(csMessage);
                    addMessage(message, convo);
                    Log.i("MessageHandler", "Message sucesffuly sent");
                    return true;
                }


            }
        }
        else
        {

            FileData data = message.getMessageFileData();
            data.setOneTimeDownloadKey(StringUtils.generateID(8));
            message.setMessageFileData(data);
            if (data != null) {
                for (int i = 0; i < rec.length; i++) {
                    Log.i("DICK", "Convo message sent " + rec[i].getUserName());
                    if (rec[i].equals(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation()) == false) {
                        Log.i(MessageHandler.class.getName(), "File path : " + data.getFilePath());
                        csMessage = new ClientServerMessage(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation(),
                                rec[i], MessageType.FileMessage,
                                SerializationOperations.serializeObjectToBtyeArray(message));
                        ClientServerMessage ftpMessage = new ClientServerMessage(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation(),
                                rec[i], MessageType.FileMessage,
                                SerializationOperations.serializeObjectToBtyeArray(message.getMessageFileData()));
                        ServerConnectInterface.getInstance().addClientServerMessageToQue(csMessage);
                        FtpServerConnectInterface.getInstance().startInteraction(ftpMessage);
                        addMessage(message, convo);
                        return true;
                    }
                }


            }
            else
            {
                Log.i(MessageHandler.class.getName(), "File data was null");
            }
        }


        return false;
    }


    /**
     *
     * @param message
     * @param convo
     * @return
     */
    public static boolean addMessage(Message message, Conversation convo)
    {
        if (AppManager.getInstance().getCurrentAccountLoggedIn() != null) {
            Account loggedIn = AppManager.getInstance().getCurrentAccountLoggedIn();


            if (convo != null) {
                int index = loggedIn.getConversations().indexOf(convo);


                Log.i("Ass", "Index : " + index);

                loggedIn.getConversations().get(index).addMessage(message);
                return true;

            }

        }
        else
        {

        }


        return false;
    }





}
