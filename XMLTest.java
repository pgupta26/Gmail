package scripts;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLTest {
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException{
		
		 
	            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
	            DocumentBuilder b = f.newDocumentBuilder();
	            Document doc = b.parse(new File("D:\\Selenium_Projects\\Testing_Project\\Data.xml"));
	            System.out.println("parsed");

	            XPath xPath = XPathFactory.newInstance().newXPath();
	            Node startDateNode = (Node) xPath.compile("/data/password").evaluate(doc, XPathConstants.NODE);
	            startDateNode.setTextContent("9876");
	            
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer();
	            DOMSource source = new DOMSource(doc);
	            StreamResult result = new StreamResult(new File("D:\\Selenium_Projects\\Testing_Project\\Data.xml"));
	            transformer.transform(source, result);
	            System.out.println("update success.");
	            
	             
	}

}
