package classes;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.arsin.ArsinSeleniumAPI;

public class HomePage {

	ArsinSeleniumAPI oASelFW;

	public HomePage(ArsinSeleniumAPI oASelFW) throws InterruptedException{
		this.oASelFW=oASelFW;
	}

	/**
	* @summary Method to enter text in google search bar.
	* @author Parshant G.
	* @param text you want to search
	* @return 
	* @exception InterruptedException
	*/
	public void type_IntoGoogleSearchBar(String text) throws InterruptedException{

		oASelFW.effecta("waitForElementPresent","//input[@id='lst-ib']","30");
		oASelFW.effecta("type","//input[@id='lst-ib']",text,text);

		oASelFW.driver.findElement(By.xpath("//input[@id='lst-ib']")).sendKeys(Keys.ENTER);

	}

	/**
	* @summary Method to click on link of text you search.
	* @author Parshant G.
	* @param  linkName as string
	* @return 
	* @exception InterruptedException
	*/
	public void click_AnchorLink(String linkName) throws InterruptedException{

		oASelFW.effecta("waitForElementPresent","//a[text()='"+linkName+"']","30");
		oASelFW.effecta("click","//a[text()='"+linkName+"']",linkName);

	}

	/**
	* @summary Method to click on closePinCodePopup in shopclues site.
	* @author Parshant G.
	* @param 
	* @return 
	* @exception InterruptedException
	*/
	public void click_ClosePinCodePopup() throws InterruptedException{
		
		if(oASelFW.driver.findElement(By.xpath("//a[@class='cross-pincode_popup']")).isDisplayed()){
			

			oASelFW.effecta("click","//a[@class='cross-pincode_popup']","close popup");
		}

	}

	/**
	* @summary Method to click on close register popup.
	* @author Parshant G.
	* @param 
	* @return 
	* @exception InterruptedException
	*/
	public void click_CloseRegisterPopup() throws InterruptedException{
		
		if(oASelFW.driver.findElement(By.xpath("//a[@id='popup_close']")).isDisplayed()){
			

			oASelFW.effecta("click","//a[@id='popup_close']","close popup");
		}
	}

	/**
	* @summary Method to click on signin button.
	* @author Parshant G.
	* @param 
	* @return 
	* @exception InterruptedException
	*/
	public void click_SignIn() throws InterruptedException{

		oASelFW.effecta("waitForElementPresent","//div[@id='login_user_data']","30");
		WebElement element=oASelFW.driver.findElement(By.xpath("//div[@id='login_user_data']"));

		Actions ac=new Actions(oASelFW.driver);
		ac.moveToElement(element).click().build().perform();
	}

	/**
	* @summary Method to enter user details during registration on shopclues.
	* @author Parshant G.
	* @param label(field you want to enter) as string, value as string
	* @return 
	* @exception InterruptedException
	*/
	public void type_UserDetails(String label,String value) throws InterruptedException{

		oASelFW.effecta("waitForElementPresent","//label[text()='"+label+"']/following::input[1]","30");
		oASelFW.effecta("type","//label[text()='"+label+"']/following::input[1]",value,label);
	}

	/**
	* @summary Method to click on select gender button.
	* @author Parshant G.
	* @param gender as string
	* @return 
	* @exception InterruptedException
	*/
	public void select_Gender(String gender) throws InterruptedException{

		oASelFW.effecta("waitForElementPresent","//input[@value='M']","30");
		oASelFW.effecta("click","//input[@value='"+gender+"']","Select Gender");
	}

	/**
	* @summary Method to click on create Account button.
	* @author Parshant G.
	* @param 
	* @return 
	* @exception InterruptedException
	*/
	public void click_CreateAccount() throws InterruptedException{

		oASelFW.effecta("waitForElementPresent","//input[@class='ml_function_button_createaccountRegister']","30");
		oASelFW.effecta("click","//input[@class='ml_function_button_createaccountRegister']","create account");
	}

	/**
	* @summary Method to verify account successfully created message.
	* @author Parshant G.
	* @param  expectedtext as string
	* @return 
	* @exception InterruptedException
	*/
	public void verify_AccountCreatedSuccessfully(String expectedText) throws InterruptedException{

		oASelFW.effecta("waitForElementPresent","//div[text()='Information']/following-sibling::div[1]","30");

		String actualText=oASelFW.driver.findElement(By.xpath("//div[text()='Information']/following-sibling::div[1]")).getText();

		actualText.trim();

		System.out.println("Text is : " +actualText);

		if(actualText.contains(expectedText)){
			oASelFW.effecta("sendReport","verify account created successfully  message","Message: "+actualText+" Successfully verified.","Pass");
		}

		else
		{
			oASelFW.effecta("sendReportWithOutExit","verify account created successfully  message","Fail to verify message: "+actualText,"Fail");

		}
	}
	
	/**
	* @summary Method to verify username after login.
	* @author Parshant G.
	* @param first as string
	* @return 
	* @exception InterruptedException
	*/
	public void verify_UserName(String firstName) throws InterruptedException{
		
		oASelFW.effecta("waitForElementPresent","//span[text()='"+firstName+"']","30");
		oASelFW.effecta("verifyElementPresent","//span[text()='"+firstName+"']","User Name:" +firstName);	
	}
	
	/**
	* @summary Method to click on signout button.
	* @author Parshant G.
	* @param 
	* @return 
	* @exception InterruptedException
	*/
	public void click_SignOut() throws InterruptedException{

		oASelFW.effecta("waitForElementPresent","//a[@class='account']","30");
		WebElement element=oASelFW.driver.findElement(By.xpath("//a[@class='account']"));

		Actions ac=new Actions(oASelFW.driver);
		ac.moveToElement(element).build().perform();

		Thread.sleep(3000);

		oASelFW.effecta("click","//a[text()='Sign Out']","Sign out");
	}
	
	/**
	* @summary Method to click on login button.
	* @author Parshant G.
	* @param 
	* @return 
	* @exception InterruptedException
	*/
	public void click_Login() throws InterruptedException{

		oASelFW.effecta("waitForElementPresent","//input[@value='Login']","30");
		oASelFW.effecta("click","//input[@value='Login']","Login");
	}
}
