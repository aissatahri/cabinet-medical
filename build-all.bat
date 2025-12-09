@echo off
echo ========================================
echo Construction des versions Cabinet Medical
echo ========================================
echo.

REM Compilation Maven
echo [1/5] Compilation du projet Maven...
call mvn clean package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo ERREUR: La compilation Maven a echoue
    pause
    exit /b 1
)
echo Compilation Maven terminee avec succes
echo.

REM Creation du repertoire installers
if not exist "installers" mkdir installers

REM Lancement de Launch4j pour version Localhost
echo [2/5] Creation de l'executable Localhost avec Launch4j...
"C:\Program Files (x86)\Launch4j\launch4jc.exe" "build-config\launch4j-localhost.xml"
if %ERRORLEVEL% neq 0 (
    echo ERREUR: Launch4j Localhost a echoue
    pause
    exit /b 1
)
echo Executable Localhost cree avec succes
echo.

REM Lancement de Launch4j pour version Serveur
echo [3/5] Creation de l'executable Serveur (192.168.1.34) avec Launch4j...
"C:\Program Files (x86)\Launch4j\launch4jc.exe" "build-config\launch4j-server.xml"
if %ERRORLEVEL% neq 0 (
    echo ERREUR: Launch4j Serveur a echoue
    pause
    exit /b 1
)
echo Executable Serveur cree avec succes
echo.

REM Creation de l'installeur Localhost avec Inno Setup
echo [4/5] Creation de l'installeur Localhost avec Inno Setup...
"C:\Program Files (x86)\Inno Setup 6\ISCC.exe" "build-config\inno-setup-localhost.iss"
if %ERRORLEVEL% neq 0 (
    echo ERREUR: Inno Setup Localhost a echoue
    pause
    exit /b 1
)
echo Installeur Localhost cree avec succes
echo.

REM Creation de l'installeur Serveur avec Inno Setup
echo [5/5] Creation de l'installeur Serveur avec Inno Setup...
"C:\Program Files (x86)\Inno Setup 6\ISCC.exe" "build-config\inno-setup-server.iss"
if %ERRORLEVEL% neq 0 (
    echo ERREUR: Inno Setup Serveur a echoue
    pause
    exit /b 1
)
echo Installeur Serveur cree avec succes
echo.

echo ========================================
echo CONSTRUCTION TERMINEE AVEC SUCCES!
echo ========================================
echo.
echo Fichiers generes:
echo - target\CabinetMedical-Localhost.exe
echo - target\CabinetMedical-Server.exe
echo - installers\CabinetMedical-Localhost-Setup.exe
echo - installers\CabinetMedical-Server-Setup.exe
echo.
pause
