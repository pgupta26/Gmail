<%@page import="java.io.File" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="javax.servlet.http.HttpServlet" %>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Execution</title>
<script language="JavaScript">
	function onlyNumbers(evt)
	{
		var e = event || evt; // for trans-browser compatibility
		var charCode = e.which || e.keyCode;

		if (charCode > 31 && (charCode < 48 || charCode > 57))
			return false;

		return true;

	}
</script>
</head>
<body>
<%
ServletContext context = getServletContext();
String scriptDir = context.getRealPath("/WEB-INF/Uploads");
File listAllFiles=new File(scriptDir);
if (!listAllFiles.exists()) {
	listAllFiles.mkdirs();
}
List<String> jmxFile = new ArrayList<String>();
for (File file : listAllFiles.listFiles()) {
    if (file.getName().endsWith((".jmx"))) {
    	
    	jmxFile.add(file.getName().substring(0,file.getName().lastIndexOf(".")));
    }
  }
%>
<form action="executeScript" method="post">
	<table>
		<tr>
  			<td>Select Script</td>
  			<td>
	  			<select name="selection">
		  			<option>Select One</option><%
						for(String s:jmxFile)
						{
						%><option><%=s %></option>
						<%
							}
						%>
				</select>
  			</td>
  		</tr>
  		<tr>
  			<td>Number of User's </td>
  			<td><input type="number" name="users"></td>
  		</tr>
  		<tr>
  			<td>Ramp Up Time </td>
  			<td><input type="number" name="rampup" ></td>
  		</tr>
  		<tr>
  			<td>Iterations</td>
  			<td><input type="number" name="iteration"></td>
  		</tr>
  		<tr>
  			<td><input type="button" value="Home" onClick="JavaScript:window.location='home.jsp';"/></td>
  			<td><input type="submit" value="Run Script"> </td>	
  		</tr>
	</table>
</form> 
</body>
</html>