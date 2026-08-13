@echo off
setlocal

echo Compiling Mastermind (source)...
if exist build rmdir /s /q build
mkdir build\classes

javac -d build\classes -encoding UTF-8 src\com\mastermind\*.java
if errorlevel 1 (
    echo Build failed.
    exit /b 1
)

echo Build succeeded. Classes are in build\classes
endlocal
