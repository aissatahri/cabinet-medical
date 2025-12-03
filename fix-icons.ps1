# Script to convert FontAwesomeIcon to Ikonli FontIcon in FXML files

$iconMap = @{
    'SEARCH' = 'fas-search'
    'PRINT' = 'fas-print'
    'EDIT' = 'fas-edit'
    'UPLOAD' = 'fas-upload'
    'USER' = 'fas-user'
    'FILE' = 'fas-file'
    'FILE_TEXT' = 'fas-file-alt'
    'INFO_CIRCLE' = 'fas-info-circle'
    'PLUS' = 'fas-plus'
    'TRASH' = 'fas-trash'
    'SEARCH_PLUS' = 'fas-search-plus'
    'CALENDAR' = 'fas-calendar'
    'CLOCK' = 'fas-clock'
    'DOLLAR' = 'fas-dollar-sign'
    'MONEY' = 'fas-money-bill'
    'CHECK' = 'fas-check'
    'TIMES' = 'fas-times'
}

$fxmlFiles = Get-ChildItem -Path "src\main\resources" -Filter "*.fxml" -Recurse

foreach ($file in $fxmlFiles) {
    $content = Get-Content $file.FullName -Raw -Encoding UTF8
    $modified = $false
    
    # Replace import
    if ($content -match 'de\.jensd\.fx\.glyphs\.fontawesome\.FontAwesomeIcon') {
        $content = $content -replace 'de\.jensd\.fx\.glyphs\.fontawesome\.FontAwesomeIcon', 'org.kordamp.ikonli.javafx.FontIcon'
        $modified = $true
        Write-Host "  - Updated import in: $($file.Name)"
    }
    
    # Convert all FontAwesomeIcon elements to FontIcon with proper iconLiteral
    $content = $content -replace '<FontAwesomeIcon\s+glyphName="([A-Z_]+)"([^>]*)/>', {
        param($match)
        $iconName = $match.Groups[1].Value
        $rest = $match.Groups[2].Value
        $ikonliCode = if ($iconMap.ContainsKey($iconName)) { $iconMap[$iconName] } else { "fas-$($iconName.ToLower().Replace('_','-'))" }
        "<FontIcon iconLiteral=`"$ikonliCode`"$rest/>"
    }
    
    $content = $content -replace '(<[^>]*)\s+glyphName="([A-Z_]+)"', {
        param($match)
        $before = $match.Groups[1].Value
        $iconName = $match.Groups[2].Value
        $ikonliCode = if ($iconMap.ContainsKey($iconName)) { $iconMap[$iconName] } else { "fas-$($iconName.ToLower().Replace('_','-'))" }
        if ($before -match 'FontAwesomeIcon') {
            $before = $before -replace 'FontAwesomeIcon', 'FontIcon'
        }
        "$before iconLiteral=`"$ikonliCode`""
    }
    
    # Replace remaining FontAwesomeIcon tags
    if ($content -match 'FontAwesomeIcon') {
        $content = $content -replace 'FontAwesomeIcon', 'FontIcon'
        $modified = $true
    }
    
    # Remove unsupported attributes
    if ($content -match '\s+(size|wrappingWidth|fontSmoothingType)=') {
        $content = $content -replace '\s+size="[^"]*"', ''
        $content = $content -replace '\s+wrappingWidth="[^"]*"', ''
        $content = $content -replace '\s+fontSmoothingType="[^"]*"', ''
        $modified = $true
    }
    
    if ($modified) {
        [System.IO.File]::WriteAllText($file.FullName, $content, [System.Text.Encoding]::UTF8)
        Write-Host "Converted: $($file.Name)"
    }
}

Write-Host "`nConversion complete!"
