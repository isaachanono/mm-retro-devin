@echo off
setlocal

echo Compiling Mastermind (source + tests)...
if exist build rmdir /s /q build
mkdir build\classes
mkdir build\test-classes

javac -d build\classes -encoding UTF-8 src\com\mastermind\*.java
if errorlevel 1 (
    echo Source compilation failed.
    exit /b 1
)

javac -cp build\classes -d build\test-classes -encoding UTF-8 test\com\mastermind\*.java
if errorlevel 1 (
    echo Test compilation failed.
    exit /b 1
)

echo Running tests...
java -cp build\classes;build\test-classes com.mastermind.TestRunner com.mastermind.GameLogicTest
set TEST_RESULT=%errorlevel%

if %TEST_RESULT% neq 0 (
    echo.
    echo TESTS FAILED
    exit /b %TEST_RESULT%
) else (
    echo.
    echo ALL TESTS PASSED
)

endlocal
