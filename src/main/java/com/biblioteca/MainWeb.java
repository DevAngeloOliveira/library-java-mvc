package com.biblioteca;

import com.biblioteca.config.DatabaseConfig;
import com.biblioteca.controller.BibliotecaController;
import com.biblioteca.repository.ItemRepository;
import com.biblioteca.repository.ItemRepositorySQLite;
import com.biblioteca.service.BibliotecaService;
import com.biblioteca.web.BibliotecaServer;

/**
 * Classe Principal - Modo Web Server
 * Inicia o servidor HTTP na porta 8080
 */
public class MainWeb {
    public static void main(String[] args) {
        try {
            System.out.println("==============================================");
            System.out.println("  INICIANDO SERVIDOR WEB DA BIBLIOTECA");
            System.out.println("==============================================\n");
            
            // Verificar conexao com banco
            if (!DatabaseConfig.testarConexao()) {
                System.err.println("ERRO: Nao foi possivel conectar ao banco de dados!");
                System.err.println("Verifique se o arquivo biblioteca.db existe.");
                return;
            }
            
            System.out.println("[OK] Conectado ao SQLite");
            
            // Criar camada de servico e controller
            ItemRepository repository = new ItemRepositorySQLite();
            BibliotecaService service = new BibliotecaService(repository);
            BibliotecaController controller = new BibliotecaController(service);
            
            // Iniciar servidor web
            BibliotecaServer server = new BibliotecaServer(controller, 8080);
            server.iniciar();
            
            System.out.println("\nPressione CTRL+C para encerrar o servidor...");
            
            // Manter servidor rodando
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("Erro ao iniciar servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
