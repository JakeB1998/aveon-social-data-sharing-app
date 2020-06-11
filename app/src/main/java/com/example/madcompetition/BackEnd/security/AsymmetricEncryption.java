package com.example.madcompetition.BackEnd.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.SealedObject;

public class AsymmetricEncryption {

    private final static AsymmetricEncryption instance = new AsymmetricEncryption();
    private AsymmetricEncryption()
    {

    }

    public static AsymmetricEncryption getInstance()
    {
        return instance;
    }



    public SealedObject ecryptObject(PublicKey publicKey, Object object)
    {
        return null;
    }

    public KeyPair generateKeys()
    {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(512);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
            return  null;
        }

    }


}
