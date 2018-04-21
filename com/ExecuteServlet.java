package com;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;


public class ExecuteServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		//Create objects
		PrintWriter out=res.getWriter();
		
		// set content type
		res.setContentType("text/html");
		
		//execute job
		executeJob(req);
		
		RequestDispatcher rd=req.getRequestDispatcher("executeScript.jsp");
		out.println("Script Execution has started in Jenkins");
		rd.include(req, res);
	}
	
	private String getProperty(String key) {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			
			ServletContext context = getServletContext();
			String configPath = context.getRealPath("/WEB-INF/config/config.properties");
			input = new FileInputStream(configPath);

			// load a properties file
			prop.load(input);

			// get the property value
			return prop.getProperty(key);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		 return "";
	}
	
	private void executeJob(HttpServletRequest req){
		
		// read config parameters
		String jenkins_url=getProperty("Jenkins_Url");
		
		//get request parameters
		String jobName=req.getParameter("selection");
		String Users=req.getParameter("users");
		String RampUpTime=req.getParameter("rampup");
		String Iterations=req.getParameter("iteration");
		
		try {
		      URL url = new URL (jenkins_url+"/job/"+jobName+"/buildWithParameters"); // Jenkins URL localhost:8080, job named 'test'
		      String jenkins_UserName=getProperty("UserName"); // username
		      String jenkins_Password=getProperty("Password"); // password or API token
		      String authStr = jenkins_UserName +":"+  jenkins_Password;
		      String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));

		      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		      connection.setRequestMethod("POST");
		      connection.setDoOutput(true);
		      connection.setRequestProperty("Authorization", "Basic " + encoding);
		      
		      String urlParams="Users="+Users+"&RampUpTime="+RampUpTime+"&Iterations="+Iterations;
		      byte[] postData = urlParams.getBytes("utf-8");
		      try(DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
		        wr.write(postData);
		      }

		      InputStream content = connection.getInputStream();
		      BufferedReader in   =
		          new BufferedReader (new InputStreamReader (content));
		      String line;
		      while ((line = in.readLine()) != null) {
		        System.out.println(line);
		      }
		    } catch(Exception e) {
		      e.printStackTrace();
		    }
	}
}
