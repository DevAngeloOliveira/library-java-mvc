# Script para executar o projeto em modo console
Write-Host "==============================================`n" -ForegroundColor Cyan
Write-Host "  EXECUTANDO MODO CONSOLE`n" -ForegroundColor Cyan
Write-Host "==============================================`n" -ForegroundColor Cyan

# Verificar se está compilado
if (!(Test-Path "bin\com\biblioteca\Main.class")) {
    Write-Host "[AVISO] Projeto não compilado. Compilando..." -ForegroundColor Yellow
    .\compile.ps1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERRO] Falha na compilação" -ForegroundColor Red
        exit 1
    }
}

# Verificar se o banco existe
if (!(Test-Path "biblioteca.db")) {
    Write-Host "[AVISO] Banco de dados não encontrado. Criando..." -ForegroundColor Yellow
    .\setup-db.ps1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERRO] Falha ao criar banco" -ForegroundColor Red
        exit 1
    }
}

# Executar aplicação
Write-Host "[+] Iniciando aplicação..`n" -ForegroundColor Green
java -cp "bin;lib\*" com.biblioteca.Main
