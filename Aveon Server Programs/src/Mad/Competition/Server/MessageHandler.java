/*
 * File name:  MessageHandler.java
 *
 * Programmer : Jake Botka
 * ULID: JMBOTKA
 *
 * Date: Feb 22, 2020
 *
 * Out Of Class Personal Program
 */
package Mad.Competition.Server;

import java.net.Inet4Address;

import com.example.madcompetition.backend.account.AccountInformation;
import com.example.madcompetition.backend.server.ClientServerMessage;
import com.example.madcompetition.backend.server.MessageType;

/**
 * <insert class description here>
 *
 * @author Jake Botka
 *
 */
public class MessageHandler implements Runnable
{
	private ClientServerMessage message;

	/**
	 * 
	 */
	public MessageHandler(ClientServerMessage message)
	{
		this.message = message;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run()
	{
		AccountInformation destinationInfo = null;
		Inet4Address addressInfo = null;
		MessageType messageType  = null;
		if (this.message.getMessageType() != null)
		{
			messageType = this.message.getMessageType();
		}
		else
		{
			
		}
		
		if (message.getReciever()!= null)
		{
			destinationInfo = message.getReciever();
		}
		else
		{
			
		}
		
		if (destinationInfo.getAdressInfo() != null)
		{
			addressInfo = destinationInfo.getAdressInfo();
		}
		
		
		// find account
		//Server.allUserAccounts.contains(arg0)
	}

}
