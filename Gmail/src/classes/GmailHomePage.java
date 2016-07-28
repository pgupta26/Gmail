package classes;

import com.arsin.ArsinSeleniumAPI;

public class GmailHomePage {
	
	ArsinSeleniumAPI oASelFW;
	
	public GmailHomePage(ArsinSeleniumAPI oASelFW){
		this.oASelFW=oASelFW;
	}

	/**
	* @summary Method to click on compose button.
	* @author Parshant G.
	* @param 
	* @return 
	* @exception InterruptedException
	*/
    public void click_Compose() throws InterruptedException{
		
		oASelFW.effecta("waitForElementPresent","//div[text()='COMPOSE']","30");
		oASelFW.effecta("click","//div[text()='COMPOSE']","Compose");
	}
    
    /**
	* @summary Method to enter recipient mail address.
	* @author Parshant G.
	* @param recipient mail address as string
	* @return 
	* @exception InterruptedException
	*/
    public void type_EmailToAddress(String toAddress) throws InterruptedException{
    	
    	oASelFW.effecta("waitForElementPresent","//textarea[@aria-label='To']","30");
		oASelFW.effecta("type","//textarea[@aria-label='To']",toAddress,"To Address");
    }
    
    /**
	* @summary Method to enter email subject.
	* @author Parshant G.
	* @param email subject as string
	* @return 
	* @exception InterruptedException
	*/
    public void type_EmailSubject(String subject) throws InterruptedException{
    	
    	oASelFW.effecta("waitForElementPresent","//input[@name='subjectbox']","30");
		oASelFW.effecta("type","//input[@name='subjectbox']",subject,"To Address");
    }
 
    /**
	* @summary Method to enter body of the message.
	* @author Parshant G.
	* @param  email body as string
	* @return 
	* @exception InterruptedException
	*/
    public void type_Body(String body) throws InterruptedException{
 	
 	    oASelFW.effecta("waitForElementPresent","//div[@aria-label='Message Body']","30");
		oASelFW.effecta("type","//div[@aria-label='Message Body']",body,"To Address");
    }
    
    /**
	* @summary Method to click on send button.
	* @author Parshant G.
	* @param 
	* @return 
	* @exception InterruptedException
	*/
    public void click_SendBtn() throws InterruptedException{
    	
    	oASelFW.effecta("waitForElementPresent","//div[contains(@aria-label,'Send')]","30");
		oASelFW.effecta("click","//div[contains(@aria-label,'Send')]","Send");
		
    }
 
    /**
	* @summary Method to click on signout button.
	* @author Parshant G.
	* @param email address as string
	* @return 
	* @exception InterruptedException
	*/
    public void click_SignOut(String email) throws InterruptedException
    {
    	oASelFW.effecta("waitForElementPresent","//a[contains(@title,'"+email+"')]","30");
		oASelFW.effecta("clickAndWait","//a[contains(@title,'"+email+"')]","Sign out");
		oASelFW.effecta("click","//a[text()='Sign out']","Sign out");

    }
}
