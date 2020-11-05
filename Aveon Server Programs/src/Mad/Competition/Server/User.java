package Mad.Competition.Server;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.Inet4Address;
import java.util.ArrayList;

import com.example.madcompetition.backend.account.AccountInformation;
import com.example.madcompetition.backend.security.Credentials;
import com.example.madcompetition.backend.server.ClientServerMessage;
import com.example.madcompetition.backend.server.ftp.FileData;


public class User implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AccountInformation restrictedAccount;
	
	private String userName;
	private String hashedPassword;



	private Inet4Address previousAdress;
	
	private ArrayList<ClientServerMessage> messageQue;
	private ArrayList<FileData> fileMessageQue;
	/**
	 * @return the fileMessageQue
	 */
	public ArrayList<FileData> getFileMessageQue()
	{
		return fileMessageQue;
	}
	/**
	 * @param fileMessageQue the fileMessageQue to set
	 */
	public void setFileMessageQue(ArrayList<FileData> fileMessageQue)
	{
		this.fileMessageQue = fileMessageQue;
	}


	private ArrayList<Inet4Address> previousAdresses;

	

	public User(AccountInformation accountInfo)
	{
		

		this.restrictedAccount = accountInfo;
		
	
			System.out.println(accountInfo.getAccountId());

			messageQue = new ArrayList(0);
			fileMessageQue = new ArrayList(0);
			previousAdresses = new ArrayList(0);
			
			previousAdress = accountInfo.getAdressInfo();
			userName = accountInfo.getUserName();
			hashedPassword = accountInfo.getHashedPassword();
		
	
		

		// check if a saved message que exists
		
		
	}
	/**
	 * @return the restrictedAccount
	 */
	public AccountInformation getRestrictedAccount()
	{
		return restrictedAccount;
	}
	/**
	 * @param restrictedAccount the restrictedAccount to set
	 */
	public void setRestrictedAccount(AccountInformation restrictedAccount)
	{
		this.restrictedAccount = restrictedAccount;
	}
	/**
	 * @return the previousAdress
	 */
	public Inet4Address getPreviousAdress()
	{
		return previousAdress;
	}
	/**
	 * @param previousAdress the previousAdress to set
	 */
	public void setPreviousAdress(Inet4Address previousAdress)
	{
		this.previousAdress = previousAdress;
	}
	/**
	 * @return the messageQue
	 */
	public synchronized ArrayList<ClientServerMessage> getMessageQue()
	{
		return messageQue;
	}
	/**
	 * @param messageQue the messageQue to set
	 */
	public void setMessageQue(ArrayList<ClientServerMessage> messageQue)
	{
		this.messageQue = messageQue;
	}
	/**
	 * @return the previousAdresses
	 */
	public ArrayList<Inet4Address> getPreviousAdresses()
	{
		return previousAdresses;
	}
	/**
	 * @param previousAdresses the previousAdresses to set
	 */
	public void setPreviousAdresses(ArrayList<Inet4Address> previousAdresses)
	{
		this.previousAdresses = previousAdresses;
	}
	/**
	 * @return the userName
	 */
	public String getUserName()
	{
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	/**
	 * @return the hashedPassword
	 */
	public String getHashedPassword()
	{
		return hashedPassword;
	}
	/**
	 * @param hashedPassword the hashedPassword to set
	 */
	public void setHashedPassword(String hashedPassword)
	{
		this.hashedPassword = hashedPassword;
	}

	
	public boolean equals(Object obj)
	{
		User user = null;
		if (obj instanceof User)
		{
			user = (User)obj;
			
		}
		
		if (user != null)
		{
			if ( this.getUserName().equals(user.getUserName())) //user.getRestrictedAccount().getAccountId() == this.getRestrictedAccount().getAccountId()
				
			{
				return true;
			}
		}
		else
		{
			return false;
		}
		
		return false;
		

	}
	
	
	public String toString()
	{
		return this.restrictedAccount.toString();
	}

}
