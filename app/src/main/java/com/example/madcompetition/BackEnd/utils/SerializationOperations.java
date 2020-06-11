package com.example.madcompetition.BackEnd.utils;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationOperations
{
    public static byte[] serializeStringToBtyeArray(String data)
    {
       return data.getBytes();
    }


    public static File serilizeObjectToFile(File file, Object obj) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(obj);
            out.flush();
            out.close();

        } catch (IOException e)
        {
            e.printStackTrace();
            Log.e("File", e.getMessage());
        }


            return file;


    }



    public static byte[] serializeObjectToBtyeArray(Object data)
    {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            ObjectOutputStream out1 =  new ObjectOutputStream(out);
            out1.writeObject(data);
            out1.flush();

        } catch (IOException e)
        {

        }

        return  out.toByteArray();


    }


    public static String deserializeToString(byte[] data)
    {


        return new String(data);
    }
    public static Object deserializeToObject(byte[] data)
    {
        Object x = null;
        try {
            ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(data));
            x = input.readObject();
        } catch (IOException e )
        {
           Log.getStackTraceString( e.getCause());

        }
        catch (ClassNotFoundException e1)
        {
            Log.e("Error", e1.getMessage());

        }
        return x;
    }

    public static byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }



}
