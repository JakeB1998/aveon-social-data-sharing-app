package com.example.madcompetition;

import android.util.Log;

import com.example.madcompetition.BackEnd.Account;
import com.example.madcompetition.BackEnd.AccountInformation;
import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.ClientServerMessage;
import com.example.madcompetition.BackEnd.Message;
import com.example.madcompetition.BackEnd.MessageHandler;
import com.example.madcompetition.BackEnd.MessageType;
import com.example.madcompetition.BackEnd.TextMessage;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class ServerConnectInterface implements Runnable
{
    private static final ServerConnectInterface ourInstance = new ServerConnectInterface();

    public static ServerConnectInterface getInstance() {
        return getOurInstance();
    }
    public static ServerConnectInterface getOurInstance() {
        return ourInstance;
    }

    private ArrayList<String> requestQue;
    private ArrayList<ClientServerMessage> messageQue;
    private Account currentAccount;

    private boolean isConnected;

    private BufferedReader inFromUser;
    private DataInputStream inFromServer;
    private Socket clientSocket;
    private DataOutputStream  outToServer;
    private boolean sendAlive;

    private ServerConnectInterface()
    {
        setConnected(false);
        requestQue = new ArrayList<>(0);
        messageQue = new ArrayList<>(0);



    }

    public boolean connect()
    {
        boolean bool;

        try
        {

            clientSocket = new Socket(ServerContract.SERVER_IP_ADRESS, ServerContract.SERVER_PORT_NUMBER);
           // clientSocket = new Socket("localhost", ServerContract.SERVER_PORT_NUMBER);
            clientSocket.setTcpNoDelay( true );
            setConnected(true);
            bool = true;
            inFromServer = new DataInputStream( clientSocket.getInputStream());
            outToServer = new DataOutputStream(  clientSocket.getOutputStream());
            AppManager.getInstance().setConnectedToServer(true);

        } catch (UnknownHostException e1)
        {
            AppManager.getInstance().setConnectedToServer(false);
            bool = false;
            // TODO Auto-generated catch block
            e1.printStackTrace();
            Log.e("Server", e1.getMessage());
        } catch (IOException e1)
        {
            AppManager.getInstance().setConnectedToServer(false);
            bool = false;
            // TODO Auto-generated catch block
            e1.printStackTrace();
            Log.e("Server", e1.getMessage());
        }

        return  bool;

    }


    @Override
    public void run()
    {
        try
        {
            Inet4Address net = (Inet4Address) Inet4Address.getLocalHost();
            AppManager.getInstance().setDeviceAddress(net);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        boolean bool = connect();
        Log.i("Server", "Server Connection Status : " + bool);

        if (isConnected)
        {
           Account[] r = {new Account("Alice"), new Account("Alex")};
            TextMessage t = new TextMessage("NEEEEEEERRRRRRRRRDDDDD", r[0], r);
            ClientServerMessage x = new ClientServerMessage(new AccountInformation(), new AccountInformation(), MessageType.TextToUser, SerializationOperations.serializeObjectToBtyeArray((Message)t));
            requestToSendMessage(x);
          //  requestToSendMessage(x);
            while (true)
            {

                long time = System.currentTimeMillis();
                long newTime = time + 5000;




                runServerInteraction();

                if (clientSocket.isClosed() == false) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            }
        }

    }

    public void runServerInteraction()
    {
        Account current = AppManager.getInstance().getCurrentAccountLoggedIn();
        current.setAccountInformation(new AccountInformation(AppManager.getInstance().getDeviceAddress(), current.getAccountID(), current.getAccountCredentials()));
        AccountInformation information = current.getAccountInformation();

        Log.i("Mine", information.getAdressInfo().toString());
        ObjectOutputStream objectOut = null;
        ObjectInputStream objectIn = null;


        try {

            objectOut = new ObjectOutputStream(outToServer);

            objectOut.writeObject(information);
            objectOut.flush();



            // send stuff to server

           while (messageQue.size() > 0)
            {
                ClientServerMessage message = messageQue.get(0);


                Log.i("Server",Integer.toString(messageQue.size()));
                objectOut.writeObject(message);
                objectOut.flush();

                messageQue.remove(0);





            }


            long time = System.currentTimeMillis();
            long newTime = time + 10000;
            while (inFromServer.available() == 0 && time < newTime )
            {
                time = System.currentTimeMillis();
            }

            // see if server has anything for me
            if (inFromServer.available() > 0)
            {
                Log.i("Server", "Bytes available : " +  Integer.toString(clientSocket.getInputStream().available()));

                if (objectIn == null)
                {
                    objectIn = new ObjectInputStream(inFromServer);
                    try {
                        Object obj =  objectIn.readObject();
                        this.handleServerIncomingObject(obj);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }



            } else if (requestQue.isEmpty() == false)
            {


            } else {


            }

            Log.i("Server", "Server cycle ended");
        } catch (IOException e)
        {
            setConnected(false);

        }


        }


    public void requestToSendMessage(ClientServerMessage message)
    {
        messageQue.add(message);
        //requestQue.add(ServerContract.SEND_MESSAGE_TO_USER);
    }

    public void sendMessage()
    {

        if (messageQue.isEmpty() == false)
        {
            ClientServerMessage message1 = messageQue.get(0);


                // send message
                messageQue.remove(0);

        }
    }



    public void queSocketClose()
    {
        requestQue.add(ServerContract.DISCONNECT_CODE);
    }


    private void disconnectFromServer()
    {

        Log.e("Server", "Server discconect initiated");
        String  exitMessage = ServerContract.DISCONNECT_CODE;
        String response = "";




            try {
                Log.e("Server", "Started to write exit code to server");

                outToServer.writeBytes(ServerContract.DISCONNECT_CODE + "\n");
                outToServer.flush();
                Log.e("Server", "Wrote exit code to server");

                while (inFromServer.available() == 0)
                {

                }
                Log.e("Server", "Getting Response from server");

                if (inFromServer.available() > 0)
                {
                    String x = inFromServer.readLine();
                    if (x.equals(ServerContract.CONFIRM_RESPONSE))
                    {
                        setConnected(false);
                        try {
                            clientSocket.close();
                            Log.i("Server", "Server disconnected");
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.i("Server", e.getMessage());
                        }

                    }
                    else
                    {
                        Log.i("Server","Failed to notify and close socket");
                    }
                }
            }
            catch(IOException e)
            {
                Log.i("Server",e.getMessage());

            }





    }


    public void handleServerIncomingObject(Object obj)
    {
        if (obj instanceof ClientServerMessage)
        {
            ClientServerMessage message = (ClientServerMessage) obj;
            if (message.getMessageType() != null) {
                MessageType messageType = message.getMessageType();

                if (messageType ==MessageType.DataLogToUser)
                {
                    byte[] payload = message.getDataPayload();
                   Object objNew =  SerializationOperations.deserializeToObject(payload);

                   if (objNew instanceof Message)
                   {
                       Message m = (Message) objNew;
                       MessageHandler.handleIncomingDataMessage(m);
                   }

                }
            }

        }
    }


    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
