package com.example.madcompetition.backend.server;

import java.io.Serializable;

public class DataBundle implements Serializable {

    private int numberOf;

    private Object[] dataObjects;


    public DataBundle(int num, Object[] data)
    {
        numberOf = num;
        data = data;
    }

}
