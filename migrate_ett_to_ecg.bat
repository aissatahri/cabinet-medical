@echo off
REM Script pour exécuter la migration - Ajout de la colonne ETT
REM Date: 2025-12-08

echo ====================================
echo Ajout de la colonne ETT
echo ====================================
echo.
echo Ce script va ajouter la colonne 'ett' dans la table consultations
echo La colonne 'ecg' existante sera conservée
echo.

REM Demander le mot de passe MySQL
set /p PASSWORD="Entrez le mot de passe MySQL root: "

REM Exécuter le script SQL
mysql -u root -p%PASSWORD% cabinetmedical < sql\rename_ett_to_ecg.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ====================================
    echo Migration réussie !
    echo ====================================
) else (
    echo.
    echo ====================================
    echo Erreur lors de la migration
    echo ====================================
)

pause
