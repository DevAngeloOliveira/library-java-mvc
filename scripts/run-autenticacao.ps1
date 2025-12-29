# ========================================
# Script: Executar Demonstração de Autenticação
# ========================================

Write-Host "==============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "  EXECUTANDO DEMONSTRACAO DE AUTENTICACAO" -ForegroundColor Yellow
Write-Host ""
Write-Host "==============================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se está compilado
if (-not (Test-Path "bin\com\biblioteca\MainAutenticacao.class")) {
    Write-Host "[ERRO] Projeto nao compilado!" -ForegroundColor Red
    Write-Host "Execute: .\compile.ps1" -ForegroundColor Yellow
    exit 1
}

# Executar
Write-Host "[+] Executando demonstracao..." -ForegroundColor Green
Write-Host ""

$env:JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"
java -cp "bin;lib/*" com.biblioteca.MainAutenticacao

Write-Host ""
Write-Host "[CONCLUIDO] Demonstracao finalizada" -ForegroundColor Green
