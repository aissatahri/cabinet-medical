@echo off
echo ========================================
echo Construction Cabinet Medical - Version Debug
echo ========================================
echo.

REM Compilation Maven (réutiliser le JAR existant si disponible)
if not exist "target\CabinetMedical-1.0-SNAPSHOT.jar" (
    echo [1/2] Compilation du projet Maven...
    call mvn clean package -DskipTests
    if %ERRORLEVEL% neq 0 (
        echo ERREUR: La compilation Maven a echoue
        pause
        exit /b 1
    )
    echo Compilation Maven terminee avec succes
    echo.
) else (
    echo [1/2] Utilisation du JAR existant
    echo.
)

REM Creation du repertoire installers
if not exist "installers" mkdir installers

REM Lancement de Launch4j pour version Debug
echo [2/2] Creation de l'executable Debug avec console...
"C:\Program Files (x86)\Launch4j\launch4jc.exe" "build-config\launch4j-universal-debug.xml"
if %ERRORLEVEL% neq 0 (
    echo ERREUR: Launch4j a echoue
    pause
    exit /b 1
)
echo Executable Debug cree avec succes
echo.

echo ========================================
echo CONSTRUCTION TERMINEE AVEC SUCCES!
echo ========================================
echo.
echo Fichier genere:
echo - installers\CabinetMedical-Universal-Debug.exe
echo.
echo Cette version affiche la console avec tous les logs
echo pour faciliter le diagnostic des problemes.
echo.
pause
