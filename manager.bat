@ECHO off
cls

:start
cls
ECHO MAKE SURE THIS IS IN YOUR MAIN DIRECTORY WITH A FILE NAMED GRADLEW.BAT
ECHO This was designed for 1.12.2
ECHO
ECHO 1. Build jar file
ECHO 2. Run client
ECHO 3. Run server
ECHO 4. Run setup
ECHO 5. Clean project
ECHO.

CHOICE /C 12345 /M "Enter your choice:"

IF ERRORLEVEL 5 GOTO clean
IF ERRORLEVEL 4 GOTO setup
IF ERRORLEVEL 3 GOTO server
IF ERRORLEVEL 2 GOTO client
IF ERRORLEVEL 1 GOTO build

:build
cls
gradlew clean
gradlew build
ECHO Would you like to return to start? 
CHOICE /C 12 /M "1 for yes 2 for no"

IF ERRORLEVEL 2 GOTO exit
IF ERRORLEVEL 1 GOTO start

:client
cls
gradlew runClient
ECHO Would you like to return to start? 
CHOICE /C 12 /M "1 for yes 2 for no"

IF ERRORLEVEL 2 GOTO exit
IF ERRORLEVEL 1 GOTO start

:server


CHOICE /C 12 /M "1 for yes 2 for no"

IF ERRORLEVEL 2 GOTO exit
IF ERRORLEVEL 1 GOTO start

:setup
ECHO.
ECHO.
ECHO Pick your coding software for it to setup. 
CHOICE /C 12 /M "1 for idea 2 for eclipse"

IF ERRORLEVEL 2 GOTO idea
IF ERRORLEVEL 1 GOTO eclipse

:idea
cls
gradlew setupdecompworkspace idea genintellijruns
echo if you see no errors here continue, else search up the error and rerun the program.
pause

ECHO Would you like to return to start? 
CHOICE /C 12 /M "1 for yes 2 for no"

IF ERRORLEVEL 2 GOTO exit
IF ERRORLEVEL 1 GOTO start


:eclipse
cls
gradlew setupdecompworkspace eclipse

ECHO Would you like to return to start? 
CHOICE /C 12 /M "1 for yes 2 for no"

IF ERRORLEVEL 2 GOTO exit
IF ERRORLEVEL 1 GOTO start



:exit
cls
ECHO SHUTTING DOWN...
exit
