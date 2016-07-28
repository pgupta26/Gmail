package scripts.shopclues;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import classes.HomePage;

import com.arsin.ArsinSeleniumAPI;

public class verifyUserIsAbleToLogin {
	
	ArsinSeleniumAPI oASelFW = null;
	@Parameters({"prjName", "testEnvironment","instanceName","sauceUser","moduleName","testSetName"})
	@BeforeClass
	public void oneTimeSetUp(String prjName,String testEnvironment,String instanceName,String sauceUser,String moduleName,String testSetName) throws InterruptedException {
		String[] environment=new ArsinSeleniumAPI().getEnvironment(testEnvironment,this.getClass().getName());
		String os=environment[0];String browser=environment[1];String testCasename=this.getClass().getSimpleName();
		oASelFW = new ArsinSeleniumAPI(prjName,testCasename,browser,os,instanceName,sauceUser,moduleName,testSetName);
		oASelFW.startSelenium("https://www.google.co.in/");	
	}
	
	@Test
	public void Test() throws Exception{
		
		String siteName="shopclues";
		String firstName="qa";
		String email="qatest270@gmail.com";
		String password="test123";
		
		//create class objects
		HomePage home=new HomePage(oASelFW);
		
		try{
			
			//enter value into google search bar
			home.type_IntoGoogleSearchBar(siteName);
			
			//click shopclues site link
			home.click_AnchorLink("ShopClues");
			
			Thread.sleep(3000);
			
			//close banner
			home.click_ClosePinCodePopup();
		
			Thread.sleep(3000);
			
			//close banner
			home.click_CloseRegisterPopup();
			
			//click on sign in
			home.click_SignIn();
			
			Thread.sleep(3000);
			
			//close banner
			home.click_ClosePinCodePopup();
			
			//type email id
			home.type_UserDetails("Login ID", email);
			
			//type password
			home.type_UserDetails("Password", password);
			
			//click login
			home.click_Login();
			
			//verify user name
			home.verify_UserName(firstName);
			
			//click on sign out
			home.click_SignOut();
			
			
		}
		
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	@AfterClass(enabled=true)
	public void oneTearDown(){
		
		oASelFW.stopSelenium();
		
	}

}
