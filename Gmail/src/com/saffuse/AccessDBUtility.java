package com.saffuse;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AccessDBUtility {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		 //create connection string
		 String dbName = "ADF_TEST";
	     String serverip="10.104.50.21";
	     String serverport="1433";
	     String url = "jdbc:sqlserver://"+serverip+"\\SQLEXPRESS:"+serverport+";databaseName="+dbName+"";
	     System.out.println(url);

		// Declare the JDBC objects.
		Connection con = null;
	
		
		try {
			
			// Establish the connection.
    		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        	con = DriverManager.getConnection(url); 
        	System.out.println("Connectin Successfully.");
			
			
			
			/*SQLServerDataSource ds = new SQLServerDataSource();
			ds.setIntegratedSecurity(true);
			ds.setServerName("10.104.50.21");
			ds.setPortNumber(1433); 
			ds.setDatabaseName("ADF_TEST");
			con = ds.getConnection();
			System.out.println("Connectin Successfully.");*/
		}
		
		catch(Exception ex){
			ex.printStackTrace();
		}
		   
	}

}
