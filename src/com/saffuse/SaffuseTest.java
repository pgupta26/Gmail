package com.saffuse;

import java.io.IOException;

import org.testng.annotations.*;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SaffuseTest {
	
	SaffuseFrameworkAPI oASelFW = null;
	@Parameters({"prjName", "testEnvironment","instanceName","sauceUser","moduleName","proxy"})
	@BeforeClass
	public void oneTimeSetUp(String prjName,String testEnvironment,String instanceName,String sauceUser,String moduleName,String proxy) throws InterruptedException, IOException {
		oASelFW = new SaffuseFrameworkAPI(prjName,testEnvironment,instanceName,sauceUser,moduleName,proxy);		
		oASelFW.startSelenium("http://toolsqa.com/selenium-webdriver/how-to-use-geckodriver/");
	}
	
	@Test
	public void test(){
			
		try{
			
			ToolsQAHomePage hmpg=new ToolsQAHomePage(oASelFW);
			
			//click on home page link
			//hmpg.clickNavigationLinks("Selenium-Webdriver");
			System.out.println(oASelFW.method("getTitle"));
			
			
		}
		
		
		
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@AfterClass
	public void stopSelenium(){
		oASelFW.stopSelenium();
	}
}
