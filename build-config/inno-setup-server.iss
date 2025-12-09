; Script Inno Setup pour Cabinet Medical
; Version Serveur (192.168.1.34)

#define MyAppName "Cabinet Medical - Serveur"
#define MyAppVersion "1.0.0"
#define MyAppPublisher "AzMicro"
#define MyAppExeName "CabinetMedical-Server.exe"

[Setup]
AppId={{B2C3D4E5-F6G7-8901-BCDE-FG2345678901}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName={autopf}\{#MyAppName}
DefaultGroupName={#MyAppName}
AllowNoIcons=yes
OutputDir=D:\Project_JavaFX\CabinetMedical\installers
OutputBaseFilename=CabinetMedical-Server-Setup
SetupIconFile=D:\Project_JavaFX\CabinetMedical\src\main\resources\com\azmicro\moms\images\logo.png
Compression=lzma
SolidCompression=yes
WizardStyle=modern
PrivilegesRequired=admin

[Languages]
Name: "french"; MessagesFile: "compiler:Languages\French.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "D:\Project_JavaFX\CabinetMedical\target\CabinetMedical-Server.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Project_JavaFX\CabinetMedical\target\CabinetMedical-1.0-SNAPSHOT.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Project_JavaFX\CabinetMedical\target\classes\*"; DestDir: "{app}\classes"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent
