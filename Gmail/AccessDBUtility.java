package classes;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class AccessDBUtility {
	
    String automationPath;
	Connection cn;
	
	public Connection getConnection(String dbName){
		try{  
			   automationPath=System.getenv("Automation_Path");
			   String database=automationPath+"Data"+File.separator+dbName;  
			   
			   System.out.println("**********DataBaseName************ "+database);

			   String url="jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};Dbq=" + database + ";";  
			  
			   Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");  
			   cn=DriverManager.getConnection(url, "", ""); 
			   System.out.println("Connection established successully.");
			   
			}
		catch(Exception ee)
		{
			System.out.println(ee);
		} 
		return cn;	  
	}
	
	public void insertIntoDB(String dbName,String query){
		try{
			cn=getConnection(dbName);
			Statement st=cn.createStatement(); 
			st.executeQuery(query);
			st.close();
		    cn.close();
			System.out.println("*******Record inserted successfully.********");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void updateIntoDB(String dbName,String query){
		try{
			
			cn=getConnection(dbName);
			Statement st=cn.createStatement(); 
			st.executeQuery(query);
			st.close();
		    cn.close();
			System.out.println("*******Record updated successfully.********");
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public HashMap<String,String> getFromDBSingleRecord(String dbName,String query){
		HashMap<String,String> result = null;
		try{
			
			cn=getConnection(dbName);
			Statement st=cn.createStatement(); 
			ResultSet rs=st.executeQuery(query);
			ResultSetMetaData metaData = rs.getMetaData();
			int count = metaData.getColumnCount();
			for(int i=0;i<count;i++){
					result.put(metaData.getColumnName(i),rs.getString(i));
			}
			st.close();
		    cn.close();
			System.out.println("*******Data returned successfully.********");
		}
		catch(Exception ex){
			System.out.println("*******No record found for this criteria********");
			ex.printStackTrace();
		}
		return result;
	}
	
	public ResultSet getFromDBResultSet(String dbName,String  query){
		ResultSet rs = null;
		try{
			
			cn=getConnection(dbName);
			Statement st=cn.createStatement(); 
		    rs=st.executeQuery(query);
			st.close();
		    cn.close();
			System.out.println("*******Data returned successfully.********");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return rs;
	}

	public static void main(String[] args){
		
	}
}
