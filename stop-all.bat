@echo off
title Aetheria - Stop All

echo ========================================
echo        AETHERIA - STOP ALL
echo ========================================
echo.
echo Stopping Hub, Survival, Minigames,
echo Velocity and Playit...
echo.
echo The hosted Discord bot is NOT touched.
echo.

powershell -NoProfile -ExecutionPolicy Bypass -Command ^
"$targets = @('Hub\paper.jar','Survival\paper.jar','Minigames\paper.jar','velocity.jar','playit.exe'); ^
Get-CimInstance Win32_Process | Where-Object { $_.CommandLine } | ForEach-Object { ^
  $cmd = $_.CommandLine; ^
  foreach ($target in $targets) { ^
    if ($cmd -like ('*' + $target + '*')) { ^
      Write-Host ('Stopping PID ' + $_.ProcessId); ^
      Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue; ^
      break; ^
    } ^
  } ^
}"

echo.
echo Aetheria local services stopped.
echo.
pause