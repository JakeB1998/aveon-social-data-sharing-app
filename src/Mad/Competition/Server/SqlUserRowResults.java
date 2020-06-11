/*
 * File name:  SqlUserRowResultsContainer.java
 *
 * Programmer : Jake Botka
 * ULID: JMBOTKA
 *
 * Date: Apr 7, 2020
 *
 * Out Of Class Personal Program
 */
package Mad.Competition.Server;

/**
 * <insert class description here>
 *
 * @author Jake Botka
 *
 */
public class SqlUserRowResults
{

	private int id;
	private int userId;
	private User user;
	
	/**
	 * 
	 */
	public SqlUserRowResults(int id, int userId, User object)
	{
		this.id = id;
		this.userId = userId;
		this.user = object;
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	/**
	 * @return the userId
	 */
	public int getUserId()
	{
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	/**
	 * @return the user
	 */
	public User getUser()
	{
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user)
	{
		this.user = user;
	}
	
	
	public String toString()
	{
		boolean bool = false;
		
		if (user != null)
		{
			bool = true;
		}
		String str = "ID : " + Integer.toString(id) + "\n"
				+ 	"User Id : " + Integer.toString(userId) + "\n"
				+ " User Object : " + Boolean.toString(bool);
		
		return str;
	}
	

}
