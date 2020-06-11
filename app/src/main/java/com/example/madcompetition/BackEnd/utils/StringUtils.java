package com.example.madcompetition.BackEnd.utils;

import android.util.Log;

import java.util.Random;

public class StringUtils {

    private String data;

    public StringUtils(String data)
    {

    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public static String generateID(int number)
    {

        String id = "";

        Random ran = new Random();
        for (int i = 0; i < number; i++)
        {
            id += Integer.toString(ran.nextInt(9));
        }


        return id;

    }

}
