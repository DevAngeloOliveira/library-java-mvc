# ============================================
# Script PowerShell para Setup do Banco
# ============================================

# Configurações - AJUSTE AQUI
$POSTGRES_PATH = "C:\Program Files\PostgreSQL\18\bin"
$DB_USER = "postgres"
$DB_NAME = "biblioteca"

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  Setup do Banco de Dados - Sistema Biblioteca" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se PostgreSQL está instalado
if (-Not (Test-Path "$POSTGRES_PATH\psql.exe")) {
    Write-Host "[ERRO] PostgreSQL nao encontrado em: $POSTGRES_PATH" -ForegroundColor Red
    Write-Host "Ajuste a variavel POSTGRES_PATH no script" -ForegroundColor Yellow
    exit 1
}

Write-Host "[1/4] Criando banco de dados '$DB_NAME'..." -ForegroundColor Yellow

# Criar banco de dados
$createDbCommand = "CREATE DATABASE $DB_NAME;"
& "$POSTGRES_PATH\psql.exe" -U $DB_USER -c $createDbCommand

if ($LASTEXITCODE -eq 0) {
    Write-Host "      Banco criado com sucesso!" -ForegroundColor Green
} else {
    Write-Host "      Aviso: Banco pode ja existir" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "[2/4] Criando tabelas (schema.sql)..." -ForegroundColor Yellow

# Executar schema.sql
& "$POSTGRES_PATH\psql.exe" -U $DB_USER -d $DB_NAME -f "src\main\resources\db\schema.sql"

if ($LASTEXITCODE -eq 0) {
    Write-Host "      Tabelas criadas com sucesso!" -ForegroundColor Green
} else {
    Write-Host "      [ERRO] Falha ao criar tabelas" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "[3/4] Populando com dados iniciais (seed.sql)..." -ForegroundColor Yellow

# Executar seed.sql
& "$POSTGRES_PATH\psql.exe" -U $DB_USER -d $DB_NAME -f "src\main\resources\db\seed.sql"

if ($LASTEXITCODE -eq 0) {
    Write-Host "      Dados inseridos com sucesso!" -ForegroundColor Green
} else {
    Write-Host "      [ERRO] Falha ao popular dados" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "[4/4] Verificando dados..." -ForegroundColor Yellow

# Verificar dados
$checkCommand = "SELECT 'Total de itens: ' || COUNT(*) FROM itens_biblioteca;"
& "$POSTGRES_PATH\psql.exe" -U $DB_USER -d $DB_NAME -c $checkCommand

Write-Host ""
Write-Host "================================================" -ForegroundColor Green
Write-Host "  Setup Concluido com Sucesso!" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Green
Write-Host ""
Write-Host "Proximos passos:" -ForegroundColor Cyan
Write-Host "1. Configure a senha em: src\main\resources\database.properties" -ForegroundColor White
Write-Host "2. Em Main.java, altere: USE_DATABASE = true" -ForegroundColor White
Write-Host "3. Execute: mvn clean compile" -ForegroundColor White
Write-Host "4. Execute: mvn exec:java -Dexec.mainClass='com.biblioteca.Main'" -ForegroundColor White
Write-Host ""
