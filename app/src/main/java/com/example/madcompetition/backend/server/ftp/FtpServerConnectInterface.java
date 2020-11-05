package com.example.madcompetition.backend.server.ftp;

import android.os.AsyncTask;
import android.util.Log;

import com.example.madcompetition.backend.AppManager;
import com.example.madcompetition.backend.Interfaces.ProgressDataTransferCallback;
import com.example.madcompetition.backend.messaging.system.Message;
import com.example.madcompetition.backend.server.ClientServerMessage;
import com.example.madcompetition.backend.server.MessageType;
import com.example.madcompetition.backend.server.ServerContract;
import com.example.madcompetition.backend.utils.SerializationOperations;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class FtpServerConnectInterface {
    public enum TaskType
    {
        Upload,Download
    }
    private static final FtpServerConnectInterface ourInstance = new FtpServerConnectInterface();

    public static FtpServerConnectInterface getInstance() {
        return ourInstance;
    }


    private ArrayList<ClientServerMessage> messageQue;
    private ArrayList<ClientServerMessage> messageQueBuffer;
    private boolean connected;


    private TaskType taskType;


    private ProgressDataTransferCallback progressCallback;

    private FtpServerConnectInterface() {

        messageQue = AppManager.getInstance().getCurrentAccountLoggedIn().getFtpMessageQue();

        if (messageQue == null)
        {
            messageQue = new ArrayList<>(0);
        }
        messageQueBuffer = new ArrayList<>(0);
    }

    public void startInteraction(ClientServerMessage message)
    {

        Log.i(this.getClass().getName(), "FTP interaction about to start");
        Log.i(this.getClass().getName(), "FTP message que form saved account : " + messageQue.size());
        enqueMessages(message);
        ConnectTask connectTask = new ConnectTask(null);
        Log.i(this.getClass().getName(), "Task about to start");
        connectTask.execute(messageQueBuffer);

    }

    public void startDownloadInteraction(Message message)
    {
        ClientServerMessage clientServerMessage = new ClientServerMessage(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation(),
                null, MessageType.FileRequest, SerializationOperations.serializeObjectToBtyeArray(message.getMessageFileData()));
        //AddMessage(clientServerMessage);
        Log.i(this.getClass().getName(), "FTP interaction about to start");
        Log.i(this.getClass().getName(), "FTP message que form saved account : " + messageQue.size());
        if (connected == false) {
            enqueMessages(clientServerMessage);
            ConnectTask connectTask = new ConnectTask(message);
            Log.i(this.getClass().getName(), "Task about to start");
            connectTask.execute(messageQueBuffer);
        }
    }



    public void AddMessage(ClientServerMessage message)
    {
        messageQue.add(message);

    }

    private void enqueMessages(ClientServerMessage message)
    {

            messageQueBuffer.add(message); // automatically shifts downards in index  thus always grabbing form the bottom



    }

    private void DequeMessages()
    {
        for (ClientServerMessage m : messageQueBuffer)
        {
            messageQue.add(messageQueBuffer.get(0)); // automatically shifts downards in index  thus always grabbing form the bottom
            messageQueBuffer.remove(0);
        }

    }


    public Boolean getConnectionStatus()
    {
        return connected;
    }

    public void registerOnProgressUpdates(ProgressDataTransferCallback callback)
    {

        this.progressCallback = callback;
    }




    private class ConnectTask extends AsyncTask<ArrayList<ClientServerMessage>, Integer, Boolean>
    {
        private Socket clientSocket;
        private Message message;

        public ConnectTask(Message message)
        {
            this.message = message;
        }


        @Override
        protected Boolean doInBackground(ArrayList<ClientServerMessage>... arrayLists) {
           ArrayList<ClientServerMessage> temp = arrayLists[0];

            Log.i(this.getClass().getName(), "FTP Message que size :" + temp.size());

            Log.i(this.getClass().getName(), "FTP attemoting to connect");
           if (connect())
           {
               connected = true;
               Log.i(this.getClass().getName(), "FTP connectt");
               ObjectOutputStream out = null;
               FileData data = null;
               /////
               ////send file info
               if (temp.size() > 0)
               {
                   ClientServerMessage message = temp.get(0);
                   if (message.getMessageType() == MessageType.FileMessage)
                   {
                       try {
                           this.uploadProtocol(temp);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
                   else if (message.getMessageType() == MessageType.FileRequest)
                   {
                           downloadProtocol(message);
                           Log.i(this.getClass().getName(), "Donwload protocol started");


                   }


               }
               else
               {
                   Log.i(this.getClass().getName(), "File content null");

               }

           }
           else
           {
               Log.i(this.getClass().getName(), "failed to connect");

           }

           connected = false;
           messageQueBuffer.clear();
            return true;
        }


        private Boolean connect()
        {
            boolean bool;

            try
            {

                clientSocket = new Socket(ServerContract.FTP_SERVER_IP_ADRESS, ServerContract.FTP_SERVER_PORT_NUMBER);
                clientSocket.setTcpNoDelay( true );
               connected = true;
                bool = true;
                //inFromServer = new DataInputStream( clientSocket.getInputStream());
                //outToServer = new DataOutputStream(  clientSocket.getOutputStream());
                AppManager.getInstance().setConnectedToServer(true);

            } catch (UnknownHostException e1)
            {

                AppManager.getInstance().setConnectedToServer(false);
                connected = false;

                // TODO Auto-generated catch block
                e1.printStackTrace();
                Log.e("Server", e1.getCause().toString());
                return  false;
            } catch (IOException e1)
            {

                AppManager.getInstance().setConnectedToServer(false);
                connected = false;
                bool = false;
                // TODO Auto-generated catch block
                e1.printStackTrace();
                Log.e("Server", e1.getCause().toString());
                return false;
            }

            connected = false;
            return  bool;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.i(this.getClass().getName(), "Progress updated with value: " + values[0]);
            if (progressCallback != null)
            {
                if (taskType == TaskType.Upload) {
                    progressCallback.uploadProgressUpdate(values[0]);
                }
                else if (taskType == TaskType.Download)
                {
                    progressCallback.downloadProgressUpdate(values[0]);
                }
            }
        }

        @Override
        protected void onCancelled() {

            super.onCancelled();
        }


        private void uploadProtocol(ArrayList<ClientServerMessage> temp ) throws IOException {
            FileData data = null;
            ObjectOutputStream out = null;

            try {
                while (temp.size() > 0) {
                    data  = (FileData) SerializationOperations.deserializeToObject(temp.get(0).getDataPayload());
                    Log.i(this.getClass().getName(), data.toString());
                    out = new ObjectOutputStream(clientSocket.getOutputStream());
                    out.writeObject(temp.get(0));
                    out.flush();
                    Log.i(this.getClass().getName(), "Object wrote to FTP server");
                    temp.remove(0);
                }


            }
            catch (IOException e) {
                e.printStackTrace();
            }

            // send file data

            if (data != null) {
                DataOutputStream dos = null;
                try {
                    dos = new DataOutputStream(clientSocket.getOutputStream());

                    FileInputStream fis = new FileInputStream(data.getFilePath());


                    Log.i(this.getClass().getName(), "Started To write file to server");
                    Log.i(this.getClass().getName(), "file length : " + data.getFileLength());
                    long remaining = data.getFileLength();
                    int count;
                    byte[] buffer = new byte[8096]; // or 4096, or more

                    while (fis.read(buffer) > 0) {
                        dos.write(buffer);
                        dos.flush();
                        remaining -= buffer.length;
                        int prog = 100 - (int)(data.getFileLength() / remaining);
                        if (prog < 0) {
                            prog = 0;
                        } else if (prog > 100) {
                            prog = 100;
                        }
                        publishProgress(prog);
                        Log.i(this.getClass().getName(), "Buffer size : " + buffer.length);
                    }


                    publishProgress(100);
                    Log.i(this.getClass().getName(), "File content wrote to server");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

        public void downloadProtocol(ClientServerMessage message)
        {

            FileData data = (FileData)SerializationOperations.deserializeToObject(message.getDataPayload());
            File file = data.getFile();
           ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            DataOutputStream dos = null;
            DataInputStream dis = null;
            try {
                dis = new DataInputStream(clientSocket.getInputStream());
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[8096];

                long filesize = data.getFileLength();
                int read = 0;
                int totalRead = 0;
                long remaining = filesize;
                while((read = dis.read(buffer, 0, buffer.length)) > 0) {
                    totalRead += read;
                    remaining -= read;
                    System.out.println("read " + totalRead + " bytes.");
                    fos.write(buffer, 0, read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }




        }


    }

}
