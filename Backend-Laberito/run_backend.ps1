$ErrorActionPreference = "Stop"
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12

$mavenVersion = "3.9.6"
# Using archive.apache.org which is more reliable than dlcdn mirrors
$mavenUrl = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$mavenZip = "maven.zip"
$mavenDir = "apache-maven-$mavenVersion"
$mavenBin = "$PWD\$mavenDir\bin\mvn.cmd"

if (-not (Test-Path $mavenBin)) {
    Write-Host "Descargando Maven (esto puede tardar un poco)..."
    $webClient = New-Object System.Net.WebClient
    $webClient.DownloadFile($mavenUrl, "$PWD\$mavenZip")
    
    if ((Get-Item $mavenZip).Length -lt 1000000) {
        Write-Host "Error: El archivo descargado es demasiado pequeño. Algo falló."
        Exit 1
    }

    Write-Host "Descomprimiendo..."
    Expand-Archive -Path $mavenZip -DestinationPath . -Force
    Remove-Item $mavenZip
}

$absPath = (Get-Item $mavenBin).FullName
Write-Host ""
Write-Host "¡ÉXITO!" -ForegroundColor Green
Write-Host "Copia esta ruta y pégala en la configuración de VS Code:" -ForegroundColor Yellow
Write-Host $absPath -ForegroundColor Cyan
Write-Host ""
Write-Host "Iniciando servidor..."
& $mavenBin spring-boot:run
$ErrorActionPreference = "Stop"

$mavenVersion = "3.9.6"
$mavenUrl = "https://dlcdn.apache.org/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$mavenZip = "maven.zip"
$mavenDir = "apache-maven-$mavenVersion"

if (-not (Test-Path "$mavenDir\bin\mvn.cmd")) {
    Write-Host "Descargando Maven portable..."
    Invoke-WebRequest -Uri $mavenUrl -OutFile $mavenZip
    
    Write-Host "Descomprimiendo Maven..."
    Expand-Archive -Path $mavenZip -DestinationPath . -Force
    
    Remove-Item $mavenZip
}

Write-Host "Ejecutando Spring Boot..."
& ".\$mavenDir\bin\mvn.cmd" spring-boot:run
