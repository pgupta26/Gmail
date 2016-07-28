package scripts.shopclues;

import org.testng.annotations.*;

import classes.HomePage;

import com.arsin.ArsinSeleniumAPI;

public class verifyUserRegisteredSuccessfully {
	
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
	public void test() throws Exception{
		
		long random=(long) (Math.random()*10000);
		
		String siteName="shopclues";
		String firstName="qa";
		String lastName="test"+random;
		String email=firstName+lastName+"@gmail.com";
		String password="test123";
		String confirmPassword="test123";
		String addressLine="abc";
		String pincode="133001";
	
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
			
			//type first name
			home.type_UserDetails("First Name ", firstName);
			home.type_UserDetails("Last Name ", lastName);
			home.type_UserDetails("e-Mail ID", email);
			home.select_Gender("m");
			home.type_UserDetails("Password ", password);
			home.type_UserDetails("Confirm Password ", confirmPassword);
			home.type_UserDetails("Address Line 1", addressLine);
			home.type_UserDetails("Pincode ", pincode);
			
			//click create account
			home.click_CreateAccount();
			
			//verify success message
			home.verify_AccountCreatedSuccessfully("The account has been created successfully.");
			
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
