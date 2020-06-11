package com.example.madcompetition.BackEnd.security;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import Mad.Competition.Server.Encryption;

public class Credentials implements Serializable
{

    private String username;
    private String hashedPassword;


    private String uniqueId;

    private static final long serialVersionUID = 1L;



    public Credentials(String username, String password)
    {
  

        this.username = username;


    }

    



    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
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

    

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }



}
