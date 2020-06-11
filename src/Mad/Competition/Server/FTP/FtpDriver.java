package Mad.Competition.Server.FTP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.madcompetition.BackEnd.server.ftp.FileData;

import Mad.Competition.Server.ClientHandlerS;
import Mad.Competition.Server.ConnectedDevice;
import Mad.Competition.Server.DatabaseServerRequest;
import Mad.Competition.Server.Server;
import Mad.Competition.Server.SqlUserDatabaseInterface;
import Mad.Competition.Server.SqlUserRowResults;
import Mad.Competition.Server.Update;
import Mad.Competition.Server.User;
import javafx.collections.FXCollections;

public class FtpDriver
{
	public static int NumberOfConnectedDevices = 0;
	public static String IP = "10.135.77.90";
	
	public static final File directory = new File("FTP_Directory");
	
	private static HashMap<String, File> localDatabase = new HashMap(0);

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		
		// load files
		
		
		String IP1 = InetAddress.getLocalHost().getHostAddress();
		System.out.println(IP1);
		//Server.connectToDatabase(null);
		ClientHandlerS client  = null;
		  


	      ServerSocket welcomeSocket = null;
		try
		{
			welcomeSocket = new ServerSocket(6789);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


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
			


	           FtpClientHandler x = new FtpClientHandler(connectionSocket, null,null);
	          
	           
	          Thread t = new Thread(x);
	        t.start();
	        
	       // x.addRequst();
	        Server.NumberOfConnectedDevices +=1;
	}


	}
	
	public static void addData(String key, File data)
	{
		localDatabase.put(key, data);
		//Server.getUser(user).getFileMessageQue().add(data);
		//SqlUserDatabaseInterface.getInstance().saveUser(user);
		System.out.println("Data added to hashmap");
		System.out.println("Amount in hashmap : " + localDatabase.size());
		
	}
	
	public static File findFile(User user, String password)
	{
		ArrayList<FileData> data = Server.getUser(user).getFileMessageQue();
		
		for (FileData x : data)
		{
			if (x.getOneTimeDownloadKey().equals(password))
			{
				System.out.println("Foudd file :" + x.toString());
				data.remove(x);
				return x.getFile();
			}
			
		}
		
		System.out.println("Did not find file");
		return null;
	}
	
	public static File findFile(String password)
	{
		System.out.println("Amount in hashmap : " + localDatabase.size());
		if (localDatabase.containsKey(password))
		{
			return localDatabase.get(password);
		}
		return null;
	}


}
