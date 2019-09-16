package com.qualitesoft.AmazonScrapperProject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.qualitesoft.core.InitializeTest;
import com.qualitesoft.core.SeleniumFunction;
import com.qualitesoft.core.WaitTool;

public class GetContentDetails extends InitializeTest {

	public void storeDateIntoExcel() throws IOException {
		
		List<WebElement> allRows = null;
		List<WebElement> allColumns = null;
		
		//Read data and store into excel
		WaitTool.sleep(10);
		
		FileInputStream inputStream = new FileInputStream("binaries/ContentDetails.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		inputStream.close();
		
		SXSSFWorkbook wb = new SXSSFWorkbook(workbook); 
        wb.setCompressTempFiles(true);
        
        SXSSFSheet sh = (SXSSFSheet) wb.getSheet("Sheet1");
        sh.setRandomAccessWindowSize(100);
        
		//XSSFSheet sheet = workbook.getSheet("Sheet1");
		int rowCount = sh.getLastRowNum();
		Row row;
		
		for(int pageCounter=1; pageCounter<149; pageCounter++) 
		{
			WaitTool.sleep(5);
			allRows=driver.findElements(By.xpath("//*[@id='app-main']/div[3]/div/awsui-table/div/div[3]/table/descendant::tr"));
			System.out.println("Page Number "+pageCounter+" Total Rows: "+allRows.size());
			
			//for(WebElement row : allRows) {
			for(int i=1; i< allRows.size(); i++) {
				
				row=sh.createRow(++rowCount);
				
				allColumns = allRows.get(i).findElements(By.xpath("td/span"));

				row.createCell(0).setCellValue(allColumns.get(0).getText());
				row.createCell(1).setCellValue(allColumns.get(1).getText());
				row.createCell(2).setCellValue(allColumns.get(2).getText());
				row.createCell(3).setCellValue(allColumns.get(3).getText());
				row.createCell(4).setCellValue(allColumns.get(4).getText());
				row.createCell(5).setCellValue(allColumns.get(5).getText());
			}

			//Click on next page icon
			SeleniumFunction.click(WaitTool.waitForElementPresentAndDisplay(driver, By.className("awsui-table-pagination-next-page"), 5));
		}	
				 
         FileOutputStream outputStream = new FileOutputStream("binaries/ContentDetails.xlsx");
         wb.write(outputStream);
         wb.close();
         outputStream.close();
	}

	@Test
	public void testGetContentDetails() throws IOException {

		//Click on A+ Content Manager link
		SeleniumFunction.click(WaitTool.waitForElementPresentAndDisplay(driver, By.linkText("A+ Content Manager"), 10));

		//Get total no. of pages
		//int pageTotal=Integer.parseInt(SeleniumFunction.getText(WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//*[@id='app-main']/div[3]/div/awsui-table/div/div[2]/div/div[2]/span/span/awsui-table-pagination/ul/li[10]/button"), 10)));

		//Store data into excel
		storeDateIntoExcel();
	}
}

