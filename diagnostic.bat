@echo off
echo ========================================
echo Diagnostic Cabinet Medical
echo ========================================
echo.

REM Afficher les informations système
echo [1] Informations système:
echo Utilisateur: %USERNAME%
echo Répertoire utilisateur: %USERPROFILE%
echo.

REM Vérifier Java
echo [2] Vérification de Java:
java -version 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERREUR: Java n'est pas installé ou n'est pas dans le PATH
    echo.
) else (
    echo Java détecté avec succès
    echo.
)

REM Vérifier le dossier AppData
echo [3] Vérification du dossier de configuration:
set CONFIG_DIR=%APPDATA%\CabinetMedical
echo Chemin: %CONFIG_DIR%
if exist "%CONFIG_DIR%" (
    echo Le dossier existe
    dir "%CONFIG_DIR%"
) else (
    echo Le dossier n'existe pas encore
    echo Création du dossier...
    mkdir "%CONFIG_DIR%"
    if %ERRORLEVEL% equ 0 (
        echo Dossier créé avec succès
    ) else (
        echo ERREUR: Impossible de créer le dossier
    )
)
echo.

REM Vérifier le fichier de configuration
echo [4] Vérification du fichier de configuration:
set CONFIG_FILE=%CONFIG_DIR%\database.properties
if exist "%CONFIG_FILE%" (
    echo Le fichier existe: %CONFIG_FILE%
    echo Contenu:
    type "%CONFIG_FILE%"
) else (
    echo Le fichier n'existe pas encore
)
echo.

REM Vérifier l'exécutable
echo [5] Vérification de l'exécutable:
if exist "CabinetMedical-Universal.exe" (
    echo Exécutable trouvé: CabinetMedical-Universal.exe
) else (
    echo ERREUR: Exécutable non trouvé
)
echo.

REM Tester le lancement avec logs
echo [6] Test de lancement (avec logs):
echo Les logs seront affichés ci-dessous...
echo.
if exist "CabinetMedical-Universal.exe" (
    CabinetMedical-Universal.exe > app-diagnostic.log 2>&1
    echo.
    echo Logs sauvegardés dans: app-diagnostic.log
    type app-diagnostic.log
) else (
    echo Impossible de tester - exécutable non trouvé
)

echo.
echo ========================================
echo Diagnostic terminé
echo ========================================
echo.
echo Si l'application ne démarre pas:
echo 1. Vérifiez que Java 21 ou supérieur est installé
echo 2. Vérifiez les logs ci-dessus pour des erreurs
echo 3. Vérifiez que le fichier database.properties est correct
echo 4. Essayez de supprimer le fichier database.properties et relancez
echo.
pause
