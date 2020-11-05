/*
 * File name:  SqlDatabaseInterface.java
 *
 * Programmer : Jake Botka
 * ULID: JMBOTKA
 *
 * Date: Mar 31, 2020
 *
 * Out Of Class Personal Program
 */
package Mad.Competition.Server;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.example.madcompetition.backend.utils.SerializationOperations;




/**
 * <insert class description here>
 *
 * @author Jake Botka
 *
 */
public class SqlUserDatabaseInterface
{
	
	private final String DATABASE_FILE_NAME = "Users.db";
	private final String TABLE_NAME = "Users";
	
	private final static SqlUserDatabaseInterface instance = new SqlUserDatabaseInterface();
	

	/**
	 * 
	 */
	private SqlUserDatabaseInterface()
	{
	
	}
	
	
	public static SqlUserDatabaseInterface getInstance()
	{
		return instance;
	}
	
	public Connection connectToDatabaseTable() {
        Connection conn = null;
        try {
            // db parameters
        	String url = "jdbc:sqlite:" + DATABASE_FILE_NAME;
           
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            
            Server.safePrintln("Connection to SQLite has been established.");
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
	}
	 public void createNewDatabase() {
		
		 if (this.doesDatabseTableExist() == false)
		 {
		 String url = "jdbc:sqlite:" + DATABASE_FILE_NAME;
	 
	        try (Connection connection = DriverManager.getConnection(url)) {
	            if (connection != null) {
	                DatabaseMetaData meta = connection.getMetaData();
	                System.out.println("The driver name is " + meta.getDriverName());
	                System.out.println("A new database has been created.");
	                connection.close();
	            }
	 
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
		 }
		 else
		 {
			 Server.safePrintln(("Database Already Exists"));
		 }
	        
	    }
	 
	 public void createDatabaseTable()
	 {
		 Connection connection = this.connectToDatabaseTable();
		 Statement statement;
			try
			{
				statement = connection.createStatement();
			    statement.setQueryTimeout(30);  // set timeout to 30 sec.
			    statement.executeUpdate("DROP TABLE IF EXISTS" + " " +  TABLE_NAME);
			    statement.executeUpdate("CREATE TABLE " + TABLE_NAME + 
			    " (id INTEGER PRIMARY KEY, " 
			    + DatabaseContract.COLUMN_NAME_USER_ID  + " STRING, " 
			    + DatabaseContract.COLUMN_NAME_USER_OBJ    + " BlOB)");
			    
			    Server.safePrintln("Database table created");
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
	            try {
	                if (connection != null) {
	                    connection.close();
	                }
	            } catch (SQLException ex) {
	                System.out.println(ex.getMessage());
	            }
	        }

		 
	 }
	 
	 public ArrayList<SqlUserRowResults> queryDatabaseTable()
	 {
		ArrayList<SqlUserRowResults> results = new ArrayList(0);
			 Statement statement;
			 String sql;
			
				 sql = "SELECT id," + DatabaseContract.COLUMN_NAME_USER_ID + "," +
			 DatabaseContract.COLUMN_NAME_USER_OBJ 
		                 + " FROM " + TABLE_NAME;
			 
	
			 try (Connection conn = this.connectToDatabaseTable();
		             Statement stmt  = conn.createStatement();
		             ResultSet rs    = stmt.executeQuery(sql))
			 {
				 
				 while (rs.next())
				 {
					 int id = rs.getRow();
					 int userId = Integer.parseInt(rs.getString(DatabaseContract.COLUMN_NAME_USER_ID));
					 if (rs.getBytes(DatabaseContract.COLUMN_NAME_USER_OBJ) != null)
					 {
					 User user = (User) SerializationOperations.deserializeToObject(rs.getBytes(DatabaseContract.COLUMN_NAME_USER_OBJ));
					 results.add(new SqlUserRowResults(id, userId, user));
					 }
				 }
				 
				
				
				 return results;
		            
		 
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
		 
		 return null;
	    }
	    
		    

		 
	
	 
	 public void getResults(String WhereClause, String selectionArgs)
	 {
		 
	 }
	 
	 public boolean doesDatabseTableExist()
	 {
		 return false;
	 }
	 
	 public void insertObject(User object)
	 {
		 if ( this.isUserInDatabase(object) == false) 
		 {
		 
		        String sql = "INSERT INTO " + TABLE_NAME + "(" + DatabaseContract.COLUMN_NAME_USER_ID 
		        		+"," + DatabaseContract.COLUMN_NAME_USER_OBJ
		        		+ ") VALUES(?,?)";
		        sql = String.format(sql , "s");
		 
		        try (Connection conn = this.connectToDatabaseTable();
		                PreparedStatement pstmt = conn.prepareStatement(sql)) {
		         
		        	pstmt.setString(1, Integer.toString(object.getRestrictedAccount().getAccountId()));
		        	
		        	pstmt.setBytes(2, SerializationOperations.serializeObjectToBtyeArray(object) );
		            pstmt.executeUpdate();
		            Server.safePrintln("Data inserted to database");
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    
		        Server.safePrintln("Data Attemted to insert completed");
		 }
		 else
		 {
			 Server.safePrintln("User already in database");
		 }
		 
		 
	 
	}
	 
	 
	 public void deleteDatabase()
	 {
		 
	 }
	 
	 
	 public void deleteTable()
	 {
		 Connection conn = this.connectToDatabaseTable();
		try
		{
			Statement statement = conn.createStatement();
			statement.execute("DROP TABLE " + TABLE_NAME);
			Server.safePrintln("Database table deleted");
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }
	 
	 public void deleteRow(int rowId)
	 {
		 String sql = "DELETE FROM " + TABLE_NAME + " " +  "WHERE id = " + Integer.toString(rowId);
		 
	        try (Connection conn = this.connectToDatabaseTable();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            // execute the delete statement
	            pstmt.executeUpdate();
	            
	            conn.close();
	 
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	        
	   
		 
	 }
	 
	 public void updateRow(int rowId, String collumn,Object data)
	 {
		  String sql = "UPDATE Users SET UserObject = ? "
	                
	                + "WHERE UserID = ?";
		 final String ID = "?";
		
		 
	
	        
		 if (rowId >= 0)
		 {
			 
             
		        
			 
		        try (Connection conn = this.connectToDatabaseTable();
		                PreparedStatement pstmt = conn.prepareStatement(sql)) {
		 
		        	pstmt.setBytes(1, SerializationOperations.serializeObjectToBtyeArray(data));
		        	pstmt.setInt(2,rowId);   
		        	pstmt.executeUpdate();
		            conn.close();
		 
		        } catch (SQLException e) {
		            System.out.println(e.getMessage());
		        }
		 }
		 else
		 {
			Server.safePrintln("Invlid row id");
		 }
		 
		
	        
		 
	 }
	 /**
	  * 
	  * @param user
	  * @return
	  */
	 public boolean saveUser(User user)
	 {
		 ArrayList<SqlUserRowResults> results = this.queryDatabaseTable();
		 
		 for (SqlUserRowResults res : results)
		 {
			 System.out.println(res.getUser().getUserName());
			 if (res.getUser().equals(user))
			 {
				 int rowId = res.getUserId();
				 this.updateRow(rowId, DatabaseContract.COLUMN_NAME_USER_OBJ, user);
				 System.out.println("Found user in database to save : " + res.getUserId());
				 return true;
			 }
		 }
		 return false;
	 }
	 
	 /**
	  * 
	  * @param user
	  * @return
	  */
	 public boolean isUserInDatabase(User user)
	 {
		 ArrayList<SqlUserRowResults> results = this.queryDatabaseTable();
		 if (results != null)
		 {
			 if (this.isDatabaseEmpty() == false)
			 {
				 for (SqlUserRowResults res : results)
				 {
					 if (res.getUser().equals(user))
					 {
						 Server.safePrintln("User already in database");
						 return true;
					 }
				 }
		
			
			 }
		 }
		 return false;
		 
	 }
	 
	 public boolean isDatabaseEmpty()
	 {
		 ArrayList<SqlUserRowResults> results = this.queryDatabaseTable();
		
		 if (results != null)
		 {
			
				 if (results.size() <= 0)
				 {
					 Server.safePrintln("The database table is empty");
					 return true;
				 }
		
		 }
		 return false;
	 }
	 
	 public class DatabaseContract
	 {
		public static final String COLUMN_NAME_USER_OBJ = "UserObject";
		 public static final String COLUMN_NAME_USER_ID = "UserID";
	 }
	 
	 
	 
	 
	 

}
