@echo off
setlocal

set "ROOT=C:\Users\Jordan\Desktop\MCNetwork"

echo ==========================================
echo        AETHERIA NETWORK LAUNCHER
echo ==========================================
echo.
echo Starting:
echo   Playit
echo   Hub
echo   Survival
echo   Minigames
echo   Velocity
echo.
echo Aetheria Manager is hosted on Wispbyte.
echo.

if not exist "%ROOT%\start-playit.bat" goto ERROR
if not exist "%ROOT%\start-hub.bat" goto ERROR
if not exist "%ROOT%\start-survival.bat" goto ERROR
if not exist "%ROOT%\start-minigames.bat" goto ERROR
if not exist "%ROOT%\start-velocity.bat" goto ERROR

start "" wt.exe -w Aetheria new-tab --title "Aetheria - Playit" cmd /k call "%ROOT%\start-playit.bat" ^
; new-tab --title "Aetheria - Hub" cmd /k call "%ROOT%\start-hub.bat" ^
; new-tab --title "Aetheria - Survival" cmd /k call "%ROOT%\start-survival.bat" ^
; new-tab --title "Aetheria - Minigames" cmd /k call "%ROOT%\start-minigames.bat" ^
; new-tab --title "Aetheria - Velocity" cmd /k call "%ROOT%\start-velocity.bat"

echo Aetheria launch command sent.
echo.
timeout /t 3 /nobreak >nul
exit /b 0

:ERROR
echo.
echo ERROR: One or more launcher files are missing.
echo Make sure all five start-*.bat files are in:
echo %ROOT%
echo.
pause
exit /b 1