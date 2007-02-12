@echo off

if not "%JAVA_HOME%" == "" goto gotJavaHome
echo You must set JAVA_HOME to point at your Java Development Kit installation
goto cleanup
:gotJavaHome

echo Generating the Server KeyStore in file server.keystore
%java_home%\bin\keytool -genkey -alias tomcat-sv -dname "CN=localhost, OU=X, O=Y, L=Z, S=XY, C=YZ" -keyalg RSA -keypass changeit -storepass changeit -keystore server.keystore

echo Exporting the certificate from keystore to an external file server.cer
%java_home%\bin\keytool -export -alias tomcat-sv -storepass changeit -file server.cer -keystore server.keystore

echo Importing Server's certificate into Client's keystore
%java_home%\bin\keytool -import -v -trustcacerts -alias tomcat -file server.cer -keystore client.keystore -keypass changeit -storepass changeit

:cleanup
