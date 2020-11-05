package com.example.madcompetition.backend.messaging.system;

import com.example.madcompetition.backend.account.AccountInformation;

import java.time.LocalDate;
import java.time.LocalTime;

public class TextMessage extends Message
{

    private String message;


    private boolean selfDestruct;
    private int selfDestructTimeDelay;


    public TextMessage( String messgae) // discard
    {
        super(true);
        this.setMessage(messgae);

    }
    public TextMessage(String messgae, AccountInformation sender, AccountInformation [] recepients) // discard maybe
    {
        super(sender,recepients, MediaType.Text, messgae);
        this.setMessage(messgae);
        setTimeSent(LocalTime.now());
        setDateSent(LocalDate
        .now());



    }
    public TextMessage(String message, Conversation convo)
    {
        super(true);
    }


    public TextMessage(TextMessage textMessage)
    {

        super(textMessage);

    }




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }




    public int getSelfDestructTimeDelay() {
        return selfDestructTimeDelay;
    }

    public void setSelfDestructTimeDelay(int selfDestructTimeDelay) {
        this.selfDestructTimeDelay = selfDestructTimeDelay;
    }




}
