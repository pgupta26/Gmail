import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class updatxJmx {

	public static void updateFile(String filePath, String fileName, String downloadDir) {
		try{

			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new InputSource(filePath+fileName));

			//Add Machine Name in Thread Group
			NodeList threadGroups = doc.getElementsByTagName("ThreadGroup");

			for(int i=0; i<threadGroups.getLength(); i++) {
				Node threadGroup=threadGroups.item(i);
				NamedNodeMap value=threadGroup.getAttributes();
				Node nodeAttr=value.getNamedItem("testname");
				if(nodeAttr.getNodeValue().contains("machineName")){}else{
					nodeAttr.setTextContent("${__(machineName)}"+nodeAttr.getNodeValue());
				}
			}

			// Update loops Controller value in Thread Group
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList loopCountNode = (NodeList)xpath.evaluate
					("//stringProp[@name='LoopController.loops']", doc, XPathConstants.NODESET);
			for (int idx = 0; idx < loopCountNode.getLength(); idx++) {
				if(loopCountNode.item(idx).getTextContent().equals("1"))
					loopCountNode.item(idx).setTextContent("-1");
			}

			// Add Delay Start Time Node
			Element delayStartNode = doc.createElement("boolProp");
			delayStartNode.setAttribute("name", "ThreadGroup.delayedStart");
			delayStartNode.setTextContent("true");

			for(int i=0; i<threadGroups.getLength(); i++) {
				threadGroups.item(i).appendChild(delayStartNode);
			}

			//3. In Thread Groups - If the scheduler checkbooks is checked then uncheck it.
			NodeList schedulerNode = (NodeList)xpath.evaluate
					("//boolProp[@name='ThreadGroup.scheduler']", doc, XPathConstants.NODESET);
			for (int idx = 0; idx < loopCountNode.getLength(); idx++) {
				if(schedulerNode.item(idx).getTextContent().equals("true"))
					schedulerNode.item(idx).setTextContent("false");
			}

			//4. Check if Mosquito Listener already? If not then add mosquito listener
			Document updatedCodeDoc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new InputSource(filePath+"UpdatedCode.jmx"));
			
			NodeList parentNode = (NodeList)xpath.evaluate
					("//jmeterTestPlan/hashTree/hashTree", doc, XPathConstants.NODESET);
			
			NodeList list = doc.getElementsByTagName("BackendListener");
			if(list.getLength() ==0){
				NodeList backendListenerNodes = updatedCodeDoc.getElementsByTagName("BackendListener");
				Node m=backendListenerNodes.item(0);
				Node imp=doc.importNode(m, true);
				parentNode.item(0).appendChild(imp);
				Element textNode = doc.createElement("hashTree");
				textNode.setTextContent("");
				parentNode.item(0).appendChild(textNode);
			}

			//5. Check if "Define the overall duration of the test" thread group already exist containing two test actions or not? 
			NodeList overallStatusList = doc.getElementsByTagName("//testname='Define the overall duration of the test'");
			if(overallStatusList.getLength() ==0){

				NodeList overallDurationNode = updatedCodeDoc.getElementsByTagName("ThreadGroup");
				Node n=overallDurationNode.item(0);
				Node imp=doc.importNode(n, true);
				parentNode.item(0).appendChild(imp);
				
				NodeList hashTreeNode = updatedCodeDoc.getElementsByTagName("hashTree");
				Node o=hashTreeNode.item(0);
				Node imp1=doc.importNode(o, true);
				parentNode.item(0).appendChild(imp1);
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(downloadDir+fileName));
			transformer.transform(source, result);

			System.out.println("File Updated Successfully.");

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
