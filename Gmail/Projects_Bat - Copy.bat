
%AUTOMATION_PATH:~0,2%

cd %AUTOMATION_PATH:~0,-1%

::Set Automation_Path,path,class path and execute the testng-suite.xml
set AUTOMATION_PATH: D:\Selenium_Projects\

:: set Project: %Project_Name%
set Project: Framework_Project1

set path=%JAVA_HOME%\bin;

set CLASSPATH=.;%AUTOMATION_PATH%Resources\*;%AUTOMATION_PATH%%Project_Name%\bin;
cd %Automation_Path%xml_files_static

java -classpath %AUTOMATION_PATH%%Project_Name%\bin;%AUTOMATION_PATH%Resources\*;.; ChangeInstanceAndBrowser %Automation_Path%xml_files_static\gmail.xml qa firefox 

java -classpath %AUTOMATION_PATH%Resources\*;%AUTOMATION_PATH%%Project_Name%\bin;.; org.testng.TestNG -d %temp%\test-output %Automation_Path%xml_files_static\gmail.xml

cd %Automation_Path%%Project_Name%\ANT\apache-ant-1.9.3\bin 
ant -Dxmlfile=%Automation_Path%xml_files_static\gmail.xml
	

Pause











