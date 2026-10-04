@echo off
setlocal enabledelayedexpansion
cd /d "%~dp0"

set "ROOT=%~dp0.."
set "PAPER=%ROOT%\Hub\versions\1.21.1\paper-1.21.1.jar"
if not exist "%PAPER%" (
  echo Could not find %PAPER%
  echo Put this HubPlugin folder next to your Hub folder and try again.
  pause
  exit /b 1
)

set "CP=%PAPER%"
for /r "%ROOT%\Hub\libraries" %%f in (*.jar) do set "CP=!CP!;%%f"
set "CP=!CP:\=/!"
> cp.txt echo -cp "!CP!"

if exist out rmdir /s /q out
mkdir out

javac --release 21 -encoding UTF-8 @cp.txt -d out src\net\aetheria\hub\HubPlugin.java
if errorlevel 1 (
  echo.
  echo BUILD FAILED - copy the errors above and send them to me.
  pause
  exit /b 1
)

copy /y plugin.yml out\ >nul
jar --create --file AetheriaHub.jar -C out .
del cp.txt

echo.
echo Built AetheriaHub.jar - copy it into Hub\plugins and restart the Hub.
pause
