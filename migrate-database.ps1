# Script PowerShell pour ajouter la colonne ETT à la base de données
# Date: 2025-12-08

Write-Host "=== Migration de la base de données CabinetMedical ===" -ForegroundColor Cyan
Write-Host ""

# Définir le chemin MySQL
$mysqlPath = "C:\Program Files\MySQL\MySQL Workbench 8.0 CE\mysql.exe"

# Vérifier si MySQL existe
if (-not (Test-Path $mysqlPath)) {
    Write-Host "ERREUR: MySQL n'a pas été trouvé à: $mysqlPath" -ForegroundColor Red
    Write-Host ""
    Write-Host "Veuillez exécuter manuellement dans MySQL Workbench:" -ForegroundColor Yellow
    Write-Host "  USE cabinetmedical;" -ForegroundColor White
    Write-Host "  ALTER TABLE consultations ADD COLUMN ett TEXT AFTER ecg;" -ForegroundColor White
    Write-Host ""
    pause
    exit 1
}

# Demander le mot de passe
$password = Read-Host "Entrez le mot de passe root MySQL" -AsSecureString
$plainPassword = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($password))

# Créer un fichier SQL temporaire
$sqlScript = @"
USE cabinetmedical;
ALTER TABLE consultations ADD COLUMN ett TEXT AFTER ecg;
SELECT 'Migration réussie!' AS Status;
"@

$tempSqlFile = "$env:TEMP\migration_ett.sql"
$sqlScript | Out-File -FilePath $tempSqlFile -Encoding UTF8

try {
    Write-Host "Exécution de la migration..." -ForegroundColor Yellow
    
    # Exécuter le script SQL
    $process = Start-Process -FilePath $mysqlPath -ArgumentList "-u root -p$plainPassword" -NoNewWindow -Wait -PassThru -RedirectStandardInput $tempSqlFile
    
    if ($process.ExitCode -eq 0) {
        Write-Host ""
        Write-Host "=== Migration réussie! ===" -ForegroundColor Green
        Write-Host "La colonne 'ett' a été ajoutée à la table 'consultations'" -ForegroundColor Green
    } else {
        Write-Host ""
        Write-Host "ERREUR lors de la migration (Code: $($process.ExitCode))" -ForegroundColor Red
    }
} catch {
    Write-Host ""
    Write-Host "ERREUR: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
    Write-Host "Veuillez exécuter manuellement dans MySQL Workbench:" -ForegroundColor Yellow
    Write-Host "  USE cabinetmedical;" -ForegroundColor White
    Write-Host "  ALTER TABLE consultations ADD COLUMN ett TEXT AFTER ecg;" -ForegroundColor White
} finally {
    # Nettoyer le fichier temporaire
    if (Test-Path $tempSqlFile) {
        Remove-Item $tempSqlFile
    }
}

Write-Host ""
pause
