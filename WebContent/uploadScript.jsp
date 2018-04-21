<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>Uploading</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
   <script language="JavaScript">
   	function Checkfiles()
   	{
   		var fup = document.getElementById('fname');
   		var fileName = fup.value;
   		var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
   		if(ext == "jmx" )
   		{
   			return true;
  	 	} 
   		else
   		{
   			alert("Upload jmx files only");
   			fup.focus();
   			return false;
   		}
   }
</script>
 <form action="uploadScript" method="post" enctype="multipart/form-data">
  <table>
  	<tr>
  		<td>Script Name </td>
  		<td><input type="file" name="fname" id="fname" onchange="return Checkfiles();"></td>
  	</tr>
  	<tr>
  		<td><input type="button" value="Home" onClick="JavaScript:window.location='home.jsp';"/></td>
  		<td><input type="submit" value="Upload"></td>
  	</tr>
  </table>
 </form> 
  </body>
</html>
