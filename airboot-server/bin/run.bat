@echo off
echo.
echo [��Ϣ] ʹ�� Spring Boot ���� Web ���̡�
echo.

%~d0
cd %~dp0

cd ../target

set JAVA_OPTS=-Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java -jar %JAVA_OPTS% airboot-server.jar

pause