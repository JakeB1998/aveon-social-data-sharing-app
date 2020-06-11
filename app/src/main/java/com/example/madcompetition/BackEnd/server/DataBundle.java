package com.example.madcompetition.BackEnd.server;

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
