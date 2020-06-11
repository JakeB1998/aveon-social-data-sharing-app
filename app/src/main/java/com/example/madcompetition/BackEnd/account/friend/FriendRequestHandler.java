package com.example.madcompetition.BackEnd.account.friend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.Interfaces.DataTransferCallback;
import com.example.madcompetition.BackEnd.account.Account;
import com.example.madcompetition.BackEnd.account.AccountInformation;
import com.example.madcompetition.BackEnd.server.ClientServerMessage;
import com.example.madcompetition.BackEnd.server.MessageType;
import com.example.madcompetition.BackEnd.server.ServerConnectInterface;
import com.example.madcompetition.BackEnd.utils.SerializationOperations;

public class FriendRequestHandler
{
    private static final String LOG = FriendRequestHandler.class.getName();

    public static void prepareFriendRequest(int friendCode)
    {
        // make server request

    }

    public static void prepareFriendRequest(AccountInformation information)
    {
        Account account = AppManager.getInstance().getCurrentAccountLoggedIn();
        FriendRequest request = new FriendRequest(true,AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation(), information);
        ClientServerMessage message = new ClientServerMessage(account.getAccountInformation(), information, MessageType.MessageToUser,SerializationOperations.serializeObjectToBtyeArray(request));
        ServerConnectInterface.getInstance().addClientServerMessageToQue(message);
    }


    public static void handleFriendRequest(final FriendRequest request)
    {

        if (request.isOnResultReturn())
        {

        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(AppManager.getInstance().getAppContext());
            builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    request.setResult(FriendRequestResult.Accepted);
                    finalizeFriendRequestResult(request);
                }
            });
            builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    request.setResult(FriendRequestResult.Dennied);
                    finalizeFriendRequestResult(request);
                }
            });
//
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        //hnadle
    }

    public static void handleFriendRequestResult(FriendRequest result)
    {
        if (result.getResult() == FriendRequestResult.Accepted)
        {
            AppManager.getInstance().getCurrentAccountLoggedIn().getFRIEND_LIST().addFriend(new Friend(result.getTarget()));
            AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(null);
            Log.i(LOG, "Result of friend request from : " + result.getTarget() + " Is : " + result.getResult().toString());
        }
        else if (result.getResult() == FriendRequestResult.Dennied)
        {
            Log.i(LOG, "Result of friend request from : " + result.getTarget() + " Is : " + result.getResult().toString());
            // delete request and send it back



        }
        else if (result.getResult() == FriendRequestResult.Pending)
        {
            Log.i(LOG, "Result of friend request from : " + result.getTarget() + " Is : " + result.getResult().toString());


        }
        else {
            Log.i(LOG, "Result of friend request is null");
            // none or null
           result.setOnResultReturn(false);
        }



    }


    public static void finalizeFriendRequestResult(final FriendRequest friendRequest)
    {
        //hnadle data

        if (friendRequest != null)
        {
            friendRequest.setOnResultReturn(true);



            if (friendRequest.isOnResultReturn())
            {
                sendRequestBackToOrigin(friendRequest);
                Log.i(FriendRequestHandler.class.getName(), "Friend Request sent back to origin");
            }



        }
    }


    public static boolean sendRequestBackToOrigin(FriendRequest request)
    {
        if (request != null) {
            ClientServerMessage m = new ClientServerMessage(request.getTarget(), request.getSource(), MessageType.MessageToUser, SerializationOperations.serializeObjectToBtyeArray(request));
            ServerConnectInterface.getInstance().addClientServerMessageToQue(m);
            return true;
        }
        return false;
    }
}
