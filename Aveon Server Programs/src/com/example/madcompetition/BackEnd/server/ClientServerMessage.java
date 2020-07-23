package com.example.madcompetition.BackEnd.server;

import com.example.madcompetition.BackEnd.account.AccountInformation;

import java.io.Serializable;

public class ClientServerMessage implements Serializable
{

    private static final long serialVersionUID = 1583910155443648055L;
    private AccountInformation sender;
    private AccountInformation reciever;

    private MessageType messageType;
    private MessageSubType messageSubType;



    private byte[] dataPayload;

    public ClientServerMessage()
    {

    }

    public ClientServerMessage(AccountInformation sender, AccountInformation reciever, MessageType messageType, byte[] dataPayload)
    {
        this.sender = sender;
        this.reciever = reciever;
        this.messageType = messageType;
        this.setDataPayload(dataPayload);

    }

    public AccountInformation getSender() {
        return sender;
    }

    public void setSender(AccountInformation sender) {
        this.sender = sender;
    }

    public AccountInformation getReciever() {
        return reciever;
    }

    public void setReciever(AccountInformation reciever) {
        this.reciever = reciever;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public byte[] getDataPayload() {
        return dataPayload;
    }

    public void setDataPayload(byte[] dataPayload) {
        this.dataPayload = dataPayload;
    }

    @Override
    public String toString()
    {
        //String s = Integer.toString(dataPayload.length);
        String s = "";
        if (sender != null)
        {
            s = sender.toString();
        }


        return s + "\nMessage Type : " + messageType.toString()
                + "Payload length : " + dataPayload.length;

    }

    public MessageSubType getMessageSubType() {
        return messageSubType;
    }

    public void setMessageSubType(MessageSubType messageSubType) {
        this.messageSubType = messageSubType;
    }
}
