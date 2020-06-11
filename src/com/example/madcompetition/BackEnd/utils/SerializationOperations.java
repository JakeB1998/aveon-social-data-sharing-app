/*
 * File name:  SeriliazationOperations.java
 *
 * Programmer : Jake Botka
 * ULID: JMBOTKA
 *
 * Date: Mar 10, 2020
 *
 * Out Of Class Personal Program
 */
package com.example.madcompetition.BackEnd.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * <insert class description here>
 *
 * @author Jake Botka
 *
 */
public class SerializationOperations
{

	 public static boolean serilizeObjectToFile(File file, Object obj) {
		 boolean z = false;
	        try {
	            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
	            out.writeObject(obj);
	            out.flush();
	            out.close();
	            z = true;

	        } catch (IOException e)
	        {
	            e.printStackTrace();
	            e.printStackTrace();
	        }


	            return z;


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
	            input.close();
	        } catch (IOException e )
	        {
	        	
	            e.printStackTrace();
	            

	        }
	        catch (ClassNotFoundException e1)
	        {
	            e1.printStackTrace();

	        }
	        return x;
	    }
	    
	    public static Object deserializeFileToObject(File file)
	    {
	        Object x = null;
	        try {
	            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
	            x = input.readObject();
	        } catch (IOException e )
	        {
	            e.printStackTrace();

	        }
	        catch (ClassNotFoundException e1)
	        {
	            e1.printStackTrace();

	        }
	        return x;
	    }

}
