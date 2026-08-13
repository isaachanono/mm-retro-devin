@echo off
setlocal

if not exist build\classes\com\mastermind\MastermindGame.class (
    echo Classes not found. Building first...
    call build.bat
    if errorlevel 1 exit /b 1
)

java -cp build\classes com.mastermind.MastermindGame
endlocal
