package com.saffuse;

public class ToolsQAHomePage {
	
	SaffuseFrameworkAPI oASelFW ;
	
	public ToolsQAHomePage(SaffuseFrameworkAPI oASelFW){
		this.oASelFW=oASelFW;
	}
	
	public void clickNavigationLinks(String linkName) throws InterruptedException{
		oASelFW.method("waitForElementPresent","link="+linkName,"30");
		oASelFW.method("click","link="+linkName,linkName);
	}
}
