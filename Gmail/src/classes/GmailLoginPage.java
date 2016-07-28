package classes;

import com.arsin.ArsinSeleniumAPI;

public class GmailLoginPage {
	
	ArsinSeleniumAPI oASelFW;
	
	public GmailLoginPage(ArsinSeleniumAPI oASelFW){
		
		this.oASelFW=oASelFW;
		
	}
	
	/**
	* @summary Method to click on AddAccount button.
	* @author Parshant G.
	* @param 
	* @return 
	* @exception InterruptedException
	*/
	public void click_AddAccount() throws InterruptedException{
		
		if(Boolean.parseBoolean(oASelFW.effecta("isElementPresent","id=account-chooser-add-account")))
		{
		oASelFW.effecta("waitForElementPresent","id=account-chooser-add-account","90");
		oASelFW.effecta("click","id=account-chooser-add-account","Add Account");
		}
	}
	
	/**
	* @summary Method to enter username.
	* @author Parshant G.
	* @param emailaddress as string
	* @return 
	* @exception InterruptedException
	*/
	public void type_UserName(String email) throws InterruptedException{
		
		oASelFW.effecta("waitForElementPresent","id=Email","30");
		oASelFW.effecta("type","id=Email",email,"Email");
	}
	
	/**
	* @summary Method to click on next button.
	* @author Parshant G.
	* @param 
	* @return 
	* @exception InterruptedException
	*/
	public void click_Next() throws InterruptedException{
		
		oASelFW.effecta("waitForElementPresent","id=next","30");
		oASelFW.effecta("click","id=next","Next");
		
	}

	/**
	* @summary Method to enter password.
	* @author Parshant G.
	* @param password as string
	* @return 
	* @exception InterruptedException
	*/
	public void type_Password(String password) throws InterruptedException{
		
		oASelFW.effecta("waitForElementPresent","id=Passwd","30");
		oASelFW.effecta("type","id=Passwd",password,"Password");
	}

	/**
	* @summary Method to click on AddAccount button.
	* @author Parshant G.
	* @param 
	* @return 
	* @exception InterruptedException
	*/
	public void click_SignIn() throws InterruptedException{
	
	oASelFW.effecta("waitForElementPresent","id=signIn","30");
	oASelFW.effecta("click","id=signIn","Sign In");	
	}
}
