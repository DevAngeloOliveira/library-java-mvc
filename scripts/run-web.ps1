# Script para executar o servidor web
Write-Host "==============================================`n" -ForegroundColor Cyan
Write-Host "  INICIANDO SERVIDOR WEB`n" -ForegroundColor Cyan
Write-Host "==============================================`n" -ForegroundColor Cyan

# Verificar se está compilado
if (!(Test-Path "bin\com\biblioteca\MainWeb.class")) {
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

# Verificar se a porta 8080 está em uso
$portInUse = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue

if ($portInUse) {
    Write-Host "[AVISO] Porta 8080 já está em uso!" -ForegroundColor Yellow
    Write-Host "Deseja encerrar o processo existente? (S/N): " -ForegroundColor Yellow -NoNewline
    $resposta = Read-Host
    
    if ($resposta -eq "S" -or $resposta -eq "s") {
        $processId = $portInUse.OwningProcess
        Stop-Process -Id $processId -Force
        Write-Host "[+] Processo encerrado.`n" -ForegroundColor Green
        Start-Sleep -Seconds 2
    } else {
        Write-Host "[CANCELADO] Servidor não iniciado." -ForegroundColor Red
        exit 1
    }
}

# Executar servidor
Write-Host "[+] Iniciando servidor na porta 8080...`n" -ForegroundColor Green
Write-Host "Acesse: " -ForegroundColor White -NoNewline
Write-Host "http://localhost:8080`n" -ForegroundColor Cyan
Write-Host "Pressione CTRL+C para encerrar o servidor...`n" -ForegroundColor Yellow

java -cp "bin;lib\*" com.biblioteca.MainWeb
