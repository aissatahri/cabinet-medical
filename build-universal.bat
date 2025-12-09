@echo off
echo ========================================
echo Construction Cabinet Medical - Version Universelle
echo ========================================
echo.

REM Compilation Maven
echo [1/3] Compilation du projet Maven...
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

REM Lancement de Launch4j pour version Universelle
echo [2/3] Creation de l'executable Universel avec Launch4j...
"C:\Program Files (x86)\Launch4j\launch4jc.exe" "build-config\launch4j-universal.xml"
if %ERRORLEVEL% neq 0 (
    echo ERREUR: Launch4j a echoue
    pause
    exit /b 1
)
echo Executable Universel cree avec succes
echo.

REM Copier le fichier database.properties dans le repertoire installers
echo Copie des fichiers de configuration et diagnostic...
copy "src\main\resources\database.properties" "installers\database.properties"
copy "LANCER_DEBUG.bat" "installers\LANCER_DEBUG.bat"
echo.

REM Creation de l'installeur Universel avec Inno Setup
echo [3/3] Creation de l'installeur Universel avec Inno Setup...
"C:\Program Files (x86)\Inno Setup 6\ISCC.exe" "build-config\inno-setup-universal.iss"
if %ERRORLEVEL% neq 0 (
    echo ERREUR: Inno Setup a echoue
    pause
    exit /b 1
)
echo Installeur Universel cree avec succes
echo.

echo ========================================
echo CONSTRUCTION TERMINEE AVEC SUCCES!
echo ========================================
echo.
echo Fichiers generes:
echo - installers\CabinetMedical-Universal.exe
echo - installers\CabinetMedical-Universal-Setup.exe
echo - installers\database.properties (fichier de configuration)
echo.
echo REMARQUE: Cette version utilise le fichier database.properties
echo pour la configuration de la base de donnees.
echo Les utilisateurs peuvent modifier ce fichier selon leurs besoins.
echo.
pause