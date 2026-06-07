@echo off
setlocal
set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_HOME=%DIRNAME%
set CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar

if not exist "%CLASSPATH%" (
  echo ERROR: gradle-wrapper.jar not found at %CLASSPATH%
  echo Extract the full ZIP and run this command from the main project folder.
  exit /b 1
)

java -cp "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
endlocal
