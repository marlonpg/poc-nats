@echo off
set JAVA_HOME=C:\Users\gamba\.jdks\corretto-25.0.1
set PATH=%JAVA_HOME%\bin;%PATH%

if "%1"=="" (
    echo Usage: run.bat ^<username^>
    echo Example: run.bat Alice
    exit /b 1
)

echo Starting chat for user: %1
.\mvnw exec:java -Dexec.mainClass="Chat" -Dexec.args="%1"