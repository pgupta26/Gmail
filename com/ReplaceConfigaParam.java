package com;

import java.io.File;
import java.io.IOException;

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
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class ReplaceConfigaParam {
	
	static String inputFile = "C:\\Users\\pgupta\\Desktop\\New folder\\New folder (2)\\Test.jmx";
	static String outputFile = "C:\\Users\\pgupta\\Desktop\\New folder\\New folder (2)\\Test.jmx";
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
		 Document doc = DocumentBuilderFactory.newInstance()
			        .newDocumentBuilder().parse(new InputSource(inputFile));

			    // locate the node(s)
			    XPath xpath = XPathFactory.newInstance().newXPath();
			    
			    NodeList loopCountNode = (NodeList)xpath.evaluate
				        ("//stringProp[@name='LoopController.loops']", doc, XPathConstants.NODESET);

				    // make the change
				    for (int idx = 0; idx < loopCountNode.getLength(); idx++) {
				    	loopCountNode.item(idx).setTextContent("${__P(loopcount,1)}");
				    }
			    
			    NodeList usersNode = (NodeList)xpath.evaluate
			        ("//stringProp[@name='ThreadGroup.num_threads']", doc, XPathConstants.NODESET);

			    // make the change
			    for (int idx = 0; idx < usersNode.getLength(); idx++) {
			    	usersNode.item(idx).setTextContent("${__P(users,1)}");
			    }
			    
			    NodeList rampUpNode = (NodeList)xpath.evaluate
				        ("//stringProp[@name='ThreadGroup.ramp_time']", doc, XPathConstants.NODESET);

				    // make the change
				    for (int idx = 0; idx < rampUpNode.getLength(); idx++) {
				    	rampUpNode.item(idx).setTextContent("${__P(rampup,1)}");
				    }
			 
			    // save the result
			    Transformer xformer = TransformerFactory.newInstance().newTransformer();
			    xformer.transform
			        (new DOMSource(doc), new StreamResult(new File(outputFile)));
			    
			    System.out.println("File updated successfully.");
	    }
}
