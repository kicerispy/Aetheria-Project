@echo off
title Aetheria Minigames
cd /d "%~dp0"
echo.
echo ========================================
echo   AETHERIA MINIGAMES
echo ========================================
echo.
if not exist "paper.jar" (
  echo ERROR: paper.jar was not found in:
  echo %CD%
  echo.
  echo Copy your Paper server jar here and name it paper.jar
  echo.
  pause
  exit /b 1
)
if not exist "eula.txt" (
  echo First launch: Paper will create its files now.
)
"C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\java.exe" -Xms1G -Xmx2G -jar "paper.jar" --nogui
if errorlevel 1 (
  echo.
  echo Paper exited with an error. Read the message above.
)
echo.
pause
