# Menu interativo para gerenciar o projeto
function Show-Menu {
    Clear-Host
    Write-Host "==============================================`n" -ForegroundColor Cyan
    Write-Host "  SISTEMA DE BIBLIOTECA - MENU PRINCIPAL`n" -ForegroundColor Cyan
    Write-Host "==============================================`n" -ForegroundColor Cyan
    Write-Host "  1. Compilar projeto" -ForegroundColor White
    Write-Host "  2. Configurar banco de dados" -ForegroundColor White
    Write-Host "  3. Executar modo console" -ForegroundColor White
    Write-Host "  4. Executar servidor web" -ForegroundColor White
    Write-Host "  5. Limpar projeto" -ForegroundColor White
    Write-Host "  6. Ver status do projeto" -ForegroundColor White
    Write-Host "  7. Abrir documentação (README)" -ForegroundColor White
    Write-Host "  0. Sair`n" -ForegroundColor White
    Write-Host "==============================================`n" -ForegroundColor Cyan
}

function Show-Status {
    Write-Host "`n==============================================`n" -ForegroundColor Cyan
    Write-Host "  STATUS DO PROJETO`n" -ForegroundColor Cyan
    Write-Host "==============================================`n" -ForegroundColor Cyan
    
    # Verificar compilação
    if (Test-Path "bin\com\biblioteca\Main.class") {
        Write-Host "[OK] Projeto compilado" -ForegroundColor Green
    } else {
        Write-Host "[X] Projeto não compilado" -ForegroundColor Red
    }
    
    # Verificar banco
    if (Test-Path "biblioteca.db") {
        $dbSize = (Get-Item "biblioteca.db").Length / 1KB
        Write-Host "[OK] Banco de dados existe ($([math]::Round($dbSize, 2)) KB)" -ForegroundColor Green
    } else {
        Write-Host "[X] Banco de dados não encontrado" -ForegroundColor Red
    }
    
    # Verificar dependências
    $jars = Get-ChildItem -Path "lib" -Filter "*.jar" -ErrorAction SilentlyContinue
    if ($jars.Count -gt 0) {
        Write-Host "[OK] Dependências: $($jars.Count) JARs" -ForegroundColor Green
    } else {
        Write-Host "[X] Dependências não encontradas" -ForegroundColor Red
    }
    
    # Verificar porta 8080
    $portInUse = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
    if ($portInUse) {
        Write-Host "[!] Servidor rodando na porta 8080" -ForegroundColor Yellow
    } else {
        Write-Host "[ ] Servidor não está rodando" -ForegroundColor Gray
    }
    
    Write-Host "`n==============================================`n" -ForegroundColor Cyan
    Write-Host "Pressione qualquer tecla para voltar..." -ForegroundColor Gray
    $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
}

# Loop principal
do {
    Show-Menu
    Write-Host "Escolha uma opção: " -ForegroundColor Yellow -NoNewline
    $opcao = Read-Host
    
    switch ($opcao) {
        "1" {
            .\compile.ps1
            Write-Host "`nPressione qualquer tecla para continuar..." -ForegroundColor Gray
            $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
        }
        "2" {
            .\setup-db.ps1
            Write-Host "`nPressione qualquer tecla para continuar..." -ForegroundColor Gray
            $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
        }
        "3" {
            .\run-console.ps1
            Write-Host "`nPressione qualquer tecla para continuar..." -ForegroundColor Gray
            $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
        }
        "4" {
            Write-Host "`nO servidor será iniciado. Use CTRL+C para encerrar.`n" -ForegroundColor Yellow
            Start-Sleep -Seconds 2
            .\run-web.ps1
        }
        "5" {
            .\clean.ps1
            Write-Host "Pressione qualquer tecla para continuar..." -ForegroundColor Gray
            $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
        }
        "6" {
            Show-Status
        }
        "7" {
            if (Test-Path "README.md") {
                code README.md
            } else {
                Write-Host "`n[ERRO] README.md não encontrado!" -ForegroundColor Red
                Start-Sleep -Seconds 2
            }
        }
        "0" {
            Write-Host "`nEncerrando...`n" -ForegroundColor Green
            exit
        }
        default {
            Write-Host "`n[ERRO] Opção inválida!`n" -ForegroundColor Red
            Start-Sleep -Seconds 1
        }
    }
} while ($true)
