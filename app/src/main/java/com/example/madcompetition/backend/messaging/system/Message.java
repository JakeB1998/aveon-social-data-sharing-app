package com.example.madcompetition.backend.messaging.system;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.madcompetition.backend.AppManager;
import com.example.madcompetition.backend.account.AccountInformation;
import com.example.madcompetition.backend.utils.Data;
import com.example.madcompetition.backend.Interfaces.DataTransferCallback;
import com.example.madcompetition.backend.server.ftp.FileData;
import com.example.madcompetition.backend.utils.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Message implements Serializable, DataTransferCallback
{
    private AccountInformation sender;
   // private Account reciever;
    private AccountInformation[] recipeints;

    private FileData messageFileData;


    private LocalDate dateRecieved;
    private LocalDate dateSent;

    private LocalTime timeSent;
    private LocalTime timeRecieved;

    private LocalDate dateRead;
    private LocalTime timeRead;

    private MediaType messageMediaType;

    private String message;

    private int conversationId;
    private int messageID;

    private boolean delivered;
    private boolean read;
    private boolean messageSelfDestructed;
    private boolean important;

    private boolean messgaePrivate;
    private boolean messageSelfDestruct;
    private boolean messageRead;
    private boolean messageReieved;

    private MessageContraints messageContraints;





    /**
     *
     */
    public Message(boolean NULL)
    {

        setTimeRecieved(LocalTime.MIDNIGHT);
        setTimeSent(LocalTime.MIDNIGHT);
        setDateRecieved(LocalDate.of(2000,1,1));
        setDateSent(LocalDate.of(2000,1,1));
        setDateRead(LocalDate.of(2000,1,1));
        setTimeRead(LocalTime.MIDNIGHT);

    }


    public Message(AccountInformation sender, AccountInformation[] recipeints, MediaType mediaType, String message)
    {
        this(true);
        this.sender = sender;
       // this.reciever = reciever;
        this.messageMediaType = mediaType;
        this.message = message;
        this.recipeints = recipeints;
        setMessageID(Integer.parseInt(StringUtils.generateID(8)));
    }

    public Message(Message message)
    {
        this(true);
        setTimeRecieved(LocalTime.now());
        setDateRecieved(LocalDate.now());

        this.setSender(message.getSender());
        this.recipeints = message.getRecipeints();
        //this.setReciever(message.getReciever());
    //    this.setTimeSent(message.getTimeSent());



    }


    public String getConversationDateDisplay()
    {
        String returnS = AppManager.NULLABLE_STRING;

        int x = LocalDate.now().getDayOfYear();
        Log.i("Date", Integer.toString(dateSent.getDayOfYear()));
        if (x == dateSent.getDayOfYear())
        {
            returnS = timeSent.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
            Log.i("Date", "Time shown");
        }
        else if (x > dateSent.getDayOfYear())
        {
            returnS = dateSent.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
            Log.i("Date", "Date Shown");
        }

        return returnS;
    }



    public AccountInformation getSender() {
        return sender;
    }

    public void setSender(AccountInformation sender) {
        this.sender = sender;
    }
/*
    public Account getReciever() {
        return reciever;
    }

    public void setReciever(Account reciever) {
        this.reciever = reciever;
    }

 */


    /**
     *
     * @return
     */
    public LocalDate getDateRecieved() {
        return dateRecieved;
    }

    /**
     *
     * @param dateRecieved
     */
    public void setDateRecieved(LocalDate dateRecieved) {
        this.dateRecieved = dateRecieved;
    }

    /**
     *
     * @return
     */
    public LocalDate getDateSent() {
        return dateSent;
    }

    public void setDateSent(LocalDate dateSent) {
        this.dateSent = dateSent;
    }


    /**
     *
     * @return
     */
    public LocalTime getTimeSent() {
        return timeSent;
    }

    /**
     *
     * @param timeSent
     */
    public void setTimeSent(LocalTime timeSent) {
        this.timeSent = timeSent;
    }

    /**
     *
     * @return
     */
    public LocalTime getTimeRecieved() {
        return timeRecieved;
    }

    /**
     *
     * @param timeRecieved
     */
    public void setTimeRecieved(LocalTime timeRecieved) {
        this.timeRecieved = timeRecieved;
    }

    /**
     *
     * @return
     */
    public LocalDate getDateRead() {
        return dateRead;
    }

    /**
     *
     * @param dateRead
     */
    public void setDateRead(LocalDate dateRead) {
        this.dateRead = dateRead;
    }

    /**
     *
     * @return
     */
    public LocalTime getTimeRead() {
        return timeRead;
    }

    /**
     *
     * @param timeRead
     */
    public void setTimeRead(LocalTime timeRead) {
        this.timeRead = timeRead;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public AccountInformation[] getRecipeints() {
        return recipeints;
    }

    public void setRecipeints(AccountInformation[] recipeints) {
        this.recipeints = recipeints;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isMessageSelfDestructed() {
        return messageSelfDestructed;
    }

    public void setMessageSelfDestructed(boolean messageSelfDestructed) {
        this.messageSelfDestructed = messageSelfDestructed;
    }
/*
    private static class SelfDestructMessage implements Runnable
    {
        private int selfDestructTimeDelay;

        private boolean selfDestructImmediate;
        private boolean selfDestructCompleted;

        public SelfDestructMessage()
        {
            selfDestructCompleted = false;

        }
        public SelfDestructMessage(int timeDelay)
        {
           this();
            this.selfDestructTimeDelay = timeDelay;

        }
        public SelfDestructMessage(boolean selfDestructImmediate)
        {
            this();
           this.selfDestructImmediate = selfDestructImmediate;
        }
        public SelfDestructMessage(boolean selfDestructImmediate, int timeDelay, Message message)
        {
            this();
            this.selfDestructImmediate = selfDestructImmediate;
        }


        @Override
        public void run()
        {
            try
            {
                Thread.sleep(selfDestructTimeDelay * 1000);

                selfDestructCompleted = true;
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }
    }

 */

    public void setMessageObject(Message message)
    {
        this.messageSelfDestructed=  message.isMessageSelfDestructed();
        this.important = message.isImportant();

        if (messageSelfDestructed) {
            this.setMessage("THIS MESSAGE HAS SELF DESTRUCTED");
        }

        if(important)
        {
            this.setMessage("Message marked important"); // delete
        }

    }

    public boolean equals(Object obj)
    {
        if (obj instanceof Message)
        {
            Message m = (Message)obj;
            Log.i("Equals", "Sender1 : " + m.getSender().getAccountId() +  "SenderThis : " + this.getSender().getAccountId());
            return this.messageID == m.messageID;

        }
        return false;
    }

    public boolean isMessgaePrivate() {
        return messgaePrivate;
    }

    public void setMessgaePrivate(boolean messgaePrivate) {
        this.messgaePrivate = messgaePrivate;
    }

    public boolean isMessageSelfDestruct() {
        return messageSelfDestruct;
    }

    public void setMessageSelfDestruct(boolean messageSelfDestruct) {
        this.messageSelfDestruct = messageSelfDestruct;
    }

    public boolean isMessageRead() {
        return messageRead;
    }

    public void setMessageRead(boolean messageRead) {
        this.messageRead = messageRead;
    }

    public boolean isMessageReieved() {
        return messageReieved;
    }

    public void setMessageReieved(boolean messageReieved) {
        this.messageReieved = messageReieved;
    }




    @NonNull
    @Override
    public String toString() {
        String master = "";

        String str1;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        return "ass";
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    @Override
    public void TransferData(Data data) {

        PictureMessage.registerCallback(this);
        Log.i("ass", "ass");

    }

    public FileData getMessageFileData() {
        return messageFileData;
    }

    public void setMessageFileData(FileData messageFileData) {
        this.messageFileData = messageFileData;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }
}
