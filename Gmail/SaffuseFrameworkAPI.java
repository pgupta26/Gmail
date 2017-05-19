package com.saffuse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SaffuseFrameworkAPI {
	
	WebDriver driver;
	String prjName;
	String tcName;
	String browser;
	String platform;
	String instanceName;
	String sauceUser;
	String moduleName;
	String proxy;
	String ResultFlag="Pass";
	int stepCount=1;
	String jobId;
	Properties prop;
	FileReader file;
	String automationPath;
	FirefoxProfile profile;
	DesiredCapabilities dc;
	String url;
	WebDriverWait wait;
	Actions ac;
	String executionDateTime;
	String currentDate;
	String currentUser;
	Date startTime;
	Date endTime;
	String resultsFolderPath;
	String htmlFilePath;
	
	ArrayList<String> sAction=new ArrayList<String>();
	ArrayList<String> sDesc=new ArrayList<String>();
	ArrayList<String> sStatus=new ArrayList<String>();
	ArrayList<String> sDateTime=new ArrayList<String>();
	
	
	public SaffuseFrameworkAPI(){};
	
	public SaffuseFrameworkAPI(String prjName,String testEnvironment,String instanceName,String sauceUser,String moduleName,String proxy) throws IOException{
		
			//set local variables
			this.moduleName=moduleName;
			this.prjName=prjName;
			this.instanceName=instanceName;
			this.sauceUser=sauceUser;
			this.proxy=proxy;
			
			//get current logged in user name
			currentUser=System.getProperty("user.name");
			
			//set automation path
		    automationPath=System.getenv("Automation_Path");
			System.out.println("Automation Path:- "+automationPath);
			
			//set drivers path
			System.setProperty("webdriver.gecko.driver",automationPath+"Resoures\\geckodriver.exe");
			System.setProperty("webdriver.chrome.driver",automationPath+"Resoures\\chromedriver.exe");
			System.setProperty("webdriver.ie.driver",automationPath+"Resoures\\iexploredriver.exe");
			
			//read properties file
			prop=new Properties();
			file=new FileReader(automationPath+"SaffuseFramework\\constants\\constant.properties");
			prop.load(file);
			
			//getting the test environment
			String[] environmentSplit=testEnvironment.replaceAll("\\{","").replaceAll("\\}","").split("="); 
			
			tcName=environmentSplit[0].trim();
			System.out.println("testCaseName: "+tcName);
			
			String[] environment1=environmentSplit[1].split(Pattern.quote("||"));
			
			platform=environment1[0].trim();
			System.out.println("platform: "+platform);
			
			browser=environment1[1].trim();
			
			//launching the browser
			if(sauceUser.equalsIgnoreCase("local")){
				if(proxy.equalsIgnoreCase("yes")){
					if(browser.equalsIgnoreCase("firefox")){
						
						//read from properties file
						int proxyType=Integer.parseInt(prop.getProperty("network.proxy.type"));
						
						String httpProxy=prop.getProperty("network.proxy.http");
						int httpProxyPort=Integer.parseInt(prop.getProperty("network.proxy.http_port"));
						
						String sslProxy=prop.getProperty("network.proxy.ssl");
						int sslProxyPort=Integer.parseInt(prop.getProperty("network.proxy.ssl_port"));
						
						String ftpProxy=prop.getProperty("network.proxy.ftp");
						int ftpProxyPort=Integer.parseInt(prop.getProperty("network.proxy.ftp_port"));
						
						String sockProxy=prop.getProperty("network.proxy.socks");
						int sockProxyPort=Integer.parseInt(prop.getProperty("network.proxy.socks_port"));
						
						//setting firefox profile
					    profile = new FirefoxProfile();
						profile.setPreference("network.proxy.type",proxyType);
						
						profile.setPreference("network.proxy.http",httpProxy);
						profile.setPreference("network.proxy.http_port",httpProxyPort);
						
						profile.setPreference("network.proxy.ssl",sslProxy);
						profile.setPreference("network.proxy.ssl_port",sslProxyPort);
						
						profile.setPreference("network.proxy.ftp",ftpProxy);
						profile.setPreference("network.proxy.ftp_port",ftpProxyPort);
						
						profile.setPreference("network.proxy.socks",sockProxy);
						profile.setPreference("network.proxy.socks_port",sockProxyPort);
						
						//setting environment
					    dc=new DesiredCapabilities();
						dc.setBrowserName("firefox");
						dc.setPlatform(Platform.WINDOWS);
						dc.setCapability(FirefoxDriver.PROFILE, profile);
						dc.setCapability("marionette", false);
						driver=new FirefoxDriver(dc);
						
					}else if(browser.equalsIgnoreCase("chrome")){
						
					}else if(browser.equalsIgnoreCase("Internet Explorer")){
						
					}else if(browser.equalsIgnoreCase("safari")){
		
					}else{
						System.out.println("Please specify Browser name.");
					}
				}	
				else{
					System.out.println("Script development for without proxy is in progress.");
				}
			}
			else{
				System.out.println("Script development for sauce labs execution is in progress.");
			}
		}
	
	public void startSelenium(String url){
		
		try{
		startTime=new Date();
		System.out.println("Fetched Url:- "+url);
		System.out.println("InstanceName:- "+instanceName);
		
		this.url=url.replace("{instanceName}",instanceName);
		driver.get(this.url);
		driver.manage().window().maximize();
	    ac=new Actions(driver);
	   
	    stepDetails("Launching main Page","Successfully launching main page: "+url+"in browser "+browser,"Pass");
		}
		catch(Exception ex){
			ex.printStackTrace();
			stepDetailsWithScreenShot("Launching main Page","Failed to launch main page"+url+"in browser "+browser,"Fail");
		}
	}
	
	public void stepDetails(String action,String desc,String status){
		sAction.add(action);
		sDesc.add(desc);
		sStatus.add(status);
		sDateTime.add(getCurrentDateTime());
		
		if(status.equalsIgnoreCase("Fail")){
			ResultFlag=status;
		}
		stepCount++;
	}
	
	public void stepDetailsWithScreenShot(String action,String desc,String status){
		
		captureScreenShot();
		
		sAction.add(action);
		sDesc.add(desc + "Screenshot:" + tcName + "_" + stepCount + ".png");
		sStatus.add(status);
		sDateTime.add(getCurrentDateTime());
		
		if(status.equalsIgnoreCase("fail")){
			ResultFlag=status;
		}
		stepCount++;
	}
	
	public String captureScreenShot() {
		
		String screenShotFilePath = null;
	    try
	    {
	      File scrFile = null;
	      
	      if (sauceUser.equalsIgnoreCase("Local")) {
	        scrFile = (File)((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	      }
	      else {
	        scrFile = (File)((TakesScreenshot)new Augmenter().augment(driver)).getScreenshotAs(OutputType.FILE);
	      }
	      
	      resultsFolderPath = automationPath + "Results" + File.separator + prjName + File.separator + instanceName + File.separator + currentUser + File.separator + getCurentDate() + File.separator + moduleName+File.separator;
	      
	      File fi = new File(resultsFolderPath);
	      if (!fi.exists()) {
	        fi.mkdirs();
	      }
	      
	      System.out.println("Screenshot path" + resultsFolderPath + tcName + "_" + stepCount + ".png");
	      
	      FileUtils.copyFile(scrFile, new File(resultsFolderPath + tcName + "_" + stepCount + ".png"));
	      FileUtils.copyFileToDirectory(new File(resultsFolderPath + tcName + "_" + stepCount + ".png"), new File(getTempPath() + "CurrentRunReports" + File.separator));
	      screenShotFilePath = "Screenshot:" + tcName + "_" + stepCount + ".png";
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    
	    return screenShotFilePath;
	}
	
	
	 public String getTempPath() {
		    try { 
		      String cananicalpath = File.createTempFile("temp_file", "tmp").getCanonicalPath();
		      int s = cananicalpath.lastIndexOf("\\");
		      return cananicalpath.substring(0, s + 1);
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
		    return null;
		  }
	
	public String getCurrentDateTime(){
		try{
			
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("dd:MM:yy hh:mm:ss");
			executionDateTime=sdf.format(date);
		
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return executionDateTime;
	}
	
	public String getCurentDate(){
		try{
			
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yy");
			currentDate=sdf.format(date);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return currentDate;
	}
	
	/*public void startSelenium(String url1,String instanceName){
		
		System.out.println("Fetched Url:- "+url1);
		System.out.println("InstanceName:- "+this.instanceName);
		
		url=url1.replace("{instanceName}",this.instanceName);
		driver.get(url);	
	}*/
	
	public String getUrl(){
		String url=prop.getProperty("url");
		return url;
	}
	
	public WebElement getWebElement(String locator){
		
			String[] tokens=locator.split("=");
			String  locatorType=tokens[0].trim();
			String strlocator=tokens[1].trim();
		
			WebElement webElement = null;

			if(locatorType.equalsIgnoreCase("class")){
				
				webElement=driver.findElement(By.className(strlocator));

			}else if(locatorType.equalsIgnoreCase("ID")){

				webElement = driver.findElement(By.id(strlocator));

			}else if(locatorType.equalsIgnoreCase("NAME")){

				webElement = driver.findElement(By.name(strlocator));

			}else if(locatorType.equalsIgnoreCase("CSS")){

				webElement = driver.findElement(By.cssSelector(strlocator));

			}else if(locatorType.equalsIgnoreCase("link")){

				webElement = driver.findElement(By.linkText(strlocator));
			}else{
				
				webElement = driver.findElement(By.xpath(strlocator));
				
			}
		return webElement;
	}
	
	public By getByLocator(String locator){
		
		String[] tokens=locator.split("=");
		String  locatorType=tokens[0].trim();
		String strlocator=tokens[1].trim();
	
		By by = null;

		if(locatorType.equalsIgnoreCase("class")){
			
			by=By.className(strlocator);
			
		}else if(locatorType.equalsIgnoreCase("ID")){

			by = By.id(strlocator);

		}else if(locatorType.equalsIgnoreCase("NAME")){

			by = By.name(strlocator);

		}else if(locatorType.equalsIgnoreCase("CSS")){

			by = By.cssSelector(strlocator);

		}else if(locatorType.equalsIgnoreCase("link")){

			by = By.linkText(strlocator);
		}else{
			
			by = By.xpath(strlocator);
			
		}
	return by;
}
	
	public void method(String command,String locator,String description){
		
		try{
			switch(command){
				case "click":
					getWebElement(locator).click();
					stepDetails("Click on button/link/image","Succesfully click on "+description,"Pass");
					break;
			
				case "waitForElementPresent":
					wait=new WebDriverWait(driver,Long.parseLong(description));
					wait.until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator)));
					stepDetails("Wait for element present","Expected element is found.","Pass");
					break;
			}	
		}
		catch(Exception ex){
			ex.printStackTrace();
			stepDetailsWithScreenShot("Wait for element present","Element is not found", "Fail");
		}
	}
	
	public void method(String command,String locator,String description,String expectedValue){
		
		try{
			switch(command){
				case "type":
					getWebElement(locator).sendKeys(expectedValue);
					stepDetails("Setting the value of "+description,description+" is set to: "+expectedValue,"Pass");
					break;
				
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			stepDetailsWithScreenShot("Wait for element present","Element is not found", "Fail");
		}
	}
	
	public static String getDateTimeDifference(Date startDate, Date endDate)
	  {
	    String time = "";
	    try {
	      System.out.println("startDate:" + startDate + " endDate:" + endDate);
	      long diff = endDate.getTime() - startDate.getTime();
	      long diffSeconds = diff / 1000L % 60L;
	      long diffMinutes = diff / 60000L % 60L;
	      long diffHours = diff / 3600000L % 24L;
	      


	      if (diffHours < 10L) {
	        time = time + "0" + diffHours + ":";
	      } else {
	        time = time + diffHours + ":";
	      }
	      if (diffMinutes < 10L) {
	        time = time + "0" + diffMinutes + ":";
	      } else {
	        time = time + diffMinutes + ":";
	      }
	      if (diffSeconds < 10L) {
	        time = time + "0" + diffSeconds;
	      } else {
	        time = time + diffSeconds;
	      }
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    
	    return time;
	  }
	
	  public void generateXMLReport() throws IOException{
		  
		try{ 
        StringBuilder xmlStringBuilder=new StringBuilder();
		xmlStringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><TestReport><TestEnvironment><ProjectName>"+prjName+
				"</ProjectName><TestCaseName>"+tcName+"</TestCaseName><Environment>"+platform+"|"+browser+" / "+instanceName+"</Environment> \n"
				+"<ExecutionTime>"+getCurrentDateTime()+"</ExecutionTime><Status>"+ResultFlag+"</Status></TestEnvironment><Steps>");
		
		for(int i=0;i<sAction.size();i++){
			xmlStringBuilder.append("<Step><Sr>"+(i+1)+"</Sr><Action>"+sAction.get(i)+"</Action>"
					+ "<Description>"+sDesc.get(i)+"</Description>"
			+ "<Time>"+sDateTime.get(i)+"</Time><Status>"+sStatus.get(i)+"</Status></Step>");
		}
		
		xmlStringBuilder.append("</Steps></TestReport>");
		
		 File dir = new File(resultsFolderPath);
	      if (!dir.exists()) {
	        dir.mkdirs();
	      }
	   
	     String tempXml = getTempPath() + tcName + ".xml";
	     System.out.println("**tempXml**" + tempXml);
	     
		 //write to file with OutputStreamWriter
	     OutputStream outputStream = new FileOutputStream(tempXml);
	     Writer writer=new OutputStreamWriter(outputStream);
	     writer.write(xmlStringBuilder.toString());
	     writer.close();
	     
	     generateHtml(tempXml, htmlFilePath);
	      
	      File reportFile = new File(getTempPath() + "CurrentRunReports");
	      if (!reportFile.exists()) {
	        reportFile.mkdir();
	      }
	      generateHtml(tempXml, getTempPath() + "CurrentRunReports" + File.separator + tcName + ".html");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	  
	 public void generateHtml(String xmlFile, String htmlFilePath)
	  {
	    String xslFile = automationPath + "Results" + File.separator + "Results1.xsl";
	    System.out.println("Entered into generateHtml CurrentRunReports");
	    try {
	      TransformerFactory tFactory = TransformerFactory.newInstance();
	      Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));
	      
	      transformer.transform( new StreamSource( xmlFile),  new StreamResult(new FileOutputStream(htmlFilePath)));
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	  }	
	
	public void stopSelenium(){
		
		try{
			if (driver != null) {
		          jobId = ((RemoteWebDriver)driver).getSessionId().toString();
		          System.out.println("jobid=" + jobId);
		        }
		      else{
		    	  System.out.println("***driver object is null while type casting from webdrier to remotewebdrier for jobid****");   
		      } 
			
			resultsFolderPath = (automationPath + "Results" + File.separator + prjName + File.separator + instanceName + File.separator + currentUser + File.separator + getCurentDate() + File.separator + moduleName + File.separator );
			System.out.println("Results Folder Path: " +resultsFolderPath);
				   
			htmlFilePath = (resultsFolderPath + tcName + "_" + jobId + "_" + ResultFlag + ".html");
			System.out.println("Html File Name: " + htmlFilePath);
		      
		    endTime=new Date();
		    String timeDiff=getDateTimeDifference(startTime,endTime);
		    stepDetails("Execution Time:(HH:MM:SS) ", "Time taken for Execution is :" + timeDiff, "Pass");
		    generateXMLReport();
		    driver.quit();
			
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		

	}

}
