
	
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
				//System.out.println("close browser");
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
				
			default:
				reportUnknowSeleniumCmdErr(sCommand);
			}

		} catch (Exception e) {
			e.printStackTrace();
			reportException(sCommand, e.toString());
		}
		return "";
	}

	public String effecta(String sCommand, String sTarget) {
		int sTargetFlag = 0;
		String sText = "";
		String sValue = "";
		boolean bFlag = false;
		try {
			if (sTargetFlag == 1) {
			
				switch (sCommand) {
				case "click":	
					webElement(sTarget).click();
					reportStepDetails("Clicking button/link/image",
							"Successfully clicked the button/link/image", PASS);
					return "Pass";
					
				case mouseOver:		
                    Actions actionMouseOver = new Actions(driver);
                    actionMouseOver.moveToElement(webElement(sTarget)).build().perform();
					Thread.sleep(1000);
					reportStepDetails("MouseOver button/link/image",
							"Successfully MouseOver the button/link/image", PASS);
					return PASS;
				case isChecked:
					bFlag = webElement(sTarget).isSelected();
					if (bFlag)
						reportStepDetails(
								"Verifying whether checkbox/radio button is checked or not",
								"Checkbox/radio button is checked", PASS);
					else
						reportStepDetails(
								"Verifying whether checkbox/radio button is checked or not",
								"Checkbox/radio button is not checked", PASS);
					return (Boolean.toString(bFlag));
				case waitForPageToLoad:
					/*
					 * waitForPageToLoad()
					 * .setTimeToWait(Integer.parseInt(sTarget));
					 */
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,80);
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
					}
					else{
					driver.manage().timeouts()
					.pageLoadTimeout(60, TimeUnit.SECONDS);
					}
					reportStepDtlsWithScreenshot(
							"Waiting for a new page to load",
							"Execution waited for new page to get loaded", PASS);
					return PASS;
				case openURL:
					driver.get(sTarget);
					reportStepDetails("Opening URL", "Successfully loaded URL:"
							+ encodeSpecialCharacters(sTarget), PASS);
					return PASS;
				case openURLAfterDeleteCookies:
					//driver.get(sTarget);
					driver.manage().deleteAllCookies();
					Thread.sleep(1000);
					driver.manage().deleteCookieNamed("JSESSIONID");
					Thread.sleep(1000);
					driver.manage().deleteAllCookies();
					Thread.sleep(1000);
					driver.get(sTarget);
					reportStepDetails("Opening URL", "Successfully loaded URL:"
							+ encodeSpecialCharacters(sTarget), PASS);
					return PASS;
				case eOpen:
					sValue = getExcelData(sTarget);
					driver.get(sValue);
					reportStepDetails("Opening URL", "Successfully loaded URL:"
							+ encodeSpecialCharacters(sValue), PASS);
					return sValue;
				case assertTitle:
					sText = driver.getTitle().trim();
					if (sText.equals(sTarget)) {
						reportStepDetails("Expected page title : "
								+ encodeSpecialCharacters(sTarget),
								"Successfully validated page title:  "
										+ encodeSpecialCharacters(sText), PASS);
					} else {						
						reportStepDtlsWithScreenshot("Expected page title : "
								+ encodeSpecialCharacters(sTarget),
								"Displayed page title :  "
										+ encodeSpecialCharacters(sText), FAIL);

					}
					return sText;
				case verifyTitle:
					sValue = getExcelData(sTarget);
					sText = driver.getTitle().trim();
					if (sText.equals(sValue)) {
						reportStepDetails(sTarget + " Expected page title : "
								+ encodeSpecialCharacters(sValue),
								"Successfully validated page title:  "
										+ encodeSpecialCharacters(sText), PASS);
					} else {						
						reportStepDtlsWithScreenshotWithoutExit(sTarget
								+ " Expected page title : "
								+ encodeSpecialCharacters(sValue),
								"Displayed page title :  "
										+ encodeSpecialCharacters(sText), FAIL);

					}
					return sText;

				case assertConfirmation:
					if (isAlertPresent()) {
						sText = driver.switchTo().alert().getText();
						driver.switchTo().alert().accept();
						if (sText.equals(sTarget)) {
							reportStepDetails("Expected confirmation message: "
									+ encodeSpecialCharacters(sTarget),
									"Successfully validated confirmation message:  "
											+ encodeSpecialCharacters(sText),
											PASS);
						} else {							
							reportStepDtlsWithScreenshot(
									"Expected confirmation message: "
											+ encodeSpecialCharacters(sTarget),
											"Displayed confirmation message:  "
													+ encodeSpecialCharacters(sText),
													FAIL);
						}
					} else {
						reportStepDetails("Verifying confirmation message",
								"There is no confirmation message", FAIL);
					}
					return sText;
				case verifyConfirmation:
					if (isAlertPresent()) {
						sText = driver.switchTo().alert().getText();
						driver.switchTo().alert().accept();
						sValue = getExcelData(sTarget);
						if (sText.equals(sValue)) {
							reportStepDetails(sTarget
									+ " Expected confirmation message: "
									+ encodeSpecialCharacters(sValue),
									"Successfully validated confirmation message:  "
											+ encodeSpecialCharacters(sText),
											PASS);
						} else {							
							reportStepDtlsWithScreenshotWithoutExit(sTarget
									+ " Expected confirmation message: "
									+ encodeSpecialCharacters(sValue),
									"Displayed confirmation message:  "
											+ encodeSpecialCharacters(sText),
											FAIL);
						}
					} else {
						reportStepDetails(sTarget
								+ " Verifying confirmation message",
								"There is no confirmation message", FAIL);
					}
					return sText;
				case windowMaximize:
					driver.manage().window().maximize();
					reportStepDetails("Maximizing window",
							"Successfully maximized window", PASS);
					return PASS;
				case typeEmpty:
					webElement(sTarget).clear();
					reportStepDetails("Clearing an input text field",
							"Successfully cleared text in input field", PASS);
					return PASS;
				case eClickAndWait:
					sTarget = getExcelData(sTarget).trim();
					webElement(sTarget).click();
					sText = encodeSpecialCharacters(webElement(sTarget)
							.getText());
					// waitForPageToLoad().setTimeToWait(30000);
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,80);
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
					}
					else{
					
					driver.manage().timeouts()
					.pageLoadTimeout(60, TimeUnit.SECONDS);
					}
					
					reportStepDetails("Clicking button/link/image:" + sText,
							"Successfully clicked the button/link/image:"
									+ sText, PASS);
					return PASS;
				case verifyElementPresent:
					// sText = webElement(sTarget).getText();				
					reportStepDetails("Verifying element existence",
							"Element exists.", PASS);
					return PASS;
				case isElementPresent:
					sText = Boolean.toString(isElementPresent(sTarget));
					return sText;
				case assertAlert:
					sText = driver.switchTo().alert().getText();
					if (sText.equals(sTarget)) {
						reportStepDetails("Expected alert message: "
								+ encodeSpecialCharacters(sTarget),
								"Successfully validated alert message:  "
										+ encodeSpecialCharacters(sText), PASS);
					} else {						
						reportStepDtlsWithScreenshot("Expected alert message: "
								+ encodeSpecialCharacters(sTarget),
								"Displayed alert message: "
										+ encodeSpecialCharacters(sText), FAIL);
					}
					return sText;
				case verifyAlert:
					sValue = getExcelData(sTarget).trim();
					sText = driver.switchTo().alert().getText();
					if (sText.equals(sValue)) {
						reportStepDetails(sTarget
								+ " Expected alert message : "
								+ encodeSpecialCharacters(sValue),
								"Successfully validated alert message:  "
										+ encodeSpecialCharacters(sText), PASS);
					} else {						
						reportStepDtlsWithScreenshotWithoutExit(sTarget
								+ " Expected alert message : "
								+ encodeSpecialCharacters(sValue),
								"Displayed  alert message:  "
										+ encodeSpecialCharacters(sText), FAIL);

					}
					return sText;
				case getText:
					sText = webElement(sTarget).getText();
					return sText;
				case selectWindow:
					driver.switchTo().window(sTarget);
					// saveNewHandles(driver);
					// String windname=ifNewWindowOccursFocusOnIt(driver);
					// selenium.selectWindow(sTarget);
					reportStepDetails("Selecting the window "
							+ encodeSpecialCharacters(sTarget),
							"Successfully selected window with ID "
									+ encodeSpecialCharacters(sTarget), PASS);
					return PASS;
				case selectFrame:					
				
					if(sTarget.matches("[0-9]+")){
						int index=Integer.parseInt(sTarget);						
						driver.switchTo().frame(index);
					} else{					
						driver.switchTo().frame(sTarget);
					}					
									
					reportStepDetails("Selecting the window "
							+ encodeSpecialCharacters(sTarget),
							"Successfully selected window with ID "
									+ encodeSpecialCharacters(sTarget), PASS);
					return PASS;
					
				case waitForFrameAndSelect:
				
					//WebDriverWait wait=new WebDriverWait(driver,100);
					if(sTarget.matches("[0-9]+")){
						int index=Integer.parseInt(sTarget);						
						driver.switchTo().frame(index);
					} else{						
						//wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(sTarget));
						driver.switchTo().frame(sTarget);
					}					
									
					reportStepDetails("Selecting the window "
							+ encodeSpecialCharacters(sTarget),
							"Successfully selected window with ID "
									+ encodeSpecialCharacters(sTarget), PASS);
					return PASS;
				
				case getAttribute:
					sTarget = sTarget.substring(0, sTarget.lastIndexOf("@"));
					String atribute_name = sTarget.substring(sTarget
							.lastIndexOf("@") + 1);
					sText = webElement(sTarget).getAttribute(atribute_name);
					return sText;
				case getXpathCount:
					sText = Integer.toString(driver.findElements(
							By.xpath(sTarget)).size());
					return sText;
				case getSelectedLabel:
					Select foo = new Select(webElement(sTarget));
					sText = foo.getFirstSelectedOption().getText();					
					reportStepDetails("Retrieving selected option label",
							"Drop-down contains label:"
									+ encodeSpecialCharacters(sText), PASS);
					return sText;
				case getCookieByName:
					sText = driver.manage().getCookieNamed(sTarget).toString();
					reportStepDetails("Retrieving the value of cookie \""
							+ encodeSpecialCharacters(sTarget) + "\"",
							"Value of cookie \""
									+ encodeSpecialCharacters(sTarget) + "\":"
									+ encodeSpecialCharacters(sText), PASS);
					return sText;
				case clickAndWait:
					
				/*	WebDriverWait wait1 = new WebDriverWait(driver, 250);
					WebElement element = wait1.until(ExpectedConditions.elementToBeClickable(by(sTarget)));
					element.click();*/
					
					//webElement(sTarget).click();
					//waitForPageToLoad().setTimeToWait(30000);
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,80);
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
						webElement(sTarget).click();
					}
					else{
					driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
					webElement(sTarget).click();
					}
					/*if(sBrowser.contains("Safari")){
						Thread.sleep(30000);
					}*/
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,80);
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
					}
					else{
						driver.manage().timeouts()
						.pageLoadTimeout(250, TimeUnit.SECONDS);
					}

					reportStepDetails("Clicking link/image/button",
							"Successfully clicked link/image/button", PASS);
					return PASS;
				default:
					reportUnknowSeleniumCmdErr(sCommand);
				}
			} else {
				captureScreenShot();
				reportElementNotFound(sCommand);
			}
		} catch (Exception e) {
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
	
	
	public String effecta(String sCommand, String sTarget, String sTitle) {
		String sText, encodedTitle, sValue = "";
		int sTargetFlag = 0;
		boolean checked, flag = false;
		try {
			encodedTitle = encodeSpecialCharacters(sTitle);
			if (sCommand.equals("sendReport") || sCommand.equals("compare")
					|| sCommand.equals("openWindow")
					|| sCommand.equals("waitForPopUp")
					|| sCommand.equals("compareURLs")
					|| sCommand.equals("captureEntirePageScreenshot")
					|| sCommand.equals("waitForElementPresent")
					|| sCommand.equals("assertTextPresent")
					|| sCommand.equals("waitForTextPresent")
					|| sCommand.equals("eOpenWindow")
					|| sCommand.equals("isElementInVisible")
					|| sCommand.equals("waitUntilElementVisible")
					//|| sCommand.equals("verifyElementPresent")
					|| sCommand.equals("getAttribute")) {				
				sTargetFlag = 1;
			} else if (sTarget.contains("&&")) {
				String[] sTargetArray = sTarget.split("&&");
				for (int i = 0; i < sTargetArray.length; i++) {
					if (isElementPresent(sTargetArray[i])) {
						sTarget = sTargetArray[i];
						sTargetFlag = 1;
						break;
					}
				}
			} else if (isElementPresent(sTarget)) {
				sTargetFlag = 1;
			}
			
			if (sCommand.equals("verifyElementPresent") && sTargetFlag == 0) {
				//return (FAIL);
				//sText = webElement(sTarget).getText().trim();
				reportStepDtlsWithScreenshotWithoutExit("Verifying element existence","Element Not exists : "+encodedTitle, FAIL);
				return sTitle;
				
			}

			if (sTargetFlag == 1) {
				enumCommand = checkEnumConstant(sCommand);
				switch (enumCommand) {
				case verifyElementPresent:
					// sText = webElement(sTarget).getText();				
					reportStepDetails("Verifying element existence","Element exists : "+encodedTitle, PASS);
					return PASS;
				
					
				/*case verifyElementPresent:
					// sText="Element is Present";
					//sValue = getExcelData(sTitle);
					sText = webElement(sTarget).getText().trim();
					sValue = sTitle.trim();
					if (isElementPresent(sTarget)) {
						if(sValue.contains(sTitle)){
						reportStepDetails(" Expected text : "
								+ encodeSpecialCharacters(sValue),
								"Successfully validated text:  "
										+ encodeSpecialCharacters(sTitle), PASS);
					} else {						
						reportStepDtlsWithScreenshotWithoutExit(" Expected text : "
								+ encodeSpecialCharacters(sValue),
								"Displayed  text :  "
										+ encodeSpecialCharacters(sTitle), FAIL);

					}
				}
					return sTitle;*/
					
/*					        try{
						      if (webElement(sTarget).isDisplayed()) {
						            sText = webElement(sTarget).getText();
						            reportStepDetails("Element: "+ encodedTitle, "Element is Available"
		                                    , PASS);
						      }
						     }
						     catch(Exception e){
						    	 e.printStackTrace();
						    	 //sText = "Element is Not Present";
						            reportStepDtlsWithScreenshotWithoutExit ("Element :" + encodedTitle,
					                        "Element is not available"
					                                    , FAIL);
						     }
						      return sText;
				*/

				case assertValue:
					sText = webElement(sTarget).getAttribute("value").trim();
					sTitle = sTitle.trim();
					if (sText.equals(sTitle)) {
						reportStepDetails("Expected value: " + encodedTitle,
								"Successfully validated value: "
										+ encodeSpecialCharacters(sText), PASS);
					} else {
						reportStepDtlsWithScreenshot("Expected value: "
								+ encodedTitle, "Actual value: "
										+ encodeSpecialCharacters(sText), FAIL);
					}
					return sText;
				case isElementInVisible:
					  sText="Element is Present";
					  
				     /*WebDriverWait waitIn = new WebDriverWait(driver,5);
				      waitIn.until(ExpectedConditions.invisibilityOfElementLocated(by(sTarget)));*/
					  Thread.sleep(2000); 
				     try{				    	
				      if (webElement(sTarget).isDisplayed()) {
				            sText = webElement(sTarget).getText();
				            reportStepDtlsWithScreenshotWithoutExit("Validated Element :" + encodedTitle,	sText, FAIL);
				      }	
				      else{				    	 				    	
					    	 sText = "Element is Not Present";
					            reportStepDetails("Validated Element: "+ encodedTitle, sText
					                                    , PASS);
				      }
				      
				     }
				     catch(Exception e){				    			    	
				    	 sText = "Element is Not Present";
				            reportStepDetails("Validated Element: "+ encodedTitle, sText
				                                    , PASS);
				            e.printStackTrace();
				     }				     
				      return sText;
				case eOpenWindow:
					sValue = getExcelData(sTarget);
					driver.navigate().to(sValue);
					reportStepDetails("Opening popup window " + encodedTitle,
							"Successfully opened " + encodedTitle
							+ " popup window", PASS);
					return sValue;
				case waitUntilElementVisible:
					flag = false;
					int secsToWait = Integer.parseInt(sTitle);

					for (int second = 0;; second++) {
						if (second >= secsToWait) {
							flag = false;
							break;
						}
						if (webElement(sTarget).isDisplayed()) {
							flag = true;
							break;
						}
						Thread.sleep(1000);
					}
					return Boolean.toString(flag);
				case isSpecifiedOptionChecked:
					checked = webElement(sTarget).isSelected();
					if (checked)
						reportStepDetails("Verifying whether \"" + encodedTitle
								+ "\" is selected or not", "\"" + encodedTitle
								+ "\" is selected", PASS);
					else
						reportStepDetails("Verifying whether \"" + encodedTitle
								+ "\" is selected or not", "\"" + encodedTitle
								+ "\" is not selected", PASS);
					return (Boolean.toString(checked));
				case typeEmpty:
					webElement(sTarget).clear();
					reportStepDetails("Setting value of " + encodedTitle,
							encodedTitle + " is set to : \"\"", PASS);
					return PASS;
				case type:
					// sValue = getExcelData(sTitle);
					//sValue = appendCurrentDateNTime(sTitle);
					 sValue = sTitle;
					WebElement elmnt = webElement(sTarget);
					elmnt.clear();
					elmnt.sendKeys(sValue);
					reportStepDetails("Setting value of " + encodedTitle,
							encodedTitle + " is set to value: \""
									+ encodeSpecialCharacters(sValue) + "\"",
									PASS);
					return sValue;
				case typeWithoutClear:
					// sValue = getExcelData(sTitle);
					//sValue = appendCurrentDateNTime(sTitle);
					sValue = sTitle;
					WebElement elmntc = webElement(sTarget);
					elmntc.sendKeys(sValue);
					reportStepDetails("Setting value of " + encodedTitle,
							encodedTitle + " is set to value: \""
									+ encodeSpecialCharacters(sValue) + "\"",
									PASS);
					return sValue;
				case typePassword:
					sValue = getExcelData(sTitle);
					elmnt = webElement(sTarget);
					elmnt.clear();
					String value=encryptPassword(sValue);
					elmnt.sendKeys(sValue);
					reportStepDetails("Setting value of " + encodedTitle,
							encodedTitle + " is set to : "+value, PASS);
					return sValue;
				case isChecked:
					checked = false;
					//encodedTitle = encodeSpecialCharacters(getExcelData(sTitle));
					encodedTitle = encodeSpecialCharacters(sTitle);
					checked = webElement(sTarget).isSelected();
					if (checked)
						reportStepDetails("Verifying whether \"" + encodedTitle
								+ "\" is selected or not", "\"" + encodedTitle
								+ "\" is selected", PASS);

					else
						reportStepDetails("Verifying whether \"" + encodedTitle
								+ "\" is selected or not", "\"" + encodedTitle
								+ "\" is not selected", PASS);
					return Boolean.toString(checked);
				case waitForElementPresent:
					secsToWait = Integer.parseInt(sTitle);
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,80);
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
					}
					//System.out.println("enter into driverWaitForElementPresent");
					WebDriverWait wait = new WebDriverWait(driver, secsToWait);
					WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by(sTarget)));
					if(element==null){
						reportStepDtlsWithScreenshot(
								"Checking the existence of element in :"
										+ sCommand+" command:" ,
										"Element verification \"" + sTarget
										+ "\" failed", FAIL);
					}
					return Boolean.toString(flag);
					
				case waitForElementVisible:
					secsToWait = Integer.parseInt(sTitle);
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,80);
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
					}
					//System.out.println("enter into driverWaitForElementPresent");
					WebDriverWait wait1 = new WebDriverWait(driver, secsToWait);
					WebElement element1 = wait1.until(ExpectedConditions.visibilityOfElementLocated(by(sTarget)));
					if(element1==null){
						reportStepDtlsWithScreenshot(
								"Checking the existence of element in :"
										+ sCommand+" command:" ,
										"Element verification \"" + sTarget
										+ "\" failed", FAIL);
					}
					return Boolean.toString(flag);
					
			
				case waitForValue:
					flag = false;
					secsToWait = Integer.parseInt(sTitle);
					sText = "";
					for (int second = 0;; second++) {
						if (second >= secsToWait) {
							flag = false;
							break;
						}
						sText = webElement(sTarget).getAttribute("value")
								.trim();
						if (sText.trim().length() > 0) {
							flag = true;
							break;
						}
						Thread.sleep(1000);
					}
					return sText;
					/*
					 * case waitForTextPresent: sValue = getExcelData(sTarget);
					 * int secsToWait = Integer.parseInt(sTitle);
					 * 
					 * for (int second = 0;; second++) { if (second >=
					 * secsToWait) { flag1 = false; break; } if (driver
					 * selenium.isTextPresent(sValue)) { flag1 = true; break; }
					 * 
					 * Thread.sleep(1000); } return Boolean.toString(flag1);
					 */
				case getText:
					encodedTitle = encodeSpecialCharacters(sTitle);
					sText = webElement(sTarget).getText();
					reportStepDetails("Retrieving " + encodedTitle,
							"Retrieved " + encodedTitle + ":"
									+ encodeSpecialCharacters(sText), PASS);
					return sText;
				case getAttribute:
					//encodedTitle = encodeSpecialCharacters(getExcelData(sTitle));
					encodedTitle = encodeSpecialCharacters(sTitle);
					sTarget = sTarget.substring(0, sTarget.lastIndexOf("@"));
					String atribute_type = sTarget.substring(sTarget
							.lastIndexOf("@") + 1);
					sText = webElement(sTarget).getAttribute(atribute_type);
					reportStepDetails("Retrieving " + encodedTitle,
							"Retrieved " + encodedTitle + ":"
									+ encodeSpecialCharacters(sText), PASS);
					return sText;
				case select:
					//sValue = getExcelData(sTitle);
					sValue = sTitle;
					if (sValue.startsWith("label="))
						sValue = sValue.substring(sValue.indexOf("=") + 1);
					WebElement select = webElement(sTarget);
					List<WebElement> options = select.findElements(By
							.tagName("option"));
					for (WebElement option : options) {
						if (option.getText().equals(sValue)) {
							option.click();
							break;
						}
					}
					
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,80);
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
					}
					else{
					
					driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
					}
					/*
					 * Select select = new Select(webElement(sTarget));
					 * select.selectByValue(sValue);
					 */
					reportStepDetails("Selecting an option from drop-down:"
							+ encodedTitle,
							"Successfully selected the list value: "
									+ encodeSpecialCharacters(sValue), PASS);
					return sValue;
					/*
					 * case selectSpecifiedOption: select = new
					 * Select(webElement(sTarget));
					 * select.selectByVisibleText(sTitle);
					 * reportStepDetails("Selecting an option from drop-down",
					 * "Successfully Selected the list value : " + encodedTitle,
					 * PASS); if (sTitle.startsWith("label=")) sTitle =
					 * sTitle.substring(sTitle.indexOf("=") + 1); return sTitle;
					 */
				case captureEntirePageScreenshot:					
					String screenshotFile=captureScreenShot();
					reportStepDetails(
							encodeSpecialCharacters(sTarget),
							screenshotFile, PASS);
					
					return PASS;
				
				case getXpathCount:
					//encodedTitle = encodeSpecialCharacters(getExcelData(sTitle));
					encodedTitle = encodeSpecialCharacters(sTitle);
					sText = String.valueOf(driver.findElements(
							By.xpath(sTarget)).size());
					reportStepDetails("Verifying " + encodedTitle, "Number of "
							+ encodedTitle + ": " + sText, PASS);
					return sText;
				case openWindow:
					driver.navigate().to(sTarget);
					reportStepDetails("Opening popup window " + encodedTitle,
							"Successfully opened " + encodedTitle
							+ " popup window", PASS);
					return PASS;
					/*
					 * case waitForPopUp: reportStepDetails("Waiting for " +
					 * sTarget + " popup window to appear",
					 * "Successfully waited for the popup window \"" + sTarget +
					 * "\" to appear", PASS); return sValue;
					 */
				case verifyValue:
					sValue = getExcelData(sTitle).trim();
					sText = webElement(sTarget).getAttribute("value").trim();
					if (sText.equals(sValue)) {
						reportStepDetails(encodedTitle + "  Expected value : "
								+ encodeSpecialCharacters(sValue),
								"Successfully validated value:  "
										+ encodeSpecialCharacters(sText), PASS);
						return sText;
					} else {					
						reportStepDtlsWithScreenshot(encodedTitle
								+ " Expected value : "
								+ encodeSpecialCharacters(sValue),
								"Displayed value :  "
										+ encodeSpecialCharacters(sText), FAIL);
					}
					return sText;
				case getValue:
					//encodedTitle = encodeSpecialCharacters(getExcelData(sTitle).trim());
					encodedTitle = encodeSpecialCharacters(sTitle).trim();
					sText = webElement(sTarget).getAttribute("value").trim();
					reportStepDetails("Retrieving value of input field: "
							+ encodedTitle, "Successfully retrieved value: "
									+ encodeSpecialCharacters(sText), PASS);
					return sText;
				case sendReport:
					reportStepDetails(encodeSpecialCharacters(" "+sTarget),
							encodeSpecialCharacters(sTitle), PASS);
				case verifySubText:
					sValue = getExcelData(sTitle).trim();
					sText = webElement(sTarget).getText().trim();
					if (sText.contains(sValue)) {
						reportStepDetails(" "+"Verifying text " + encodedTitle,
								"\"" + encodeSpecialCharacters(sValue)
								+ "\" text appears in \""
								+ encodeSpecialCharacters(sText) + "\"",
								PASS);
					} else {						
						reportStepDtlsWithScreenshot(" "+"Verifying text "
								+ encodedTitle, "\""
										+ encodeSpecialCharacters(sValue)
										+ "\" text doesn't appears \""
										+ encodeSpecialCharacters(sText) + "\"", FAIL);
					}
					return sText;
				case assertSubText:
					sText = webElement(sTarget).getText().trim();
					if (sText.contains(sTitle)) {
						reportStepDetails("Verifying text:\"" + encodedTitle
								+ "\"", "\"" + encodedTitle
								+ "\" text appears in \""
								+ encodeSpecialCharacters(sText) + "\"", PASS);
					} else {						
						reportStepDtlsWithScreenshot("Verifying text:\""
								+ encodedTitle + "\"", "\"" + encodedTitle
								+ "\" text doesn't appears in \""
								+ encodeSpecialCharacters(sText) + "\"", FAIL);
						stopSelenium();
					}
					return sText;
				case verifyTextPresent:
					//sValue = sTitle.trim();
					sValue = getExcelData(sTitle).trim();
					sText = webElement(sTarget).getText().trim();
					if (sText.equals(sValue)) {
						reportStepDetails(" "+"Validated: " + encodedTitle
								+ " Expected text : "
								+ encodeSpecialCharacters(sValue),
								"Displayed  text :  "
										+ encodeSpecialCharacters(sText), PASS);
					} else {						
						reportStepDtlsWithScreenshotWithoutExit(" "+"Validated: "
								+ encodedTitle + " Expected text : "
								+ encodeSpecialCharacters(sValue),
								"Displayed  text :  "
										+ encodeSpecialCharacters(sText), FAIL);
						
					}
					return sText;
				case verifyText:
					/*sValue = getExcelData(sTitle);
					sText = webElement(sTarget).getText().trim();*/
					sText = webElement(sTarget).getText().trim();
					sValue = sTitle.trim();
					if (sText.equals(sValue)) {
						reportStepDetails(" "+"Validated: " + encodedTitle
								+ " Expected text : "
								+ encodeSpecialCharacters(sValue),
								"Successfully validated text:  "
										+ encodeSpecialCharacters(sText), PASS);
					} else {						
						reportStepDtlsWithScreenshotWithoutExit(" "+"Validated: "
								+ encodedTitle + " Expected text : "
								+ encodeSpecialCharacters(sValue),
								"Displayed  text :  "
										+ encodeSpecialCharacters(sText), FAIL);

					}
					return sText;
				case assertText:
					sText = webElement(sTarget).getText().trim();
					sTitle = sTitle.trim();
					if (sText.equals(sTitle)) {
						reportStepDetails(" "+"Verification:Expected text : " + encodedTitle,
								"Successfully validated text:  "
										+ encodeSpecialCharacters(sText), PASS);
					} else {						
						reportStepDtlsWithScreenshot(" "+"Expected text : "
								+ encodedTitle, "Displayed  text :  "
										+ encodeSpecialCharacters(sText), FAIL);
						stopSelenium();
					}
					return sText;
				case assertTextPresent:
					sText = webElement(sTarget).getText().trim();
					sTitle = sTitle.trim();
					if (sText.equals(sTitle)) {
						reportStepDetails(" "+"Expected text : " + encodedTitle,
								"Successfully validated text:  "
										+ encodeSpecialCharacters(sText), PASS);
					} else {						
						reportStepDtlsWithScreenshotWithoutExit(" "+"Expected text : "
								+ encodedTitle, "Displayed  text :  "
										+ encodeSpecialCharacters(sText), FAIL);
						stopSelenium();
						//fail(sCommand);
					}
					return sText;
				case click:
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,80);
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
					}
					else{
					driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
					}
					//System.out.println("***in click****");
					if (sTitle.endsWith(".id") || sTitle.endsWith(".name")
							|| sTitle.endsWith(".link"))
						encodedTitle = encodeSpecialCharacters(sTitle
								.substring(0, sTitle.lastIndexOf(".")));
					else						
						encodedTitle = encodeSpecialCharacters(sTitle);
					//Thread.sleep(2000);
					   // executor.executeScript("arguments[0].click();", webElement(sTarget));
					    //Thread.sleep(3000);
					WebElement clickElementjs = null;    
					try{
					    	//System.out.println("Click Entered for 3 arguments");
					    	clickElementjs=webElement(sTarget);
					    	//WebElement elementjs=driver.findElement(By.)
						webElement(sTarget).click();
						//System.out.println("Click on 3 Args");
					    }
					    catch(Exception e){
					    	//System.out.println("Entered Execption Click on 3 Args");
					    	JavascriptExecutor clickJavaScript = (JavascriptExecutor)driver;
					    	clickJavaScript.executeScript("arguments[0].click();", clickElementjs);
					    }
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,80);
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
					}
					else{
					
						driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
					}
						 //Thread.sleep(2000);
					   
					
					
					reportStepDetails("Clicking button/link/image:"
							+ encodedTitle,
							"Successfully clicked button/link/image:"
									+ encodedTitle, PASS);
					return PASS;
				case clickAndWait:
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,80);
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
					}
					else{
					
					driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
					}
					/*WebDriverWait wait1 = new WebDriverWait(driver, 250);
					WebElement element1 = wait1.until(ExpectedConditions.elementToBeClickable(by(sTarget)));*/
					
					if (sTitle.endsWith(".id") || sTitle.endsWith(".name")
							|| sTitle.endsWith(".link"))
						encodedTitle = encodeSpecialCharacters(sTitle
								.substring(0, sTitle.lastIndexOf(".")));
					else
						encodedTitle = encodeSpecialCharacters(sTitle);
					//Thread.sleep(1000);
					    //executor.executeScript("arguments[0].click();", webElement(sTarget));
					    //Thread.sleep(3000);
					//element1.click();
					 webElement(sTarget).click();
					 //driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
					// driver.manage().timeouts().setScriptTimeout(100,TimeUnit.SECONDS);
					// Thread.sleep(3000);
					try{
						if(sBrowser.contains("Safari")){
							WebDriverWait safariWait=new WebDriverWait(driver,80);
							JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
							safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
						}
						else{
						driver.manage().timeouts()
						.pageLoadTimeout(60, TimeUnit.SECONDS);
						}
					}catch(TimeoutException te){
					//	reportException(sCommand, te.toString());
						reportException(sCommand, sTitle);
					}	
					catch(Exception e){
						reportException(sCommand, sTitle);
					}
					
					reportStepDetails("Clicking button/link/image:"
							+ encodedTitle,
							"Successfully clicked button/link/image:"
									+ encodedTitle, PASS);
					return PASS;
					/*
					 * case check: encodedTitle =
					 * encodeSpecialCharacters(getExcelData(sTitle));
					 * webElement(sTarget). selenium.check(sTarget);
					 * reportStepDetails("Checking a checkbox/radio button:" +
					 * encodedTitle,
					 * "Successfully checked checkbox/radio button:" +
					 * encodedTitle, PASS); return PASS;
					 */
				case clickSpecifiedLink:
					webElement(sTarget).click();
					reportStepDetails("Clicking button/link/image:"
							+ encodedTitle,
							"Successfully clicked button/link/image:"
									+ encodedTitle, PASS);
					return PASS;
				case clickSpecifiedLinkNWait:
					webElement(sTarget).click();
					// waitForPageToLoad().setTimeToWait(30000);
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,80);
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
					}
					else{
					driver.manage().timeouts()
					.pageLoadTimeout(60, TimeUnit.SECONDS);
					}
					reportStepDtlsWithScreenshot("Clicking button/link/image:"
							+ encodedTitle,
							"Successfully clicked the button/link/image:"
									+ encodedTitle, PASS);
					return PASS;
				case verifyElement:
				/*case verifyElementPresent:
					//encodedTitle = encodeSpecialCharacters(getExcelData(sTitle));
					encodedTitle = encodeSpecialCharacters(sTitle);
					reportStepDetails("Checking the existence of element:"
							+ encodedTitle, "Element verification \""
									+ encodedTitle + "\" succeeded", PASS);
					return Boolean.toString(true);*/
				case compare:
					String sTitleString = "";
					String sTargetString = "";
					sTarget = sTarget.trim();
					sTitle = sTitle.trim();
					if (sTarget.contains(":")) {
						String[] sTargetVlaue = sTarget.split(":");
						if (sTargetVlaue[1].equals("")) {
							sTarget = sTargetVlaue[0];
							sTargetString = sTarget;
						} else {
							sTarget = sTargetVlaue[1];
							sTargetString = sTargetVlaue[0] + sTarget;
						}
					} else {
						sTargetString = sTarget;
					}
					if (sTitle.contains(":")) {
						String[] sTitleVlaue = sTitle.split(":");
						if (sTitleVlaue[1].equals("")) {
							sTitle = sTitleVlaue[0];
							sTitleString = sTitle;
						} else {
							sTitle = sTitleVlaue[1];
							sTitleString = sTitleVlaue[0] + sTitle;
						}
					} else {
						sTitleString = sTitle;
					}
					sTarget = sTarget.trim();
					sTitle = sTitle.trim();
					if (sTarget.equals(sTitle)) {
						reportStepDetails(
								encodeSpecialCharacters(sTargetString),
								encodeSpecialCharacters(sTitleString), PASS);
						return PASS;
					} else {

						reportStepDtlsWithScreenshot(
								encodeSpecialCharacters(sTargetString),
								encodeSpecialCharacters(sTitleString), FAIL);
						return FAIL;

					}
				case compareURLs:
					String sTitleString1 = "";
					String sTargetString1 = "";
					sTarget = sTarget.trim();
					sTitle = sTitle.trim();
					if (sTarget.contains("::")) {
						String[] sTargetVlaue = sTarget.split("::");
						if (sTargetVlaue[1].equals("")) {
							sTarget = sTargetVlaue[0];
							sTargetString1 = sTarget;
						} else {
							sTarget = sTargetVlaue[1];
							sTargetString1 = sTargetVlaue[0] + ": " + sTarget;
						}
					} else {
						sTargetString1 = sTarget;
					}
					if (sTitle.contains("::")) {
						String[] sTitleVlaue = sTitle.split("::");
						if (sTitleVlaue[1].equals("")) {
							sTitle = sTitleVlaue[0];
							sTitleString1 = sTitle;
						} else {
							sTitle = sTitleVlaue[1];
							sTitleString1 = sTitleVlaue[0] + ": " + sTitle;
						}
					} else {
						sTitleString1 = sTitle;
					}
					sTarget = sTarget.trim();
					sTitle = sTitle.trim();
					if (sTarget.equals(sTitle)) {
						reportStepDetails(
								encodeSpecialCharacters(sTargetString1),
								encodeSpecialCharacters(sTitleString1), PASS);
						return PASS;
					} else {
						reportStepDtlsWithScreenshot(
								encodeSpecialCharacters(sTargetString1),
								encodeSpecialCharacters(sTitleString1), FAIL);
						return FAIL;
					}

				default:
					reportUnknowSeleniumCmdErr(sCommand);
				}
			} else {
				if (sCommand.equals("verifyElement")) {
					//encodedTitle = encodeSpecialCharacters(getExcelData(sTitle));
					encodedTitle = encodeSpecialCharacters(sTitle);
					reportStepDtlsWithScreenshot(
							"Checking the existence of element:" + encodedTitle,
							"Element verification \"" + encodedTitle
							+ "\" failed", FAIL);
					return Boolean.toString(false);
				} else if (sCommand.equals("Present")) {					
					encodedTitle = encodeSpecialCharacters(sTitle);
					reportStepDtlsWithScreenshot(
							"Checking the existence of element:" + encodedTitle,
							"Element verification \"" + encodedTitle
							+ "\" failed", FAIL);					
				} else {
					captureScreenShot();
					reportElementNotFound(sCommand);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(sCommand.contains("waitFor")){
				reportException(sCommand, "Expected");
			}else{
				reportException(sCommand, sTitle);
			}
		}

		return "";
	}


	
	public String method(String sCommand,String sLocator,String sDescription){
		
		String value;
		try{
			if(getWebElement(sLocator)!=null){
				switch(sCommand){
					case "click":
						getWebElement(sLocator).click();
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
					case "isChecked":
						boolean checked = false;
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
	}
	
	public String effecta(String sCommand, String sTarget, String sTitle,
			String sResult) {
		String sText = "";
		String encodedTitle = "";
		int sTargetFlag = 0;
		boolean flag = false;
		try {
			if (sCommand.equals("sendReport")
					|| sCommand.equals("sendReportWithOutExit")
					|| sCommand.equals("captureEntirePageScreenshot")
					|| sCommand.equals("assertTextPresent")					
					|| sCommand.equals("waitForElementPresent")) {
				sTargetFlag = 1;
			} else if (sTarget.contains("&&")) {
				String[] sTargetArray = sTarget.split("&&");
				for (int i = 0; i < sTargetArray.length; i++) {
					if (isElementPresent(sTargetArray[i])) {
						sTarget = sTargetArray[i];
						sTargetFlag = 1;
						break;
					}
				}
			} else if (isElementPresent(sTarget)) {
				sTargetFlag = 1;
			}

			if (sTargetFlag == 1) {
				enumCommand = checkEnumConstant(sCommand);
				switch (enumCommand) {
				case type:
					String sValue = sTitle;
					//System.out.println("*********svalue***********:"+sValue);
					WebElement elmnt1 = webElement(sTarget);
					elmnt1.clear();
					elmnt1.sendKeys(sValue);
					reportStepDetails("Setting value of " + sResult,
							sResult + " is set to value: \""
									+ encodeSpecialCharacters(sValue) + "\"",
									PASS);
					return sValue;
				case typePassword:
					sValue = sTitle;
					WebElement eleObj = webElement(sTarget);
					eleObj.clear();
					eleObj.sendKeys(sValue);
					
					String value=encryptPassword(sValue);
				
					reportStepDetails("Setting value of " + sResult,
							sResult + " is set to : "+value, PASS);
					
					
					
					return sValue;
				case select:
					sValue = sTitle;
					if (sValue.startsWith("label="))
						sValue = sValue.substring(sValue.indexOf("=") + 1);
					WebElement select = webElement(sTarget);
					List<WebElement> options = select.findElements(By
							.tagName("option"));
					//System.out.println("legnthttth"+options.size());
					for (WebElement option : options) {
						
						if (option.getText().equals(sValue)) {
							option.click();
							break;
						}
					}
					
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,80);
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
					}
					else{
					driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
					}
					/*
					 * Select select = new Select(webElement(sTarget));
					 * select.selectByValue(sValue);
					 */
					reportStepDetails("Selecting an option from drop-down:"
							+ sResult,
							"Successfully selected the list value : "
									+ encodeSpecialCharacters(sValue), PASS);
					return sValue;
					
				case selectAndWait:
					sValue = sTitle;
					if (sValue.startsWith("label="))
						sValue = sValue.substring(sValue.indexOf("=") + 1);
					WebElement select1 = webElement(sTarget);
					List<WebElement> options1 = select1.findElements(By
							.tagName("option"));
					//System.out.println("legnthttth"+options.size());
					for (WebElement option : options1) {
						if (option.getText().equals(sValue)) {
							option.click();
							break;
						}
					}
					/*
					 * Select select = new Select(webElement(sTarget));
					 * select.selectByValue(sValue);
					 */
					reportStepDetails("Selecting an option from drop-down:"
							+ sResult,
							"Successfully selected the list value : "
									+ encodeSpecialCharacters(sValue), PASS);
					return sValue;


				case sendReport:
					if (sResult.equalsIgnoreCase(FAIL)) {						
						reportStepDtlsWithScreenshot(
								encodeSpecialCharacters(" "+sTarget),
								encodeSpecialCharacters(sTitle), sResult);	
						//stopSelenium();
					} else {
						reportStepDetails(encodeSpecialCharacters(" "+sTarget),
								encodeSpecialCharacters(sTitle), sResult);
					}
					return sResult;
				case captureEntirePageScreenshot:

					reportStepDtlsWithScreenshot(
							encodeSpecialCharacters(sTarget),
							encodeSpecialCharacters(sTitle), sResult);
					return PASS;
				case clickAndWait:
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,80);
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
					}
					else{
					
					driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
					}
				/*	WebDriverWait wait1 = new WebDriverWait(driver, 250);
					WebElement element1 = wait1.until(ExpectedConditions.elementToBeClickable(by(sTarget)));*/
					
					encodedTitle = encodeSpecialCharacters(sTitle);
					//element1.click();
					webElement(sTarget).click();
					//driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
					/*if(sBrowser.contains("Safari")){
						Thread.sleep(Integer.parseInt(sResult));
					}*/
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,Integer.parseInt(sResult));
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
					}
					else{
						
						try{					
							driver.manage().timeouts()
							.pageLoadTimeout(250, TimeUnit.SECONDS);
						}catch(TimeoutException te){
							reportException(sCommand, sTitle);
						}	
						catch(Exception e){
							reportException(sCommand, sTitle);
						}
					}					
					reportStepDetails("Clicking button/link/image:"
							+ encodedTitle,
							"Successfully clicked button/link/image:"
									+ encodedTitle, PASS);

					return PASS;
				case clickAndWaitForElementPresent:					
					encodedTitle = encodeSpecialCharacters(sTitle);
					webElement(sTarget).click();
					if(sBrowser.contains("Safari")){
						WebDriverWait safariWait=new WebDriverWait(driver,80);
						JavascriptExecutor safariWaitJavaScript = (JavascriptExecutor)driver;
						safariWait.equals(safariWaitJavaScript.executeScript("return document.readyState").equals("complete"));
					}
				WebDriverWait wait = new WebDriverWait(driver, 500);
					try{
					wait.until(ExpectedConditions.visibilityOfElementLocated(by(sResult)));	
					}catch(TimeoutException te){
						reportException(sCommand, te.toString());
						//reportException(sCommand, sTitle);
					}	
					catch(Exception e){
						reportException(sCommand, sTitle);
					}
					reportStepDetails("Clicking button/link/image:"
							+ encodedTitle,
							"Successfully clicked button/link/image:"
									+ encodedTitle, PASS);

					return PASS;

					
					
					
				case sendReportWithOutExit:
					if (sResult.equalsIgnoreCase(FAIL)) {						
						reportStepDtlsWithScreenshotWithoutExit(
								//reportStepDtlsWithScreenshot(
								encodeSpecialCharacters(" "+sTarget),
								encodeSpecialCharacters(sTitle), FAIL);
					} else {
						reportStepDetails(encodeSpecialCharacters(" "+sTarget),
								encodeSpecialCharacters(sTitle), PASS);
					}
					return sResult;
					
					
				case waitForElementPresent:
					flag = false;
					int secsToWait = Integer.parseInt(sTitle);

					for (int second = 0;; second++) {
						if (second >= secsToWait) {
							flag = false;
							break;
						}
						if (isElementPresent(sTarget)) {
							flag = true;
							break;
						}
						Thread.sleep(1000);
					}
					if (!flag) {						
						encodedTitle = encodeSpecialCharacters(sResult);					
						reportStepDtlsWithScreenshot(
								"Checking the existence of element:"
										+ encodedTitle,
										"Element verification \"" + encodedTitle
										+ "\" failed", FAIL);
					}
					return Boolean.toString(flag);
					
				case verifyText:
					/*sValue = getExcelData(sTitle);
					sText = webElement(sTarget).getText().trim();*/
					sText = webElement(sTarget).getText().trim();
					sTitle = sTitle.trim();
					encodedTitle = encodeSpecialCharacters(sResult);
					if (sText.equalsIgnoreCase(sTitle)) {
						reportStepDetails(" "+"Expected " + encodedTitle + ": "
								+ encodeSpecialCharacters(sTitle),
								"Successfully validated " + encodedTitle
								+ ":  "
								+ encodeSpecialCharacters(sText), PASS);
					} else {						
						reportStepDtlsWithScreenshotWithoutExit(" "+"Expected " + encodedTitle
								+ ": " + encodeSpecialCharacters(sTitle),
								"Displayed " + encodedTitle + ":  "
										+ encodeSpecialCharacters(sText), FAIL);				
					}
					return sText;
				case assertText:

					sText = webElement(sTarget).getText().trim();

					sTitle = sTitle.trim();
					//encodedTitle = encodeSpecialCharacters(getExcelData(sResult));
					encodedTitle = encodeSpecialCharacters(sResult);
					if (sText.equalsIgnoreCase(sTitle)) {
						reportStepDetails(" "+"Expected " + encodedTitle + ": "
								+ encodeSpecialCharacters(sTitle),
								"Successfully validated " + encodedTitle
								+ ":  "
								+ encodeSpecialCharacters(sText), PASS);
					} else {						
						reportStepDtlsWithScreenshot(" "+"Expected " + encodedTitle
								+ ": " + encodeSpecialCharacters(sTitle),
								"Displayed " + encodedTitle + ":  "
										+ encodeSpecialCharacters(sText), FAIL);
						//stopSelenium();
					}

					return sText;
				
				case assertValue:
					sText = webElement(sTarget).getAttribute("value").trim();
					sTitle = sTitle.trim();
					//encodedTitle = encodeSpecialCharacters(getExcelData(sResult));
					encodedTitle = encodeSpecialCharacters(sResult);
					if (sText.equalsIgnoreCase(sTitle)) {
						reportStepDetails("Expected " + encodedTitle + ": "
								+ encodeSpecialCharacters(sTitle),
								"Successfully validated " + encodedTitle + ":"
										+ encodeSpecialCharacters(sText), PASS);
					} else {						
						reportStepDtlsWithScreenshot("Expected " + encodedTitle
								+ ": " + encodeSpecialCharacters(sTitle),
								"Displayed " + encodedTitle + ":  "
										+ encodeSpecialCharacters(sText), FAIL);
					}
					return sText;
				case typeSpecifiedText:
					encodedTitle = encodeSpecialCharacters(sResult);
					WebElement elmnt = webElement(sTarget);
					elmnt.clear();
					//System.out.println("sTitlesTitle"+sTitle);
					elmnt.sendKeys(sTitle);
					reportStepDetails("Setting value of " + encodedTitle,
							encodedTitle + " is set to value: \""
									+ encodeSpecialCharacters(sTitle) + "\"",
									PASS);
					return sTitle;
				case assertTextPresent:
					System.out.println("***********assertTextPresent*********");
					sText = webElement(sTarget).getText().trim();
					sTitle = sTitle.trim();
					//encodedTitle = encodeSpecialCharacters(getExcelData(sResult));
					encodedTitle = encodeSpecialCharacters(sResult);
					if (sText.equals(sTitle)) {
						reportStepDetails(" "+"Expected " + encodedTitle + ": "
								+ encodeSpecialCharacters(sTitle),
								"Successfully validated " + encodedTitle
								+ ":  "
								+ encodeSpecialCharacters(sText), PASS);
					} else {						
						reportStepDtlsWithScreenshotWithoutExit(" "+"Expected " + encodedTitle
								+ ": " + encodeSpecialCharacters(sTitle),
								"Displayed " + encodedTitle + ":  "
										+ encodeSpecialCharacters(sText), FAIL);
						//stopSelenium();
						//fail(sCommand);
					}
					return sText;
				case assertSubText:
					sText = webElement(sTarget).getText().trim();
					//encodedTitle = encodeSpecialCharacters(getExcelData(sResult));
					encodedTitle = encodeSpecialCharacters(sResult);
					if (sText.contains(sTitle)) {
						reportStepDetails("Verifying " + encodedTitle + ":\""
								+ encodeSpecialCharacters(sTitle) + "\"", "\""
										+ encodeSpecialCharacters(sTitle)
										+ "\" text appears in \""
										+ encodeSpecialCharacters(sText) + "\"", PASS);
					} else {						
						reportStepDtlsWithScreenshot("Verifying "
								+ encodedTitle + ":\""
								+ encodeSpecialCharacters(sTitle) + "\"", "\""
										+ encodeSpecialCharacters(sTitle)
										+ "\" text doesn't appears in \""
										+ encodeSpecialCharacters(sText) + "\"", FAIL);
					}
					return sText;
				default:
					reportUnknowSeleniumCmdErr(sCommand);
				}
			} else {
				captureScreenShot();
				reportElementNotFound(sCommand);
			}

		} catch (Exception e) {

			e.printStackTrace();
			//System.out.println("Enter into exception block");
			captureScreenShot();
			reportException(sCommand, sTitle);
		}
		return "";
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

