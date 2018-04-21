package com;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
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


@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      						// 50 MB
maxRequestSize=1024*1024*100)   					// 100 MB
public class UploadServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		ServletContext context = getServletContext();
		String UPLOAD_DIR = context.getRealPath("/WEB-INF/Uploads");

		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		String fileName = null;

		// creates the save directory if it does not exists
		File fileSaveDir = new File(UPLOAD_DIR);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdirs();
		}

		//Get all the parts from request and write it to the file on server
		Part part=req.getPart("fname");

		fileName = getFileName(part);

		int i=fileName.lastIndexOf("\\");
		fileName=fileName.substring(i+1);

		part.write(UPLOAD_DIR + File.separator + fileName);

		updateScriptFile(UPLOAD_DIR + File.separator + fileName);

		RequestDispatcher rd=req.getRequestDispatcher("uploadScript.jsp");
		out.println("Script "+fileName+" uploaded successfully!!");
		rd.include(req, res);

	}

	private String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] tokens = contentDisp.split(";");
		for (String token : tokens) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=") + 2, token.length()-1);
			}
		}
		return "";
	}

	private void updateScriptFile(String fileName){

		Document doc;
		NodeList loopCountNode;
		Transformer xformer;
		try {

			doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new InputSource(fileName));
			// locate the node(s)
			XPath xpath = XPathFactory.newInstance().newXPath();

			loopCountNode = (NodeList)xpath.evaluate
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
			xformer = TransformerFactory.newInstance().newTransformer();


			xformer.transform
			(new DOMSource(doc), new StreamResult(new File(fileName)));

			System.out.println("File updated successfully.");

		}catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}catch (XPathExpressionException e) {
			e.printStackTrace();
		}catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
