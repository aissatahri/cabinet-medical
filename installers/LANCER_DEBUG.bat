@echo off
title Cabinet Medical - Diagnostic Console
color 0A
echo.
echo  ================================================
echo    CABINET MEDICAL - LANCEMENT AVEC DIAGNOSTIC
echo  ================================================
echo.
echo  Ce script lance l'application et affiche
echo  tous les messages d'erreur dans cette console.
echo.
echo  ================================================
echo.

REM Chercher le JAR dans différents emplacements
set JAR_FILE=

if exist "CabinetMedical-1.0-SNAPSHOT.jar" (
    set JAR_FILE=CabinetMedical-1.0-SNAPSHOT.jar
) else if exist "target\CabinetMedical-1.0-SNAPSHOT.jar" (
    set JAR_FILE=target\CabinetMedical-1.0-SNAPSHOT.jar
) else if exist "..\target\CabinetMedical-1.0-SNAPSHOT.jar" (
    set JAR_FILE=..\target\CabinetMedical-1.0-SNAPSHOT.jar
)

if "%JAR_FILE%"=="" (
    echo  [ERREUR] Fichier JAR non trouve!
    echo.
    echo  Cherche dans:
    echo  - CabinetMedical-1.0-SNAPSHOT.jar
    echo  - target\CabinetMedical-1.0-SNAPSHOT.jar
    echo  - ..\target\CabinetMedical-1.0-SNAPSHOT.jar
    echo.
    pause
    exit /b 1
)

echo  [INFO] JAR trouve: %JAR_FILE%
echo.
echo  ------------------------------------------------
echo  Verification de Java...
echo  ------------------------------------------------
echo.

java -version 2>&1
if %ERRORLEVEL% neq 0 (
    echo.
    echo  [ERREUR] Java n'est pas installe ou pas dans PATH
    echo.
    echo  Telechargez Java 21 depuis:
    echo  https://adoptium.net/
    echo.
    pause
    exit /b 1
)

echo.
echo  ------------------------------------------------
echo  Lancement de l'application...
echo  ------------------------------------------------
echo.
echo  Les logs apparaitront ci-dessous.
echo  Si l'application plante, les erreurs seront
echo  affichees avant que cette fenetre se ferme.
echo.
echo  ================================================
echo.

REM Lancer Java avec les logs
java -jar "%JAR_FILE%" 2>&1

set EXIT_CODE=%ERRORLEVEL%

echo.
echo  ================================================
echo  L'application s'est terminee
echo  Code de sortie: %EXIT_CODE%
echo  ================================================
echo.

if %EXIT_CODE% neq 0 (
    color 0C
    echo  [ERREUR] L'application s'est terminee avec une erreur
    echo.
    echo  Lisez les messages ci-dessus pour identifier le probleme.
    echo.
) else (
    color 0A
    echo  [OK] L'application s'est terminee normalement
    echo.
)

echo  Appuyez sur une touche pour fermer cette fenetre...
pause > nul
