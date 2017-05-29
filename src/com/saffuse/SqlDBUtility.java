package com.saffuse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

public class SqlDBUtility {

	String automationPath;
	Connection cn;
	Statement st;
	ResultSet rs;
	Properties prop;
	FileReader file;
	
	public SqlDBUtility() throws IOException {
		
		//set environment variable
		automationPath=System.getenv("Automation_Path");
		System.out.println("********Automation Path********** "+automationPath);
		
		//read properties file
		prop=new Properties();
		file=new FileReader(automationPath+File.separator+"SaffuseFramework\\constants\\constant.properties");
		prop.load(file);

	}
	
	public Connection getConnection(String dbName){
		
		try{ 
			   String serverip=prop.getProperty("dbServerIp");
			   String serverport=prop.getProperty("dbServerPort");
			
			   String database=automationPath+"\\Data"+File.separator+dbName;  
			   
			   System.out.println("**********DataBaseName************ "+database);

			   String url = "jdbc:sqlserver://"+serverip+"\\SQLEXPRESS:"+serverport+";databaseName="+database+""; 
			   System.out.println("Connection string: "+url);
			  
			   Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();  
			   cn=DriverManager.getConnection(url, "", ""); 
			   System.out.println("****** Connection established successully. ********");
			   
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
			st=cn.createStatement(); 
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
			st=cn.createStatement(); 
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
