package com.example.madcompetition.BackEnd;

import com.example.madcompetition.MediaType;

import java.time.LocalDate;
import java.time.LocalTime;

public class TextMessage extends Message
{

    private String message;







    private boolean messgaePrivate;
    private boolean messageSelfDestruct;
    private boolean messageRead;
    private boolean messageReieved;
    private boolean selfDestruct;
    private int selfDestructTimeDelay;

    public TextMessage(Account sender, Account reciever, String messgae, LocalDate transmissionDate, LocalTime transmissionTime) // fiscard
    {
        super();
        this.setMessage(getMessage());

    }
    public TextMessage( String messgae) // discard
    {
        super();
        this.setMessage(messgae);

    }
    public TextMessage( String messgae, Account sender, Account [] recepients) // discard maybe
    {
        super(sender,recepients, MediaType.Text, messgae);
        this.setMessage(messgae);



    }
    public TextMessage(String message, Conversation convo)
    {
        super();
    }


    public TextMessage(TextMessage textMessage)
    {

        super((Message)textMessage);

    }




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public boolean isSelfDestruct() {
        return selfDestruct;
    }

    public void setSelfDestruct(boolean selfDestruct) {
        this.selfDestruct = selfDestruct;
    }

    public int getSelfDestructTimeDelay() {
        return selfDestructTimeDelay;
    }

    public void setSelfDestructTimeDelay(int selfDestructTimeDelay) {
        this.selfDestructTimeDelay = selfDestructTimeDelay;
    }




}
