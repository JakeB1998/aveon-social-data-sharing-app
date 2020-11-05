package com.example.madcompetition.backend.server;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.example.madcompetition.backend.account.Account;
import com.example.madcompetition.backend.account.AccountInformation;
import com.example.madcompetition.backend.AppManager;
import com.example.madcompetition.backend.account.friend.FriendRequest;
import com.example.madcompetition.backend.account.friend.FriendRequestHandler;
import com.example.madcompetition.backend.account.friend.FriendRequestResult;
import com.example.madcompetition.backend.messaging.system.Message;
import com.example.madcompetition.backend.messaging.system.MessageHandler;
import com.example.madcompetition.backend.utils.SerializationOperations;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;


public class ServerConnectInterface
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

    private Socket clientSocket;
    private boolean sendAlive;

    private ServerConnectTask runningTask;

    private int mInterval = 1000; // 5 seconds by default, can be changed later
    Handler handler = new Handler();
    private Handler mHandler;
    private Runnable mStatusChecker;

    private boolean taskRunning;

    /**
     * private singleton constructor that only constructs once per lifecycle
     */
    private ServerConnectInterface()
    {

        setConnected(false);
        mHandler = new Handler();


    }

    /**
     * runs
     */
    public void run()
    {

        if (mHandler == null)
        {
            mHandler = new Handler();
        }
        mStatusChecker = new Runnable() {
            @Override
            public void run() {

                runTask();
            }
        };
        mHandler.postDelayed(mStatusChecker, mInterval);
        Log.i(this.getClass().getName(), "Repeating task started");

    }

    /**
     *
     */
    public void runTask()
    {

        if (runningTask == null) {
            runningTask = new ServerConnectTask();
            runningTask.execute(getMessageQue());
        }
        else
        {
            Log.e(this.getClass().getName(), "Attepted to connect to server while it is already connected");
        }
    }
    /**
     * Adds message to be sent into the server que
     * @param message
     */
    public void addClientServerMessageToQue(ClientServerMessage message)
    {
        if (message != null) {
            if (getMessageQue() != null) {
                getMessageQue().add(message);
                Log.i("Server", "Client server message succesffuly added to que");
                Log.i("Server", "Messgaes in local que : " + message.getMessageType().toString());
                Log.i("Server", "Messgaes in AccountQue : " + AppManager.getInstance().getCurrentAccountLoggedIn().getServerMessagesQue().size());
                AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(AppManager.getInstance().getAppContext());
            } else {
                messageQue = AppManager.getInstance().getCurrentAccountLoggedIn().getServerMessagesQue();
                this.addClientServerMessageToQue(message);
            }
        }
        else
        {
            //message is null
        }

        // add to a databaase
        //read in messages from database
    }

    public void removeClientServerMessageToQue(ClientServerMessage message)
    {
        if (getMessageQue() != null)
        {
            getMessageQue().remove(message);
            Log.i("Server", "Client server message succesffuly added to que");
            Log.i("Server", "Messgaes in local que : " + messageQue.size());
            Log.i("Server", "Messgaes in AccountQue : " + AppManager.getInstance().getCurrentAccountLoggedIn().getServerMessagesQue().size());
            AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(AppManager.getInstance().getAppContext());
        }
        else
        {
            messageQue = AppManager.getInstance().getCurrentAccountLoggedIn().getServerMessagesQue();
            this.removeClientServerMessageToQue(message);
        }

        // add to a databaase
        //read in messages from database
    }

    /**
     * cancells the server connection task
     */
    public void cancelBackroundTask()
    {

        if (runningTask != null)
        {
            Log.i(this.getClass().getName(), "Attempting to cancel task");
            runningTask.cancel(true);
            runningTask.onCancelled();
        }
        else
        {
            Log.i(this.getClass().getName(), "Attemted to cancel server connect task that is not currently running");
        }

    }

    /**
     * starts server polling
     */
    public void startRepeatingTask()
    {

        setMessageQue(AppManager.getInstance().getCurrentAccountLoggedIn().getServerMessagesQue());
        currentAccount = AppManager.getInstance().getCurrentAccountLoggedIn();
        if (taskRunning == false)
        {
            taskRunning = true;
            if (mHandler == null || mStatusChecker == null) {
                this.run();
            } else {
                mHandler.postDelayed(mStatusChecker, mInterval);
                Log.i(this.getClass().getName(), "Repeating task started");
            }
        }


    }


    /**
     * stops server polling
     */
    public void stopRepeatingTask()
    {
        taskRunning = false;
        mHandler.removeCallbacks(mStatusChecker);
        Log.i(this.getClass().getName(), "Repeating task stopped");
    }


    /**
     *
     * @return
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     *
     * @param connected
     */
    public void setConnected(boolean connected) {
        if (connected == false)
        {
            if (clientSocket != null)
            {
                if (clientSocket.isClosed()== false)
                {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            taskRunning = false;
            startRepeatingTask();

        }
        else
        {
            taskRunning = true;
        }
        AppManager.getInstance().setConnectedToServer(connected);
        isConnected = connected;
    }

    /**
     *
     * @return
     */
    public synchronized ArrayList<ClientServerMessage> getMessageQue() {
        return messageQue;
    }

    /**
     *
     * @param messageQue
     */
    public synchronized void setMessageQue(ArrayList<ClientServerMessage> messageQue) {
        this.messageQue = messageQue;
    }

    public void startTimeout()
    {

        int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                cancelBackroundTask();
            }
        }, 5000);
        setConnected(false);
    }

    public void cancelTimeout()
    {
        if (handler != null)
        {
            //handler.removeCallbacks(handler.g);
        }
    }




    /**
     *
     */
    private class ServerConnectTask extends AsyncTask<ArrayList<ClientServerMessage>,Void,Void>
    {

        private ArrayList<ClientServerMessage> thisMessageQue;
        Timer timer; // used as a timeout for the server connection

        @Override
        protected Void doInBackground(ArrayList<ClientServerMessage>... params) {

            stopRepeatingTask(); // stops the server polling
            startTimeout();



            //Log.i("ASS", "Friend Array from Server in account : " + AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation().getFriendList().length);
            thisMessageQue = getMessageQue(); // this is sycnhronized

            if (params != null) {
                thisMessageQue = params[0];
                try
                {
                    Inet4Address net = (Inet4Address) Inet4Address.getLocalHost();
                    AppManager.getInstance().setDeviceAddress(net);

                }
                catch (UnknownHostException e)
                {
                    e.printStackTrace();
                }

                boolean bool = false;
                if (isConnected == false) {
                     bool = connect(); // connects to the server;
                }

                if (bool)
                {
                    setConnected(true);


                    while (true)
                    {

                        if (clientSocket != null) {
                            runServerInteraction();
                            Log.i(this.getClass().getName(), "Interaction and delay done");
                            if (clientSocket.isClosed() == false) {
                                try {
                                    clientSocket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        break;
                    }
                }
                else
                {

                }
            }
            return null;

        }

        /**
         * The set up before backround execution
         */
        @Override
        protected void onPreExecute() {


            super.onPreExecute();

        }


        /**
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        /**
         *
         * @param aBoolean
         */
        @Override
        protected void onPostExecute(Void aBoolean) {
            runningTask = null;
            if (timer != null)
            {
                timer.cancel();
            }
            Log.i(this.getClass().getName(), "Connect task completed");
            if (clientSocket.isClosed() == false) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            setConnected(false);
            super.onPostExecute(aBoolean);
        }

        /**
         * called when async task is cancelled
         */
        @Override
        protected void onCancelled() {

            Log.i(this.getClass().getName(), "Task was canceled");

            runningTask = null;
            if (timer != null)
            {
                timer.cancel();
            }
            stopRepeatingTask();
            Log.i(this.getClass().getName(), "Connect task completed");
            if (clientSocket != null) {
                if (clientSocket.isClosed() == false) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            setConnected(false);

            super.onCancelled();
        }



        /**
         * Attemts to connect to the nearest application server in this project
         * @return
         */
        public boolean connect()
        {
            boolean bool;

            try
            {

                clientSocket = new Socket(ServerContract.SERVER_IP_ADRESS, ServerContract.SERVER_PORT_NUMBER);
                clientSocket.setTcpNoDelay( true );
                setConnected(true);
                bool = true;
                //inFromServer = new DataInputStream( clientSocket.getInputStream());
                //outToServer = new DataOutputStream(  clientSocket.getOutputStream());
                AppManager.getInstance().setConnectedToServer(true);

            } catch (UnknownHostException e1)
            {

                AppManager.getInstance().setConnectedToServer(false);
                onCancelled();
                setConnected(false);

                // TODO Auto-generated catch block
                e1.printStackTrace();
              //  Log.e("Server", e1.getCause().toString());
                return  false;
            } catch (IOException e1)
            {

                setConnected(false);
                AppManager.getInstance().setConnectedToServer(false);
                bool = false;
                // TODO Auto-generated catch block
                e1.printStackTrace();
                //Log.e("Server", e1.getCause().toString());
                return false;
            }

            return  bool;

        }

        /**
         * This is the entire interaction protocol with the APPLICATION server.
         */
        public void runServerInteraction()
        {

            Account current = AppManager.getInstance().getCurrentAccountLoggedIn();

            AccountInformation information = current.getAccountInformation();

            Log.i(this.getClass().getName(),current.getAccountCredentials().getHashedPassword());

            //Log.i(this.getClass().getName(), information.getAdressInfo().toString());
            ObjectOutputStream objectOut = null;
            ObjectInputStream objectIn = null;
            Log.i(this.getClass().getName(), "First exchange protocol started");
            try {
                objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOut.writeObject(information);
                objectOut.flush();

                // send stuff to server

                if (thisMessageQue.size() > 0) {

                    ClientServerMessage number = new ClientServerMessage(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation(), null ,
                            MessageType.ObjectAmountToRead, Integer.toString(thisMessageQue.size()).trim().getBytes());

                    objectOut.writeObject(number);
                    objectOut.flush();
                    int index = 0;
                    while (thisMessageQue.size() > 0) {
                        ClientServerMessage message = thisMessageQue.get(0);
                        if (message != null) {



                            Log.i(this.getClass().getName(), Integer.toString(thisMessageQue.size()));

                            objectOut.writeObject(message);
                            objectOut.flush();
                            Log.i(this.getClass().getName(), "Wrote message to server : " + message.getMessageType());
                        }

                        thisMessageQue.remove(0);
                    }

                    AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(AppManager.getInstance().getAppContext());
                }
                else
                {
                    ClientServerMessage number = new ClientServerMessage(currentAccount.getAccountInformation(),null ,
                            MessageType.ContinueProtocol, null);
                    objectOut.writeObject(number);
                    objectOut.flush();

                }
                Log.i(this.getClass().getName(), "First exchange protocol finished");


                long time = System.currentTimeMillis();
                long newTime = time + 1000;

                while (clientSocket.getInputStream().available() < 4 || System.currentTimeMillis() < newTime)
                {
                    if (clientSocket.getInputStream().available() < 0)
                    {
                        break;
                    }

                }

                long timenew = System.currentTimeMillis() + 500;
                while ( clientSocket.getInputStream().available() < 4 ||System.currentTimeMillis() < timenew)
                {

                }
                Log.i(this.getClass().getName(), "second exchange protocol started");
                // see if server has anything for me
                if (clientSocket.getInputStream().available() > 4)
                {
                    Log.i("Server", "Bytes available : " + clientSocket.getInputStream().available());


                        int index;
                        objectIn = new ObjectInputStream(clientSocket.getInputStream());
                        try {
                            Object obj =  objectIn.readObject();
                            if (obj instanceof ClientServerMessage)
                            {
                                Log.i(this.getClass().getName(), "Object About to be read from server");
                                ClientServerMessage CM = (ClientServerMessage) obj;
                                if (CM.getMessageType() == MessageType.ObjectAmountToRead)
                                {
                                    String num = new String(CM.getDataPayload());
                                    num = num.trim();
                                    index = Integer.parseInt(num);
                                    Log.i(this.getClass().getName(), "Number of ojects to read : " + index);
                                    for (int i = 0; i< index; i++)
                                    {
                                        Object newObj =  objectIn.readObject();
                                        handleServerIncomingObject(newObj);
                                    }
                                }
                                else if (CM.getMessageType() == MessageType.ContinueProtocol)
                                {
                                    handleServerIncomingObject(CM);

                                }
                            }

                        } catch (ClassNotFoundException e) {
                            Log.i(this.getClass().getName(), e.getMessage(),e);
                        }




                }  else {


                }

                Log.i(this.getClass().getName(), "Server cycle ended");
            } catch (IOException e)
            {

                Log.i(this.getClass().getName(), e.getMessage());
                setConnected(false);

            }


        }


        public void handleServerIncomingObject(Object obj)
        {
            if (obj instanceof ClientServerMessage)
            {
                ClientServerMessage message = (ClientServerMessage) obj;
                if (message.getMessageType() != null) {
                    MessageType messageType = message.getMessageType();
                    Log.i(this.getClass().getName() + "-Mesage", "message type : " + message.getMessageType().toString());
                    byte[] payload = message.getDataPayload();
                    if (payload != null) {
                        Log.i(this.getClass().getName() + "-Message", "Length of payload :" + payload.length);
                    }
                    Object objNew = null;
                    if (payload != null) {
                       objNew = SerializationOperations.deserializeToObject(payload);
                    }

                    switch (messageType)
                    {
                        case MessageToUser:


                            if (objNew instanceof Message)
                            {
                                Message m = (Message) objNew;
                                MessageHandler.handleIncomingDataMessage(m);
                                Log.i(this.getClass().getName() + "-Message", "User recieved message : " + m.getMessage());
                            }
                            else if (objNew instanceof FriendRequest)
                            {
                                Log.i(this.getClass().getName(), "User recieved friend request");
                                FriendRequestHandler.handleFriendRequest((FriendRequest)objNew);
                            }
                            else if (objNew instanceof FriendRequestResult)
                            {
                                Log.i(this.getClass().getName(), "User recieved results from friend request");
                                FriendRequestResult friendResult = (FriendRequestResult)objNew;
                            }
                            else
                            {
                                Log.i(this.getClass().getName(), "invalid message at line 308");
                            }
                            break;
                        case DataLogToUser:
                            Log.i(this.getClass().getName(), "User recieved a data log messgae");
                            if (objNew instanceof ClientServerObjectResults)
                            {
                                ClientServerObjectResults results = (ClientServerObjectResults) objNew;
                               Object objResults =  SerializationOperations.deserializeToObject(results.getData());


                               if (objResults instanceof AccountInformation)
                               {
                                   AccountInformation recievedInfo = (AccountInformation)objResults;
                                   AccountInformationTemporaryDataContainer dataContainer = AccountInformationTemporaryDataContainer.getInstance();
                                   dataContainer.addData(recievedInfo);
                                  // AppManager.getInstance().getCurrentAccountLoggedIn().getFRIEND_LIST().addFriend(new Friend(recievedInfo));
                                   Log.i(this.getClass().getName(), "Recieved another users account information, their username is : " + recievedInfo.getUserName());
                               }
                            }

                            break;
                        case ContinueProtocol:
                            Log.i(this.getClass().getName(), "Server told us to continue protocol");
                            break;
                        case FileMessage:
                            //Log.i(this.getClass().getName(), objNew.toString());
                        if (objNew instanceof Message) {
                            Message m = (Message) objNew;
                            MessageHandler.handleIncomingDataMessage(m);
                             Log.i(this.getClass().getName() + "-Message", "User recieved message : " + m.getMessageFileData().toString());
                        }
                            break;
                    }

                }

            }
        }


    }

}
