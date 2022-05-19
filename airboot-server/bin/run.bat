@echo off
echo.
echo [信息] 使用 Spring Boot 运行 Web 工程。
echo.

%~d0
cd %~dp0

cd ../target

set JAVA_OPTS=-Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java -jar %JAVA_OPTS% airboot-server.jar

pause