
	
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
	//Properties prop;
	//FileReader file;
	//String automationPath;
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
	
	
	public SaffuseFrameworkAPI() throws IOException{};
	
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
		    //automationPath=System.getenv("Automation_Path");
			//System.out.println("Automation Path:- "+automationPath);
			
			//set drivers path
			System.setProperty("webdriver.gecko.driver",automationPath+File.separator+"Resoures\\geckodriver.exe");
			System.setProperty("webdriver.chrome.driver",automationPath+File.separator+"Resoures\\chromedriver.exe");
			System.setProperty("webdriver.ie.driver",automationPath+File.separator+"Resoures\\iexploredriver.exe");
			
			//read properties file
			//prop=new Properties();
			//file=new FileReader(automationPath+File.separator+"SaffuseFramework\\constants\\constant.properties");
			//prop.load(file);
			
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
	      
	      resultsFolderPath = automationPath +File.separator+ "Results" + File.separator + prjName + File.separator + instanceName + File.separator + currentUser + File.separator + getCurentDate() + File.separator + moduleName+File.separator;
	      
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
	
	public String getConstantValueFromProperties(String key){
		String url=prop.getProperty(key);
		return url;
	}
	
	public WebElement getWebElement(String locator){
		
		String[] tokens=locator.split("=");
		String  locatorType=tokens[0].trim();
		String strlocator=tokens[1].trim();
	
		WebElement webElement = null;
		WebDriverWait wait1 = new WebDriverWait(driver,30);
		
		try{

			wait1.until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator)));
			
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
		}
		catch (NoSuchElementException e) {
	        return null;
	    }catch(StaleElementReferenceException s){
			return null;
		}catch (TimeoutException e) {
	        return null;
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
	
	public String method(String sCommand) {
		String sText = "";
		try {
			switch (sCommand) {
			case "getUrl":
				sText = driver.getCurrentUrl();
				stepDetails(
						"Retrieving the absolute URL of current page",
						"URL of current page:" + sText,
						"Pass");
				return (sText);
			case "getTitle":
				sText = driver.getTitle();
				stepDetails("Retrieving page title",
						"Title of current page:"
								+ sText, "Pass");
				return (sText);
			case "closeBrowser":
				driver.close();
				stepDetails("Closing window",
						"Successfully closed window", "Pass");
				return "Pass";
			case "goBack":
				driver.navigate().back();
				stepDetails("Going back to the previous page",
						"Successfully navigated to previous page", "Pass");
				return "Pass";
			case "goForward":
				driver.navigate().forward();
				stepDetails("Going forward to the next page",
						"Successfully navigated to next page", "Pass");
				return "Pass";
			case "refresh":
				driver.navigate().refresh();
				stepDetails("Refresh current page",
						"Successfully refreshed current page", "Pass");
				return "Pass";
			case "windowMaximize":
				driver.manage().window().maximize();
				stepDetails("Maximizing window",
						"Successfully maximized window", "Pass");
				return "Pass";
			default:
				reportUnknowSeleniumCmdErr(sCommand);
			}

		} catch (Exception e) {
			e.printStackTrace();
			reportException(sCommand, e.toString());
		}
		return "";
	}

	public String method(String sCommand, String sLocator) {
		
		String sText = "";
		boolean bFlag = false;
		try {
			if(getWebElement(sLocator)!=null) {
				switch (sCommand) {
				case "click":	
					getWebElement(sLocator).click();
					stepDetails("Clicking button/link/image","Successfully clicked the button/link/image","Pass");
					return "Pass";
					
				case "mouseOver":		
                    Actions actionMouseOver = new Actions(driver);
                    actionMouseOver.moveToElement(getWebElement(sLocator)).build().perform();
					Thread.sleep(1000);
					stepDetails("MouseOver button/link/image",
							"Successfully MouseOver the button/link/image", "Pass");
					return "Pass";
				case "isChecked":
					bFlag = getWebElement(sLocator).isSelected();
					if (bFlag)
						stepDetails(
								"Verifying whether checkbox/radio button is checked or not",
								"Checkbox/radio button is checked", "Pass");
					else
						stepDetails(
								"Verifying whether checkbox/radio button is checked or not",
								"Checkbox/radio button is not checked", "Pass");
					return (Boolean.toString(bFlag));
				case "openUrl":
					driver.get(sLocator);
					stepDetails("Opening URL", "Successfully loaded URL:"
							+ sLocator, "Pass");
					return "Pass";
				case "getText":
					sText = getWebElement(sLocator).getText();
					return sText;
				default:
					reportUnknowSeleniumCmdErr(sCommand);
				}
			} else {
				captureScreenShot();
				reportElementNotFound(sCommand);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			reportException(sCommand, e.toString());
		}
		return "";
	}

	
	private void reportElementNotFound(String sCommand) {

		stepDetailsWithScreenShot("The element of a command " + "\""
				+ sCommand + "\" not found",
				"Did not find the field  at step : " + stepCount
				+ " : on the displayed page", "Fail");
	}
	
	private void reportException(String sCommand, String elementName) {

		System.out.println("**** in reportException ***");
		stepDetailsWithScreenShot(sCommand, 
		elementName+" element is not found", "Fail");
	}
	
	private void reportUnknowSeleniumCmdErr(String sCommand) {
		stepDetailsWithScreenShot("Unknown Selenium/effecta Command " + "\""
				+ sCommand + "\"", "Please Contact Automtaion team", "Fail");
	}
	
	public String method(String sCommand,String sLocator,String sDescription){
		String sText, encodedTitle, sValue = "";
		int sTargetFlag = 0;
		boolean checked, flag = false;
		String value;
		try{
			if(getWebElement(sLocator)!=null){
				switch(sCommand){
					case "click":
						getWebElement(sLocator).click();
						stepDetails("Clicking on button/link/image","Succesfully clicked on "+sDescription,"Pass");
						return "Pass";
					case "clickAndWait":
						getWebElement(sLocator).click();
						driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
						stepDetails("Clicking on button/link/image","Succesfully clicked on "+sDescription,"Pass");
						return "Pass";
					case "waitForElementPresent":
						wait=new WebDriverWait(driver,Long.parseLong(sDescription));
						wait.until(ExpectedConditions.visibilityOfElementLocated(getByLocator(sLocator)));
						stepDetails("Wait for element present","Expected element is found.","Pass");
						break;
					case "isDisplayed":
						value="Element is present";
						 try{				    	
						      if (getWebElement(sLocator).isDisplayed()) {
						    	  stepDetails("Validated Element :" + sDescription,	value, "Pass");
						      }	
						      else{	
						    	  value="Element is not present";
						    	  stepDetailsWithScreenShot("Validated Element: "+ sDescription, value,"Fail");
						      }
					      
					     }
					     catch(Exception e){
					    	 value="Element is not present";
					    	stepDetailsWithScreenShot("Validated Element: "+ sDescription, value,"Fail");
				            e.printStackTrace();
					     }				     
					    return value;
					case "isSelected":
						//boolean checked = false;
						checked = getWebElement(sLocator).isSelected();
						if (checked)
							stepDetails("Verifying whether \"" + sDescription
									+ "\" is selected or not", "\"" + sDescription
									+ "\" is selected", "Pass");

						else
							stepDetails("Verifying whether \"" + sDescription
									+ "\" is selected or not", "\"" + sDescription
									+ "\" is not selected", "Pass");
						return Boolean.toString(checked);
						
					case "isEnabled":
						boolean enabled = false;
						checked = getWebElement(sLocator).isEnabled();
						if (checked)
							stepDetails("Verifying whether \"" + sDescription
									+ "\" is enabled or not", "\"" + sDescription
									+ "\" is enabled", "Pass");

						else
							stepDetails("Verifying whether \"" + sDescription
									+ "\" is enabled or not", "\"" + sDescription
									+ "\" is not enabled", "Pass");
						return Boolean.toString(checked);
					case "getText":
						
						sText =  getWebElement(sLocator).getText();
						stepDetails("Retrieving " + sDescription,
								"Retrieved " + sDescription + ":"
										+ sText, "Pass");
						return sText;
					case "openWindow":
						driver.navigate().to(sLocator);
						stepDetails("Opening popup window " + sDescription,
								"Successfully opened " + sDescription
								+ " popup window", "Pass");
						return "Pass";
					case "type":
						sValue = sDescription;
						WebElement elmnt = getWebElement(sLocator);
						elmnt.clear();
						elmnt.sendKeys(sValue);
						stepDetails("Setting value of " + sDescription,
								sDescription + " is set to value: \""
										+ sValue + "\"",
										"Pass");
						return sValue;
					default:
						reportUnknowSeleniumCmdErr(sCommand);
					}
				}else{
				reportElementNotFound(sCommand);
			}
			
		}
		catch(Exception ex){
			ex.printStackTrace();
			if(sCommand.contains("waitFor")){
				reportException(sCommand, "Expected");
			}else{
				reportException(sCommand, sDescription);
			}		
		}
		return "";
	}
	
	public String effecta(String sCommand, String sLocator, String sValue,
			String sDescription) {
		String sText = "";
		String encodedTitle = "";
		int sTargetFlag = 0;
		boolean flag = false;
		try {
			if(getWebElement(sLocator)!=null){
				switch(sCommand){
					case "type":
						WebElement elmnt1 = getWebElement(sLocator);
						elmnt1.clear();
						elmnt1.sendKeys(sValue);
						stepDetails("Setting value of " + sDescription,
								sDescription + " is set to value: \""
									+ sValue + "\"",
									"Pass");
						return sValue;
				
				default:
					reportUnknowSeleniumCmdErr(sCommand);
				}
			} else {
				reportElementNotFound(sCommand);
			}

		} catch (Exception e) {
			e.printStackTrace();
			reportException(sCommand, sDescription);
		}
		return "";
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
			xmlStringBuilder.append("<Step><Sr>"+(i+1)+"</Sr><Action>"+sAction.get(i)+"</Action>");
			  
			String sDescContent=sDesc.get(i);
			
			if(sDescContent.contains("Screenshot:")){
				xmlStringBuilder.append("<Description>"+sDescContent.substring(0, sDescContent.indexOf("Screenshot:"))+"</Description>");
				xmlStringBuilder.append("<Screenshot>" + sDescContent.substring(sDescContent.indexOf("Screenshot:") + 11) +"</Screenshot>");
			}
			else{
			    xmlStringBuilder.append("<Description>"+sDesc.get(i)+"</Description>");
			}
			
				xmlStringBuilder.append("<Time>"+sDateTime.get(i)+"</Time><Status>"+sStatus.get(i)+"</Status></Step>");
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
	    String xslFile = automationPath+File.separator + "Results" + File.separator + "Results.xsl";
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
			
			resultsFolderPath = (automationPath+File.separator + "Results" + File.separator + prjName + File.separator + instanceName + File.separator + currentUser + File.separator + getCurentDate() + File.separator + moduleName + File.separator );
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

