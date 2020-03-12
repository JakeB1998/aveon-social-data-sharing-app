package com.example.madcompetition.BackEnd;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Credentials implements Serializable
{

    private String username;
    private String hashedPassword;

    private PublicKey publicKey;
    private transient PrivateKey privateKey;

    private String uniqueId;

    private static final long serialVersionUID = 1L;



    public Credentials(String username, String password)
    {
        generateKeyPair();

        this.username = username;
        setHashedPassword(Encryption.hashMessage(password));


    }

    private void generateKeyPair()
    {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            publicKey = kp.getPublic();
            setPrivateKey(kp.getPrivate());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }



    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public CredentialsType getCredType() {
        return credType;
    }

    public void setCredType(CredentialsType credType) {
        this.credType = credType;
    }

    public String getUsername() {
        if (username == null)
        {
           // username = AppManager.NULLABLE_STRING;
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public enum CredentialsType
    {
        Locked, Unlocked
    }

    private CredentialsType credType;

    public Credentials()
    {

    }




    public Credentials(CredentialsType credType)
    {

    }

}
