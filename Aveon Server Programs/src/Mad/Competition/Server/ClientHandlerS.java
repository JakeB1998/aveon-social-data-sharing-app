/*
 * File name:  ClientHandlerS.java
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
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import org.sqlite.core.DB;

import com.example.madcompetition.BackEnd.account.AccountInformation;
import com.example.madcompetition.BackEnd.server.ClientServerMessage;
import com.example.madcompetition.BackEnd.server.CredentialsRequest;
import com.example.madcompetition.BackEnd.server.MessageType;
import com.example.madcompetition.BackEnd.utils.SerializationOperations;

/**
 * <insert class description here>
 *
 * @author Jake Botka
 *
 */
public class ClientHandlerS implements Runnable
{

	private Socket s;
	private ObjectInputStream objectIn = null;
	private ObjectOutputStream objectOut = null;
	

	private boolean sendAlive;
	private boolean connectionAlive;
	private boolean skip;
	private  int clientId;
	
	private final int ID_LENGTH = 5;
	

	
	private boolean closedConnection;

	
	private User currentUser;
	
	
	private  ArrayList<ClientServerMessage> requestQueMessages;

	
	private boolean firstRead;
	
	
	public ClientHandlerS(Socket s,DataInputStream in, DataOutputStream out)
	{
		currentUser = null;
		skip = false;
		firstRead = false;
		closedConnection = false;
		sendAlive = false;
		this.s = s;
	
		
		
		
		requestQueMessages = new ArrayList(0);
	
		
		
	}
	

	long timeStart;
	

	@Override
	public void run()
	{
		
		
		
		try
		{
			timeStart = System.currentTimeMillis();
			long time = System.currentTimeMillis();
			long newTime = time + 1000;
			while (s.getInputStream().available() <= 0 && time < newTime )
			{
				time = System.currentTimeMillis();
			}
			// first read rptocol for user information
			if (firstRead == false)
				try
				{
					
						firstRead = true;
						objectIn = new ObjectInputStream(s.getInputStream());
						Object obj =  objectIn.readObject();
						if (obj instanceof AccountInformation)
						{
						
							AccountInformation info = (AccountInformation) obj;
					
							String deviceId = info.getUniqueDeviceID();
							User user = new User(info);
							System.out.println(deviceId);
							this.saveUser(user);

							if (Server.existsInDatabase(user) == false)
							{
								
								 currentUser = new User(info);
								
								
								 if (currentUser != null)
								 {
									
									 // reload database then retry if fails then discconect
									
								 }
								 else
								 {
									 currentUser = Server.getUser(user);
								
								 }
							}
							else
							{
					
								currentUser = Server.getUser(user);
								
								System.out.println(deviceId);
								/*
								if (currentUser.getRestrictedAccount().getUniqueDeviceID().equals(deviceId) == false)
								{
									Server.safePrintln("Unathorized device");
								}
								*/
							
							}
						}
						else if (obj instanceof CredentialsRequest)
						{
							CredentialsRequest request = (CredentialsRequest)obj;
							request.setOnReturn(true);
							User user = Server.getUser(request.getUsername(),request.getHashedPassword());
							
							if (user != null)
							{
								System.out.println("Valid credentials");
								request.setAccountInformation(SerializationOperations.serializeObjectToBtyeArray(user.getRestrictedAccount()));
							
							}
							
							else
							{
								request.setAccountInformation(new byte[0]);
								
							}
							ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
							out.writeObject(request);
							out.flush();
							out.close();
							
							
							
							s.close();
						}
						
						

				} catch (ClassNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			
			
		
			long timenew = System.currentTimeMillis() + 500;
            while (s.getInputStream().available() < 4 || System.currentTimeMillis() < timenew)
            {

            }
			// second actionin exchange protocol
			//
			//
			//
			if (s.getInputStream().available() > 0)
			{
				
				
				Server.safePrintln( currentUser.getRestrictedAccount().getUserName() + " : Client sent a message");
				//Server.safePrintln( currentUser.getRestrictedAccount().getFriendList().length + " : Client sent a message");
				// reads from user
				try
				{
					Object obj = objectIn.readObject();
					if (obj instanceof ClientServerMessage )
					{
						ClientServerMessage zzz = (ClientServerMessage) obj;
						Server.safePrintln(zzz.getMessageType().toString());
						if (zzz.getMessageType() == MessageType.ObjectAmountToRead)
						{
							String num = new String( zzz.getDataPayload());
							int index = Integer.parseInt(num);
						
							
							for (int i = 0; i < index; i++)
							{
								Object objNew = objectIn.readObject();
								
								if (objNew instanceof ClientServerMessage)
								{
									ClientServerMessage m = (ClientServerMessage) objNew;
									Server.safePrintln("\nClient Sever message from clinet : " + m.toString() + "\n");
									this.handleMessage(m);
								}
								
							}
						}
						
					}
				} catch (ClassNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			// checks if any messages needs to be sent from user from saved database que
			if (currentUser != null)
			{
				if (objectOut == null)
				{
					objectOut = new ObjectOutputStream(s.getOutputStream());
				}
				
				Server.safePrintln( currentUser.getUserName() + " : Amount in users message que : " + Integer.toString(currentUser.getMessageQue().size()));
				if (currentUser.getMessageQue().size() > 0)
				{
					
					  ClientServerMessage number = new ClientServerMessage(null,currentUser.getRestrictedAccount() ,
	                            MessageType.ObjectAmountToRead, Integer.toString(currentUser.getMessageQue().size()).trim().getBytes());
					  objectOut.writeObject(number);
					  objectOut.flush();
					  
					  int index = 0;
					for (ClientServerMessage message : currentUser.getMessageQue())
					{
						objectOut.writeObject(message);
						objectOut.flush();
						index++;
		
					}
					
					String str = Integer.toString(index);
					Server.safePrintln(str + " messages were sent to clinet from server");
					
					currentUser.getMessageQue().clear();
				}
				else
				{
					  ClientServerMessage number = new ClientServerMessage(null,currentUser.getRestrictedAccount() ,
	                            MessageType.ContinueProtocol, null);
					  
					  objectOut.writeObject(number);
					  objectOut.flush();
				}
			}
			
			
			else
			{
				System.out.println("End of connection");
			}
			
			// ends connection
			this.closeConnection();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
	
	private void handleMessage(ClientServerMessage message)
	{
		if (message != null)
		{
			AccountInformation target = message.getReciever();
			User targetUser = null;
			
			if (target != null)
			{
				Server.safePrintln(message.getReciever().toString());
				 targetUser = Server.getUser(new User(target));
				 Server.safePrintln(target.getUserName());
			}
			
			Server.safePrintln(message.getMessageType().toString());
			switch (message.getMessageType())
			{
				case MessageToUser:
			
				ClientServerMessageHandler.handleMessageToUser(message, targetUser);
				Server.safePrintln("Income message added to targets message que, " + "Target is  : " + target.getUserName() + "Targets que size = " 
				+ Integer.toString(targetUser.getMessageQue().size()) );
					break;
				case IncomingObjectRequest:
					ClientServerMessageHandler.handleIncomingObjectRequest(currentUser,message);
					break;
				case DataLogToServer:
					ClientServerMessageHandler.handleDataLogToServer(currentUser, message);
					break;
				case DataLogToUser:
					break;
				case LocationToServer:
					break;
				case ObjectAmountToRead:
					break;
				case FileMessage:
					ClientServerMessageHandler.handleMessageToUser(message, targetUser);
					Server.safePrintln("Income message added to targets message que with message type : " + message.getMessageType().toString() + ", " + "Target is  : " + target.getUserName() + "Targets que size = " 
					+ Integer.toString(targetUser.getMessageQue().size()) );
					break;
				default:
					break;
			
			}
		}
		
	}
	
	
	
	public void closeConnection()
	{
		System.out.println("Shutdown of connect has been initiated");
		try
		{
			
			s.shutdownInput();
			s.shutdownOutput();
			
			s.close();
			
			System.out.println("Connection was closed");
			Server.NumberOfConnectedDevices -= 1;
			Server.connectedClients.remove(this);

			closedConnection = true;
		
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			Server.NumberOfConnectedDevices -= 1;
			Server.connectedClients.remove(this);
			e.printStackTrace();
		}
		
	
		
		String timeToComplete =Long.toString(System.currentTimeMillis() - timeStart);
		Server.safePrintln("Time to complete interaction : " + timeToComplete);
	}
	
	
	public void addRequst(ClientServerMessage message)
	{
		if (currentUser.getMessageQue() != null)
		{
			currentUser.getMessageQue().add(message);
		}
		Server.safePrintln("Request Recieved");
	}
	
	
	public void sendErrorToUser()
	{
		try
		{
			if (objectOut == null)
			{
				
				objectOut = new ObjectOutputStream(s.getOutputStream());
			}
			
				ClientServerMessage message = null;
				objectOut.writeObject(message);
				objectOut.flush();
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//discconect
	}
	
	public void saveUser(User user)
	{
		
		 SqlUserDatabaseInterface db = SqlUserDatabaseInterface.getInstance();
		 db.saveUser(user);
	
		
	}
	
	
	@Override
	public String toString()
	{
		return s.getInetAddress().getHostAddress().toString();
	}


}
