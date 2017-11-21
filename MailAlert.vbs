


If Hour(now) >= 12 Then
    ampm="PM"
Else
   ampm="AM"
End If

strDate = Day(Date())& MonthName(month(date()))&Year(Date())&"_" & Hour(now)&"_"&Minute(Now)&ampm
destinationPath="D:\Selenium_Projects\Framework_Project\ANT\apache-ant-1.9.3\bin\email.zip"
'msgbox destinationPath

Set FS = CreateObject("Scripting.FileSystemObject")
FS.MoveFile "D:\Selenium_Projects\Framework_Project1\email.zip", destinationPath
'msgbox "hello"
'EmailSubject = "AutomatedMailAlert" &"_"& ModuleName&"_"&strDate
EmailSubject ="Testing"
EmailBody = "FYI"

Const EmailFrom = "mcaparshant@gmail.com"
'EmailFromName = "Airwatch_Prod Automation Report_"  & instanceName &"_"& ModuleName&"_"&currentUser
EmailFromName = "Jenkins_QA"




'Const EmailTo = "sdhava@vmware.com,bperali@vmware.com,nrasamalla@vmware.com,schikkajalash@vmware.com,nsood@vmware.com,chakrabortyp@vmware.com,ITPCQAQAsOnly@vmware.com"
Const EmailTo="parshant.gupta00@gmail.com"




Const SMTPServer = "smtp.gmail.com"
Const SMTPLogon = "mcaparshant@gmail.com"
Const SMTPPassword = "Decentyash2612"
Const SMTPSSL = True
Const SMTPPort = 465

Const cdoSendUsingPickup = 1 	'Send message using local SMTP service pickup directory.
Const cdoSendUsingPort = 2 	'Send the message using SMTP over TCP/IP networking.

Const cdoAnonymous = 0 	' No authentication
Const cdoBasic = 1 	' BASIC clear text authentication
Const cdoNTLM = 2 	' NTLM, Microsoft proprietary authentication

' First, create the message

Set objMessage = CreateObject("CDO.Message")
objMessage.Subject = EmailSubject
objMessage.From = """" & EmailFromName & """ <" & EmailFrom & ">"
objMessage.To = EmailTo


'objMessage.AddAttachment destinationPath
objMessage.AddAttachment "D:\Selenium_Projects\Framework_Project\ANT\apache-ant-1.9.3\bin\test.txt"
Set objFSO = CreateObject("Scripting.FileSystemObject") 
'objFSO.DeleteFile(destinationPath)

'objMessage.TextBody = EmailBody
objMessage.CreateMHTMLBody "file:///D:\Selenium_Projects\Framework_Project1\testng-xslt\overview.html"
' Second, configure the server

objMessage.Configuration.Fields.Item _
("http://schemas.microsoft.com/cdo/configuration/sendusing") = 2

objMessage.Configuration.Fields.Item _
("http://schemas.microsoft.com/cdo/configuration/smtpserver") = SMTPServer

objMessage.Configuration.Fields.Item _
("http://schemas.microsoft.com/cdo/configuration/smtpauthenticate") = cdoBasic

objMessage.Configuration.Fields.Item _
("http://schemas.microsoft.com/cdo/configuration/sendusername") = SMTPLogon

objMessage.Configuration.Fields.Item _
("http://schemas.microsoft.com/cdo/configuration/sendpassword") = SMTPPassword

objMessage.Configuration.Fields.Item _
("http://schemas.microsoft.com/cdo/configuration/smtpserverport") = SMTPPort

objMessage.Configuration.Fields.Item _
("http://schemas.microsoft.com/cdo/configuration/smtpusessl") = SMTPSSL

objMessage.Configuration.Fields.Item _
("http://schemas.microsoft.com/cdo/configuration/smtpconnectiontimeout") = 100

objMessage.Configuration.Fields.Update

' Now send the message!

objMessage.Send