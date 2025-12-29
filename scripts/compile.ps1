# Script para compilar o projeto
Write-Host "==============================================`n" -ForegroundColor Cyan
Write-Host "  COMPILANDO PROJETO BIBLIOTECA`n" -ForegroundColor Cyan
Write-Host "==============================================`n" -ForegroundColor Cyan

# Criar diretório bin se não existir
if (!(Test-Path "bin")) {
    Write-Host "[+] Criando diretório bin..." -ForegroundColor Yellow
    New-Item -ItemType Directory -Force -Path "bin" | Out-Null
}

# Limpar compilações antigas
Write-Host "[+] Limpando compilações antigas..." -ForegroundColor Yellow
Remove-Item -Path "bin\*" -Recurse -Force -ErrorAction SilentlyContinue

# Compilar todos os arquivos Java
Write-Host "[+] Compilando arquivos Java..." -ForegroundColor Yellow
$javaFiles = Get-ChildItem -Recurse -Path "src\main\java" -Filter "*.java"
$totalFiles = $javaFiles.Count
$current = 0

foreach ($file in $javaFiles) {
    $current++
    Write-Progress -Activity "Compilando" -Status "$current de $totalFiles" -PercentComplete (($current / $totalFiles) * 100)
    javac -encoding UTF-8 -cp "lib\*" -d bin -sourcepath src\main\java $file.FullName 2>&1 | Out-Null
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "`n[ERRO] Falha ao compilar: $($file.Name)" -ForegroundColor Red
        exit 1
    }
}

Write-Progress -Activity "Compilando" -Completed

# Copiar arquivo de configuração
Write-Host "[+] Copiando arquivos de recursos..." -ForegroundColor Yellow
Copy-Item "src\main\resources\database.properties" "bin\database.properties" -Force

Write-Host "`n==============================================`n" -ForegroundColor Green
Write-Host "  COMPILACAO CONCLUIDA COM SUCESSO!`n" -ForegroundColor Green
Write-Host "==============================================`n" -ForegroundColor Green
Write-Host "Total de arquivos compilados: $totalFiles`n" -ForegroundColor White
Write-Host "Próximos passos:" -ForegroundColor Cyan
Write-Host "  - Execute: .\run-console.ps1 (modo console)" -ForegroundColor White
Write-Host "  - Execute: .\run-web.ps1 (modo web)`n" -ForegroundColor White
