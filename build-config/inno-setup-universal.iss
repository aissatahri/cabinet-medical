; Script Inno Setup pour Cabinet Medical
; Version Universelle avec Configuration Modifiable

#define MyAppName "Cabinet Medical - Universal"
#define MyAppVersion "1.0.0"
#define MyAppPublisher "AzMicro"
#define MyAppExeName "CabinetMedical-Universal.exe"

[Setup]
AppId={{U1N2I3V4-E5R6-7890-ABCD-EF1234567890}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName={autopf}\Cabinet Medical
DefaultGroupName=Cabinet Medical
AllowNoIcons=yes
OutputDir=D:\Project_JavaFX\CabinetMedical\installers
OutputBaseFilename=CabinetMedical-Universal-Setup
SetupIconFile=D:\Project_JavaFX\CabinetMedical\src\main\resources\com\azmicro\moms\images\cardiology.ico
Compression=lzma
SolidCompression=yes
WizardStyle=modern
PrivilegesRequired=admin

[Languages]
Name: "french"; MessagesFile: "compiler:Languages\French.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "D:\Project_JavaFX\CabinetMedical\installers\CabinetMedical-Universal.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Project_JavaFX\CabinetMedical\target\CabinetMedical-1.0-SNAPSHOT.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Project_JavaFX\CabinetMedical\installers\database.properties"; DestDir: "{app}"; Flags: ignoreversion confirmoverwrite
Source: "D:\Project_JavaFX\CabinetMedical\src\main\resources\database.properties.example"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Project_JavaFX\CabinetMedical\installers\LANCER_DEBUG.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Project_JavaFX\CabinetMedical\lancer-avec-logs.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Project_JavaFX\CabinetMedical\diagnostic.bat"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{group}\Lancer avec Logs (Diagnostic)"; Filename: "{app}\LANCER_DEBUG.bat"; IconFilename: "{app}\{#MyAppExeName}"
Name: "{group}\Diagnostic Système"; Filename: "{app}\diagnostic.bat"
Name: "{group}\Configuration Base de Données"; Filename: "notepad.exe"; Parameters: """{app}\database.properties"""
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

[Messages]
french.FinishedLabel=L'installation de [name] est terminée.%n%nVous pouvez configurer la connexion à la base de données en éditant le fichier database.properties dans le dossier d'installation.
