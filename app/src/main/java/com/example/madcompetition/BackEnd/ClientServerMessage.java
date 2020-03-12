package com.example.madcompetition.BackEnd;

import java.io.Serializable;

public class ClientServerMessage implements Serializable
{
    private AccountInformation sender;
    private AccountInformation reciever;

    private MessageType messageType;

    private static final long serialVersionUID = 1583910155443648055L;



    private byte[] dataPayload;

    public ClientServerMessage()
    {

    }

    public ClientServerMessage(AccountInformation sender, AccountInformation reciever, MessageType messageType, byte[] dataPayload)
    {
        this.sender = sender;
        this.reciever = reciever;
        this.setDataPayload(dataPayload);
        this.messageType = messageType;

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
}
