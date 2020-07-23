package Mad.Competition.Server;

import java.util.ArrayList;

import com.example.madcompetition.BackEnd.server.ClientServerMessage;
import com.example.madcompetition.BackEnd.server.ClientServerObjectRequest;
import com.example.madcompetition.BackEnd.server.ClientServerObjectResults;
import com.example.madcompetition.BackEnd.server.MessageSubType;
import com.example.madcompetition.BackEnd.server.MessageType;
import com.example.madcompetition.BackEnd.server.ObjectRequest;
import com.example.madcompetition.BackEnd.utils.SerializationOperations;

public class ClientServerMessageHandler
{


	public static void handleMessageToUser(ClientServerMessage message, User target)
	{
		target.getMessageQue().add(message);
		System.out.println("Message length" + message.getDataPayload().length);
		SqlUserDatabaseInterface.getInstance().saveUser(target);
	}
	public static void handleIncomingObjectRequest(User host,ClientServerMessage message)
	{
		Object obj = SerializationOperations.deserializeToObject(message.getDataPayload());
		ClientServerObjectResults resultsToSend = null;
		
		
		
		if (obj instanceof ClientServerObjectRequest)
		{
			Server.safePrintln("object request recieved from : " + host.getRestrictedAccount().toString());
			ClientServerObjectRequest request = (ClientServerObjectRequest) obj;
			byte[] returnData = handleObjectRequest(request.getObjectRequest(), request.getData());
			resultsToSend = new ClientServerObjectResults(request.getObjectRequest(), returnData);
			ClientServerMessage returnMessage = new ClientServerMessage(null,message.getSender(),
			MessageType.DataLogToUser,SerializationOperations.serializeObjectToBtyeArray(resultsToSend));
			host.getMessageQue().add(returnMessage);
			Server.safePrintln("Results were aded to be sent back to user");
			
			
		}
		
	}
	
	public static void handleDataLogToServer(User user, ClientServerMessage message)
	{
		if (message != null)
		{
			SqlUserDatabaseInterface db = SqlUserDatabaseInterface.getInstance();
			if (message.getMessageSubType() != null)
			{
				MessageSubType subType = message.getMessageSubType();
				switch (subType)
				{
				case CreateAccount:
					db.insertObject(user);
					Server.AddUser(user);
					Server.safePrintln("USer created account");
					break;
				case AuthenticateUser:
					break;
				
					default:
				}
			}
		}
		
	}
	
	public static void handleDataLogToDatabase(ClientServerMessage message)
	{
		
	}
	
	private static byte[] handleObjectRequest(ObjectRequest request, String data)
	{
		System.out.println("Seraching for user with username : " + data);
		SqlUserDatabaseInterface db = SqlUserDatabaseInterface.getInstance();
		switch (request)
		{
			case AccountInformation:
				ArrayList<SqlUserRowResults> result = db.queryDatabaseTable();
				for (SqlUserRowResults res : result)
				{
					System.out.println(res.getUser().getRestrictedAccount().getUserName());
					if (res.getUser().getRestrictedAccount().getUserName().equals(data))
					{
						Server.safePrintln("Found user for result");
						return SerializationOperations.serializeObjectToBtyeArray(res.getUser().getRestrictedAccount());
					
					}
					
				}
				break;
			case LocationInformation:
				break;
			default:
		}
	
		return new byte[0];
		
	}

}
