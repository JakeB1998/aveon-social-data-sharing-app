package com.example.madcompetition.backend.security;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Credentials implements Serializable
{

    private String username;
    private String hashedPassword;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private PrivateKey AesPrivateKey;


    private String uniqueId;

    private static final long serialVersionUID = 1L;



    public Credentials(String username, String password)
    {
        this.username = username;
        setHashedPassword(password);
        KeyPair pair = AsymmetricEncryption.getInstance().generateKeys();
        if (pair != null)
        {
            this.setPublicKey(pair.getPublic());
            this.setPrivateKey(pair.getPrivate());
        }



    }

    private void sendServerPublicKey()
    {

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

    public PrivateKey getAesPrivateKey() {
        return AesPrivateKey;
    }

    public void setAesPrivateKey(PrivateKey aesPrivateKey) {
        AesPrivateKey = aesPrivateKey;
    }
}
