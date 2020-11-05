package com.example.madcompetition.backend.utils;

import java.io.Serializable;
import java.util.HashMap;

public class Data implements Serializable {

    public HashMap<String,Object> data;

    public Data()
    {
        data = new HashMap<>(0);
    }

    public void add(String key, Object obj)
    {
        data.put(key, obj);
    }


    public boolean contains(String key)
    {
        return data.containsKey(key);
    }

    public Object getObject(String key)
    {
        return data.get(key);
    }


    public int length()
    {
        return data.size();
    }





}
