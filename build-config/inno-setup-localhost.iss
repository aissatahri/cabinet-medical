; Script Inno Setup pour Cabinet Medical
; Version Localhost

#define MyAppName "Cabinet Medical - Localhost"
#define MyAppVersion "1.0.0"
#define MyAppPublisher "AzMicro"
#define MyAppExeName "CabinetMedical-Localhost.exe"

[Setup]
AppId={{A1B2C3D4-E5F6-7890-ABCD-EF1234567890}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName={autopf}\{#MyAppName}
DefaultGroupName={#MyAppName}
AllowNoIcons=yes
OutputDir=D:\Project_JavaFX\CabinetMedical\installers
OutputBaseFilename=CabinetMedical-Localhost-Setup
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
Source: "D:\Project_JavaFX\CabinetMedical\target\CabinetMedical-Localhost.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Project_JavaFX\CabinetMedical\target\CabinetMedical-1.0-SNAPSHOT.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Project_JavaFX\CabinetMedical\target\classes\*"; DestDir: "{app}\classes"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent
