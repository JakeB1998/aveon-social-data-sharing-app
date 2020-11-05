package Mad.Competition.Server.FTP;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.example.madcompetition.backend.account.AccountInformation;
import com.example.madcompetition.backend.server.ClientServerMessage;
import com.example.madcompetition.backend.server.CredentialsRequest;
import com.example.madcompetition.backend.server.MessageType;
import com.example.madcompetition.backend.server.ftp.FileData;
import com.example.madcompetition.backend.utils.SerializationOperations;

import Mad.Competition.Server.ClientServerMessageHandler;
import Mad.Competition.Server.Server;
import Mad.Competition.Server.SqlUserDatabaseInterface;
import Mad.Competition.Server.User;

public class FtpClientHandler implements Runnable
{

	private Socket s;
	private FileInputStream objectIn = null;
	private FileOutputStream objectOut = null;
	

	private boolean sendAlive;
	private boolean connectionAlive;
	private boolean skip;
	private  int clientId;
	
	private final int ID_LENGTH = 5;
	

	
	private boolean closedConnection;

	
	private User currentUser;
	
	
	private  ArrayList<ClientServerMessage> requestQueMessages;

	
	private boolean firstRead;
	
	
	public FtpClientHandler(Socket s,DataInputStream in, DataOutputStream out)
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
		
			timeStart = System.currentTimeMillis();
			long time = System.currentTimeMillis();
			long newTime = time + 1000;
			try
			{
				while (s.getInputStream().available() < 4) 
				{
					// using this to assure that efo exception doesnt pop up from emtry stream when reading in object
				}
				File data = readFileData();
			
				if (currentUser != null)
				{
					ObjectOutputStream objectOut = new ObjectOutputStream(s.getOutputStream());
					objectOut.writeObject(data);
					objectOut.flush();
					objectOut.close();
				}
				
			
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
				
		
	}
	
	private void handleMessage(ClientServerMessage message)
	{
		
		
	}
	
	public File readFileData()
	{
		try
		{
			ObjectInputStream objectIn = new ObjectInputStream(s.getInputStream());
			Object obj = objectIn.readObject();
			System.out.println("Object read");
			
			if (obj instanceof ClientServerMessage)
			{
				
				ClientServerMessage message = (ClientServerMessage)obj;
				if (message.getMessageType() == MessageType.FileMessage)
				{
					FileData data = (FileData)SerializationOperations.deserializeToObject(message.getDataPayload());
					if (data != null)
					{
						String ext = data.getFilePath().substring(data.getFilePath().lastIndexOf("."));
					File newFile = new File(data.getOneTimeDownloadKey() + ext);
					System.out.println("File path : " + newFile.toPath().toString());
					newFile.createNewFile();
					
					System.out.println(message.getSender().getUserName());
					System.out.println(data.toString());
					System.out.println(" File Path : " + data.getFilePath());
					System.out.println("Fille length : " + data.getFileLength());
					//currentUser = this.findUser(message.getSender());
					
			
					this.saveFile(s, newFile, newFile.length(), data.getOneTimeDownloadKey());
					return newFile;
					}
					else
					{
						System.out.println("data file was null");
					}
				}
				else if (message.getMessageType() == MessageType.FileRequest)
				{
					FileData data = (FileData)SerializationOperations.deserializeToObject(message.getDataPayload());
					File file  = FtpDriver.findFile( data.getOneTimeDownloadKey());
					//find file
					
					if (file != null)
					{
					System.out.println("Found file");
					this.sendFile(file);
					}
					else
					{
						System.out.println("File not found");
					}
					System.out.println("User requesting file");
				}
				else
				{
					System.out.println("Incorrect message type : " + message.getMessageType().toString());
				}
				
			}
			else
			{
				System.out.println("Object not instance of clientServerMessage Class");
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Object not read");
		return null;
		
	}
	
	public void sendFile(File file) throws IOException {
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[4096];
		
		while (fis.read(buffer) > 0) {
			dos.write(buffer);
			dos.flush();
		}
		System.out.println("Wrote File");
		
		this.closeConnection();
		
		
	}
	
	private void saveFile(Socket clientSock, File file, long fileSize, String oneTimePassword) throws IOException {
		System.out.println("waiting for bytes");
		
		System.out.println("Started to read file");
		DataInputStream dis = new DataInputStream(clientSock.getInputStream());
		FileOutputStream fos = new FileOutputStream(file.toPath().toString());
		byte[] buffer = new byte[8096];
		
		long filesize = fileSize; 
		int read = 0;
		int totalRead = 0;
		long remaining = filesize;
		while((read = dis.read(buffer, 0,buffer.length)) > 0) {
			totalRead += read;
			remaining -= read;
			System.out.println("read " + totalRead + " bytes.");
			fos.write(buffer, 0, read);
		}
		FtpDriver.addData(oneTimePassword, file )
	;
		System.out.println("File sucessfuy wrote");
		
		
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
		
	
		Server.safePrintln("Shutdown of connection complete\n\n\n");
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
	
	public User findUser(AccountInformation info)
	{
		User user = Server.getUser(new User(info));
		
		if (user == null)
		{
			System.out.println("User is null");
		}
		return user;
	}
	

	public void saveUser(User user)
	{
		
		 SqlUserDatabaseInterface db = SqlUserDatabaseInterface.getInstance();
		 db.saveUser(user);
		 Server.safePrintln("User saved");
		
	}
	
	
	@Override
	public String toString()
	{
		return s.getInetAddress().getHostAddress().toString();
	}


}
