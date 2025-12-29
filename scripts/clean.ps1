# Script para limpar arquivos compilados e temporários
Write-Host "==============================================`n" -ForegroundColor Cyan
Write-Host "  LIMPANDO PROJETO`n" -ForegroundColor Cyan
Write-Host "==============================================`n" -ForegroundColor Cyan

$removidos = 0

# Remover pasta bin
if (Test-Path "bin") {
    Write-Host "[+] Removendo diretório bin..." -ForegroundColor Yellow
    Remove-Item -Path "bin" -Recurse -Force
    $removidos++
}

# Remover classes compiladas na raiz
$classFiles = Get-ChildItem -Path "." -Filter "*.class"
if ($classFiles.Count -gt 0) {
    Write-Host "[+] Removendo arquivos .class da raiz..." -ForegroundColor Yellow
    Remove-Item -Path "*.class" -Force
    $removidos += $classFiles.Count
}

# Remover banco de dados (opcional)
Write-Host "`nDeseja remover o banco de dados? (S/N): " -ForegroundColor Yellow -NoNewline
$resposta = Read-Host

if ($resposta -eq "S" -or $resposta -eq "s") {
    if (Test-Path "biblioteca.db") {
        Remove-Item "biblioteca.db" -Force
        Write-Host "[+] Banco de dados removido." -ForegroundColor Yellow
        $removidos++
    }
}

Write-Host "`n==============================================`n" -ForegroundColor Green
Write-Host "  LIMPEZA CONCLUÍDA!`n" -ForegroundColor Green
Write-Host "==============================================`n" -ForegroundColor Green

if ($removidos -gt 0) {
    Write-Host "Arquivos/pastas removidos: $removidos`n" -ForegroundColor White
} else {
    Write-Host "Nada para limpar!`n" -ForegroundColor White
}
