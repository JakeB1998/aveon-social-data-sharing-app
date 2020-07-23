package Mad.Competition.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.SQLException;

import Mad.Competition.Server.SqlUserDatabaseInterface.DatabaseContract;

public class TestDriver
{

	public static void main(String[] args) throws IOException
	{
		File file = new File("ObjectTest123456");
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		
		User user = null;
		try
		{
			user = (User)in.readObject();
			System.out.println(user.toString());
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SqlUserDatabaseInterface db = SqlUserDatabaseInterface.getInstance();
		//user.getRestrictedAccount().setAccountId(123445);
		if (db.saveUser(user))
		{
		System.out.println(db.queryDatabaseTable().get(0).getUser().getRestrictedAccount().getAccountId());
		}//db.deleteTable();
		//db.createDatabaseTable(db.connectToDatabaseTable());
		
			

				//db.insertObject(user);

				
	
	}

}
