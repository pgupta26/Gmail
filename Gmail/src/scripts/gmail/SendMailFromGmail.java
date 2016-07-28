package scripts.gmail;

import java.util.concurrent.TimeUnit;

import org.testng.annotations.*;

import com.arsin.ArsinSeleniumAPI;

import classes.GmailHomePage;
import classes.GmailLoginPage;

public class SendMailFromGmail {
	
	ArsinSeleniumAPI oASelFW = null;
	@Parameters({"prjName", "testEnvironment","instanceName","sauceUser","moduleName","testSetName"})
	@BeforeClass
	public void oneTimeSetUp(String prjName,String testEnvironment,String instanceName,String sauceUser,String moduleName,String testSetName) throws InterruptedException {
		String[] environment=new ArsinSeleniumAPI().getEnvironment(testEnvironment,this.getClass().getName());
		String os=environment[0];String browser=environment[1];String testCasename=this.getClass().getSimpleName();
		oASelFW = new ArsinSeleniumAPI(prjName,testCasename,browser,os,instanceName,sauceUser,moduleName,testSetName);		
		oASelFW.startSelenium("https://accounts.google.com/ServiceLogin?service=mail&passive=true&rm=false&continue=https://mail.google.com/mail/&ss=1&scc=1&ltmpl=default&ltmplcache=2&emr=1&osid=1");
	}
	
	@Test
	public void Test(){
		
			String email="mcaparshant@gmail.com";
			String password="test123";
			String toAddress="parshant.gupta00@gmail.com";
			String subject="Testing mail";
			String body="Ignore this.";
		
		try{
			
			GmailHomePage homePge=new GmailHomePage(oASelFW);
			GmailLoginPage lgPge=new GmailLoginPage(oASelFW);
			
			//click on add account
			lgPge.click_AddAccount();
			
			//type email address
			lgPge.type_UserName(email);
			
			//click next
			lgPge.click_Next();
			
			//type password
			lgPge.type_Password(password);
			
			//click sign in
			lgPge.click_SignIn();
			
			Thread.sleep(7000);
			
			//click compose
			homePge.click_Compose();
			
			//type To Address
			homePge.type_EmailToAddress(toAddress);
			
			//type subject
			homePge.type_EmailSubject(subject);
			
			//type body
			homePge.type_Body(body);
			
			//click send
			homePge.click_SendBtn();
			
			TimeUnit.MINUTES.sleep(1);
			
			//click sign out
			homePge.click_SignOut(email);
			
		}
		
		catch(Throwable e){
			e.printStackTrace();	
		}
		
	}

	@AfterClass(enabled=true)
	public void oneTearDown()
	{
		oASelFW.stopSelenium();
	}
}
