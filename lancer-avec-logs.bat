@echo off
echo ========================================
echo Cabinet Medical - Lancement avec Logs
echo ========================================
echo.
echo Demarrage de l'application...
echo Les logs s'afficheront ci-dessous:
echo.
echo ----------------------------------------

REM Lancer l'application Java directement avec affichage console
java -jar "CabinetMedical-1.0-SNAPSHOT.jar"

echo.
echo ----------------------------------------
echo.
echo L'application s'est terminee.
echo Code de sortie: %ERRORLEVEL%
echo.
echo Si l'application s'est fermee immediatement,
echo lisez les messages d'erreur ci-dessus.
echo.
pause
