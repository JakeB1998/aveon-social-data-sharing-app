/*
 * File name:  AccountHandler.java
 *
 * Programmer : Jake Botka
 * ULID: JMBOTKA
 *
 * Date: Feb 11, 2020
 *
 * Out Of Class Personal Program
 */
package Mad.Competition.Server;

import com.example.madcompetition.BackEnd.account.AccountInformation;

/**
 * <insert class description here>
 *
 * @author Jake Botka
 *
 */
public class AccountHandler implements Runnable
{
	private AccountInformation account;

	/**
	 * 
	 */
	public AccountHandler()
	{
		// TODO Auto-generated constructor stub
	}
	public AccountHandler(AccountInformation account)
	{
		this.account = account;
		// TODO Auto-generated constructor stub
	
	}
	
	@Override
	public void run()
	{
		
		User s = null;
		
		
	}


}
