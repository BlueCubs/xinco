@echo off

if not "%JAVA_HOME%" == "" goto gotJavaHome
echo You must set JAVA_HOME to point at your Java Development Kit installation
goto cleanup
:gotJavaHome

echo Importing Server's certificate into Client's Java Web Start keystore
%java_home%\bin\keytool -import -v -trustcacerts -alias tomcat -file server.cer -keystore "[full/path/to/Java Runtime Environment]/lib/security/cacerts" -keypass changeit -storepass changeit

:cleanup
