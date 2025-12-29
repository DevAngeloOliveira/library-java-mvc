# Script para configurar o banco de dados SQLite
Write-Host "==============================================`n" -ForegroundColor Cyan
Write-Host "  CONFIGURANDO BANCO DE DADOS SQLite`n" -ForegroundColor Cyan
Write-Host "==============================================`n" -ForegroundColor Cyan

# Verificar se os scripts SQL existem
if (!(Test-Path "src\main\resources\db\schema-sqlite.sql")) {
    Write-Host "[ERRO] Arquivo schema-sqlite.sql não encontrado!" -ForegroundColor Red
    exit 1
}

if (!(Test-Path "src\main\resources\db\seed-sqlite.sql")) {
    Write-Host "[ERRO] Arquivo seed-sqlite.sql não encontrado!" -ForegroundColor Red
    exit 1
}

# Remover banco antigo se existir
if (Test-Path "biblioteca.db") {
    Write-Host "[+] Removendo banco de dados antigo..." -ForegroundColor Yellow
    Remove-Item "biblioteca.db" -Force
}

# Compilar SetupDatabase se necessário
if (!(Test-Path "SetupDatabase.class")) {
    Write-Host "[+] Compilando SetupDatabase..." -ForegroundColor Yellow
    javac -encoding UTF-8 -cp "lib\*" SetupDatabase.java
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERRO] Falha ao compilar SetupDatabase" -ForegroundColor Red
        exit 1
    }
}

# Executar setup do banco
Write-Host "[+] Criando banco de dados..." -ForegroundColor Yellow
Write-Host "[+] Executando schema..." -ForegroundColor Yellow
Write-Host "[+] Inserindo dados iniciais..." -ForegroundColor Yellow

java -cp ".;lib\*" SetupDatabase

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n==============================================`n" -ForegroundColor Green
    Write-Host "  BANCO CONFIGURADO COM SUCESSO!`n" -ForegroundColor Green
    Write-Host "==============================================`n" -ForegroundColor Green
    Write-Host "Arquivo criado: biblioteca.db`n" -ForegroundColor White
    
    # Mostrar estatísticas
    Write-Host "Dados iniciais carregados:" -ForegroundColor Cyan
    Write-Host "  - 5 Livros" -ForegroundColor White
    Write-Host "  - 3 Revistas" -ForegroundColor White
    Write-Host "  - 4 DVDs" -ForegroundColor White
    Write-Host "  - Total: 12 itens`n" -ForegroundColor White
} else {
    Write-Host "`n[ERRO] Falha ao configurar banco de dados" -ForegroundColor Red
    exit 1
}
