@echo off
chcp 65001 >nul

echo ============================================
echo  IMPORTATION DE LA BASE VERS INFOMANIAK
echo ============================================
echo.

REM === CONFIGURATION ===
set "HOST=pcdb-mau7krg.c02.dbaas.infomaniak.cloud"
set "PORT=24158"
set "USER=admin"
set "DBNAME=Cabinetmedical"
set "SQLFILE=C:\cabinetmedicalbis.sql"

echo Fichier SQL : %SQLFILE%
echo Serveur : %HOST%:%PORT%
echo Base distante : %DBNAME%
echo.

REM === Vérifier fichier SQL ===
if not exist "%SQLFILE%" (
    echo ERREUR : Le fichier "%SQLFILE%" est introuvable !
    pause
    exit /b
)

pause
echo.

REM === IMPORTATION ===
mysql --ssl-mode=REQUIRED -h %HOST% -P %PORT% -u %USER% -p %DBNAME% < "%SQLFILE%"

echo.
echo ============================================
echo  Import termine !
echo ============================================
pause
