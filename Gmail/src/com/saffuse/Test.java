package com.saffuse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Test {
	
	
	
	
	
	public Test(){
		System.out.println(this.getClass());
		System.out.println(this.getClass().getName());
		System.out.println(this.getClass().getSimpleName());
	}

	public static void main(String[] args) throws IOException {
		Test obj=new Test();
		
		
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\e5399484\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe");
		
		WebDriver driver=new ChromeDriver();
		driver.get("http://toolsqa.com/selenium-webdriver/how-to-use-geckodriver/");
		
		
		/*String prjName="Zeus";
		String tcName="hello";
		String platform="windows";
		String browser="firefox";
		String instanceName="QA";
		String ResultFlag="Pass";
		
		
		ArrayList<String> sAction=new ArrayList<String>();
		sAction.add("launching main page");
		sAction.add("wait for element present");
		ArrayList<String> sDesc=new ArrayList<String>();
		sDesc.add("successsfully.");
		sDesc.add("successfully.");
		ArrayList<String> sStatus=new ArrayList<String>();
		sStatus.add("Pass");
		sStatus.add("Pass");
		ArrayList<String> sDateTime=new ArrayList<String>();
		sDateTime.add("1");
		sDateTime.add("2");
		
		//define a XML String Builder
		 StringBuilder xmlStringBuilder=new StringBuilder();
			xmlStringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><TestReport><TestEnvironment><ProjectName>"+prjName+
					"</ProjectName><TestCaseName>"+tcName+"</TestCaseName><Environment>"+platform+"|"+browser+"</Environment> \n"
					+"<ExecutionTime>"+1+"</ExecutionTime><Instance>"+instanceName+"</Instance><Status>"+ResultFlag+"</Status></TestEnvironment><Steps>");
			
			xmlStringBuilder.append("<Step><Sr>Sr.no.</Sr><Action>Action</Action><Description>Description</Description><Time>Execution Time</Time><Status>Status</Status></Step>");
			for(int i=0;i<sAction.size();i++){
				xmlStringBuilder.append("<Step><Sr>"+i+"</Sr><Action>"+sAction.get(i)+"</Action><Description>"+sDesc.get(i)+"</Description><Time>"+sDateTime.get(i)+"</Time><Status>"+sStatus.get(i)+"</Status></Step>");
			}
			
			xmlStringBuilder.append("</Steps></TestReport>");
		
		 //write to file with OutputStreamWriter
        OutputStream outputStream = new FileOutputStream("C:\\Users\\e5399484\\Desktop\\test1.xml");
        Writer writer=new OutputStreamWriter(outputStream);
        writer.write(xmlStringBuilder.toString());
        writer.close();*/
		
		
		/*String xslFile = "E:\\Selenium_Projects\\Results\\Results1.xsl";
	    System.out.println("Entered into generateHtml CurrentRunReports");
	    try
	    {
	      TransformerFactory tFactory = TransformerFactory.newInstance();
	      Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));
	      
	      transformer.transform( new StreamSource( "C:\\Users\\e5399484\\Desktop\\test1.xml"),  new StreamResult(new FileOutputStream("C:\\Users\\e5399484\\Desktop\\test1.html")));
	    
	      System.out.println("Completed.");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }*/
		
		/*for(int i=0;i<10;i++){
			System.out.println("<Step><Sr>"+(i+1)+"</Sr></Step>");
		}*/
		
		
		
		/*String cananicalpath = File.createTempFile("temp_file", "tmp").getCanonicalPath();
		
		System.out.println(cananicalpath);
	     
		int s = cananicalpath.lastIndexOf("\\");
		System.out.println(s);
		System.out.println(cananicalpath.substring(0, s + 1));*/
		
		
		/*String automationPath=System.getenv("Automation_Path");
		
		String value=automationPath+"Results";
		System.out.println(value);
*/

		
	/*	Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("dd:MM:yy hh:mm:ss");
		System.out.println(sdf.format(date));*/
		
		/*String locator="id=username";
		String[] tokens=locator.split("=");
		String  locatorType=tokens[0];
		String strLocator=tokens[1];
		
		System.out.println(locatorType);
		System.out.println(strLocator);*/
		
		/*String url="http://tools{instanceName}.com/selenium-webdriver/how-to-use-geckodriver/";
		String instanceName="qa";
		
		System.out.println(url.indexOf("{"));
		System.out.println(url.indexOf("}"));
		
		//System.out.println(url.replaceAll("{instanceName}", "qa"));
		
		System.out.println(url.replace("{instanceName}", "qa"));
		*/
		
		/*String automationPath=System.getenv("Automation_Path");
		System.out.println("Automation Path: "+automationPath);
		
		Properties prop=new Properties();
		FileReader file=new FileReader(automationPath+"SaffuseFramework\\constants\\constant.properties");
		
		prop.load(file);
		
		String url=prop.getProperty("network.proxy.type");
		
		System.out.println(url);*/
	
		/*System.setProperty("webdriver.gecko.driver",automationPath+"Resoures\\geckodriver.exe");
		
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("network.proxy.type", 1);
		
		profile.setPreference("network.proxy.http","10.74.30.6");
		profile.setPreference("network.proxy.http_port",8080);
		
		profile.setPreference("network.proxy.ssl","10.74.30.6");
		profile.setPreference("network.proxy.ssl_port",8080);
		
		profile.setPreference("network.proxy.ftp","10.74.30.6");
		profile.setPreference("network.proxy.ftp_port",8080);
		
		profile.setPreference("network.proxy.socks","10.74.30.6");
		profile.setPreference("network.proxy.socks_port",8080);
		
		DesiredCapabilities dc=new DesiredCapabilities();
		dc.setBrowserName("firefox");
		dc.setPlatform(Platform.WINDOWS);
		dc.setCapability(FirefoxDriver.PROFILE, profile);
		dc.setCapability("marionette", false);
		
		WebDriver driver=new FirefoxDriver(dc);*/

		
	}

}
