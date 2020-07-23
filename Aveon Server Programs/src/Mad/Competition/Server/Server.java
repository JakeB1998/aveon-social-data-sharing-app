/*
 * File name:  Server.java
 *
 * Programmer : Jake Botka
 * ULID: JMBOTKA
 *
 * Date: Feb 11, 2020
 *
 * Out Of Class Personal Program
 */
package Mad.Competition.Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * <insert class description here>
 *
 * @author Jake Botka
 *
 */
public class Server
{
	
	public static int NumberOfConnectedDevices = 0;
	public static String IP = "10.135.77.90";
	
	public static volatile  ArrayList<ConnectedDevice> connectedDevices = new ArrayList<>(0);
	public static volatile ArrayList<ClientHandlerS> connectedClients =  new ArrayList<>(0);
	
	private static  ArrayList<User> allUserAccounts = new ArrayList<>(0);

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		SqlUserDatabaseInterface db = SqlUserDatabaseInterface.getInstance();
		

		Server.loadUsers();
		
		
		String IP1 = InetAddress.getLocalHost().getHostAddress();
		System.out.println(IP1);
		//Server.connectToDatabase(null);
		ClientHandlerS client  = null;
		  


	      ServerSocket welcomeSocket = null;
		try
		{
			welcomeSocket = new ServerSocket(6788);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
Thread t1 = new Thread(new Update());
t1.start();

		while (true) {
	  
	            Socket connectionSocket = null;
	           
				try
				{
					
					System.out.println("Listening for connection");
					connectionSocket = welcomeSocket.accept();
					
					System.out.println("Connection accepted");
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

				try
				{
					connectionSocket.setTcpNoDelay( true );
				} catch (SocketException e)
				{
		
					e.printStackTrace();
				}
				DataInputStream inFromClient =  new DataInputStream(connectionSocket.getInputStream()); 
						DataOutputStream  outToClient =  new DataOutputStream(connectionSocket.getOutputStream()); 


	           ClientHandlerS x = new ClientHandlerS(connectionSocket, inFromClient, outToClient);
	           
	          Thread t = new Thread(x);
	        t.start();
	        
	       // x.addRequst();
	        Server.NumberOfConnectedDevices +=1;
	}


	}
	public static void safePrintln(String s) {
		  synchronized (System.out) {
		    System.out.println(s);
		  }
		}
	public static void AddUser(User user) 
	{
			File file = new File("ObjectTest123456");
			FileOutputStream fileOut;
			try
			{
				fileOut = new FileOutputStream(file);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(user);
				Server.safePrintln("Object written to file");
				
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
	public static void loadUsers()
	{

		SqlUserDatabaseInterface db = SqlUserDatabaseInterface.getInstance();
	
		allUserAccounts.clear();
		int index = 0;
		for ( SqlUserRowResults res: db.queryDatabaseTable())
		{
			allUserAccounts.add(res.getUser());
			index++;
		}
		System.out.println("Users Count : " + index);
		
	}
	public static boolean existsInDatabase(User user) 
	{
		boolean z = false;
		
		if (allUserAccounts.size() > 0)
		{
			for (User user1 : allUserAccounts)
			{
		
				if (user1.equals(user))
				{
					System.out.println("Matched User : " + user1.toString() + "\n Matching User " + user.toString());
					z = true;
				}
				
			}
		}
		
		   
		  
		  
		  return z;
		}
	
	
	public static User getUser(User user)
	{
		System.out.println(user.getUserName());
		return allUserAccounts.get(allUserAccounts.indexOf(user));
	}
	public static User getUser(String username, String hashedPassword)
	{
		for (User user : allUserAccounts)
		{
			if (user != null)
			{
				if (user.getUserName() != null && user.getHashedPassword() != null)
				{
					if (user.getUserName().equals(username) && user.getHashedPassword().equals(hashedPassword))
					{

						
						
						return user;
				
					}
				}
			}
		}
		
		return null;
	
	}
	
	
	public static void connectToDatabase(DatabaseServerRequest request)
	{
		final String ip = "localhost";
		
		try
		{
			Socket socket = new Socket(ip, 6789);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
