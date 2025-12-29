# Script para executar testes unitarios
Write-Host "==============================================`n" -ForegroundColor Cyan
Write-Host "  EXECUTANDO TESTES UNITARIOS`n" -ForegroundColor Cyan
Write-Host "==============================================`n" -ForegroundColor Cyan

# Verificar se está compilado
if (!(Test-Path "bin\com\biblioteca\test\BibliotecaTest.class")) {
    Write-Host "[+] Compilando testes..." -ForegroundColor Yellow
    .\compile.ps1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERRO] Falha na compilação" -ForegroundColor Red
        exit 1
    }
}

# Executar testes
Write-Host "[+] Executando testes...`n" -ForegroundColor Green
java -cp "bin;lib\*" com.biblioteca.test.BibliotecaTest

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n[SUCESSO] Todos os testes passaram!`n" -ForegroundColor Green
} else {
    Write-Host "`n[FALHA] Alguns testes falharam!`n" -ForegroundColor Red
    exit 1
}
