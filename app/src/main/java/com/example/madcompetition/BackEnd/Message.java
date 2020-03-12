package com.example.madcompetition.BackEnd;

import android.util.Log;

import com.example.madcompetition.MediaType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Message implements Serializable
{
    private Account sender;
   // private Account reciever;
    private Account[] recipeints;


    private LocalDate dateRecieved;
    private LocalDate dateSent;

    private LocalTime timeSent;
    private LocalTime timeRecieved;

    private LocalDate dateRead;
    private LocalTime timeRead;

    private MediaType messageMediaType;

    private String message;
    private int conversationId;




    public Message()
    {
        setTimeRecieved(LocalTime.MIDNIGHT);
        setTimeSent(LocalTime.MIDNIGHT);
        setDateRecieved(LocalDate.of(2000,1,1));
        setDateSent(LocalDate.of(2000,1,1));
        setDateRead(LocalDate.of(2000,1,1));
        setTimeRead(LocalTime.MIDNIGHT);

    }
    public Message(Account sender,Account[] recipeints, byte[] dataPayload, MessageType messageType) // delete this
    {
        this();
        this.setSender(sender);
       // this.setReciever(reciever);
        this.recipeints = recipeints;





    }

    public Message(Account sender, Account[] recipeints, MediaType mediaType, String message)
    {
        this();
        this.sender = sender;
       // this.reciever = reciever;
        this.messageMediaType = mediaType;
        this.message = message;
        this.recipeints = recipeints;
    }

    public Message(Message message)
    {
        this();
        setTimeRecieved(LocalTime.now());
        setDateRecieved(LocalDate.now());

        this.setSender(message.getSender());
        this.recipeints = message.getRecipeints();
        //this.setReciever(message.getReciever());
        this.setTimeSent(message.getTimeSent());








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



    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
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





    public LocalDate getDateRecieved() {
        return dateRecieved;
    }

    public void setDateRecieved(LocalDate dateRecieved) {
        this.dateRecieved = dateRecieved;
    }

    public LocalDate getDateSent() {
        return dateSent;
    }

    public void setDateSent(LocalDate dateSent) {
        this.dateSent = dateSent;
    }



    public LocalTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalTime timeSent) {
        this.timeSent = timeSent;
    }

    public LocalTime getTimeRecieved() {
        return timeRecieved;
    }

    public void setTimeRecieved(LocalTime timeRecieved) {
        this.timeRecieved = timeRecieved;
    }


    public LocalDate getDateRead() {
        return dateRead;
    }

    public void setDateRead(LocalDate dateRead) {
        this.dateRead = dateRead;
    }

    public LocalTime getTimeRead() {
        return timeRead;
    }

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

    public Account[] getRecipeints() {
        return recipeints;
    }

    public void setRecipeints(Account[] recipeints) {
        this.recipeints = recipeints;
    }

    public static class SelfDestructMessage implements Runnable
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
}
