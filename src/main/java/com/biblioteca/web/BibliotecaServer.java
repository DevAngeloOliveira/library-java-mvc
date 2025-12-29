package com.biblioteca.web;

import com.biblioteca.controller.AutenticacaoController;
import com.biblioteca.controller.BibliotecaController;
import com.biblioteca.dto.ItemDTO;
import com.biblioteca.dto.ItemResponseDTO;
import com.biblioteca.dto.LoginDTO;
import com.biblioteca.dto.LoginResponseDTO;
import com.biblioteca.exception.*;
import com.biblioteca.model.ItemBiblioteca;
import com.biblioteca.model.usuario.Usuario;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.repository.UsuarioRepositoryImpl;
import com.biblioteca.service.AutenticacaoService;
import com.biblioteca.util.Logger;
import com.biblioteca.web.response.ApiResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servidor HTTP para a aplicação de biblioteca.
 * Fornece uma API REST para gerenciamento de itens.
 * 
 * @author Sistema de Biblioteca
 * @version 2.0
 */
public class BibliotecaServer {
    private static final Logger logger = Logger.getLogger(BibliotecaServer.class);
    private final HttpServer server;
    private final BibliotecaController controller;
    private final AutenticacaoController autenticacaoController;
    private final int porta;

    /**
     * Construtor do servidor.
     * 
     * @param controller Controlador de biblioteca
     * @param porta Porta do servidor
     * @throws IOException Se houver erro ao criar servidor
     */
    public BibliotecaServer(BibliotecaController controller, int porta) throws IOException {
        this.controller = controller;
        this.porta = porta;
        
        // Inicializar controller de autenticação
        UsuarioRepository usuarioRepo = new UsuarioRepositoryImpl();
        AutenticacaoService autenticacaoService = new AutenticacaoService(usuarioRepo);
        this.autenticacaoController = new AutenticacaoController(autenticacaoService);
        
        this.server = HttpServer.create(new InetSocketAddress(porta), 0);
        configurarRotas();
        logger.info("Servidor HTTP criado na porta " + porta);
    }

    /**
     * Configura as rotas da API REST.
     */
    private void configurarRotas() {
        // Health check endpoint
        server.createContext("/health", new HealthCheckHandler());
        
        // Recursos estáticos
        server.createContext("/", new StaticFileHandler());
        
        // API REST endpoints - Autenticação
        server.createContext("/api/auth/login", new LoginHandler());
        server.createContext("/api/auth/logout", new LogoutHandler());
        server.createContext("/api/auth/me", new MeHandler());
        
        // API REST endpoints - Itens
        server.createContext("/api/itens", new ListarItensHandler());
        server.createContext("/api/item/adicionar", new AdicionarItemHandler());
        server.createContext("/api/item/emprestar", new EmprestarItemHandler());
        server.createContext("/api/item/devolver", new DevolverItemHandler());
        server.createContext("/api/item/remover", new RemoverItemHandler());
        
        logger.debug("Rotas configuradas com sucesso");
    }

    /**
     * Inicia o servidor.
     */
    public void iniciar() {
        server.setExecutor(null);
        server.start();
        logger.info("=".repeat(60));
        logger.info("Servidor iniciado com sucesso!");
        logger.info("Acesse: http://localhost:" + porta);
        logger.info("Health Check: http://localhost:" + porta + "/health");
        logger.info("API Base: http://localhost:" + porta + "/api");
        logger.info("=".repeat(60));
    }

    /**
     * Para o servidor.
     */
    public void parar() {
        server.stop(0);
        logger.info("Servidor parado");
    }

    /**
     * Handler para verificação de saúde do servidor.
     */
    class HealthCheckHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            logger.debug("Health check requisitado");
            
            Map<String, Object> healthData = new HashMap<>();
            healthData.put("status", "UP");
            healthData.put("service", "Biblioteca API");
            healthData.put("version", "2.0");
            healthData.put("timestamp", System.currentTimeMillis());
            
            ApiResponse<Map<String, Object>> response = ApiResponse.sucesso(healthData, "Serviço operacional");
            enviarResposta(exchange, 200, response.toJSON());
        }
    }

    /**
     * Handler para listar todos os itens.
     */
    class ListarItensHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            habilitarCORS(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                enviarResposta(exchange, 204, "");
                return;
            }

            try {
                logger.info("Listando todos os itens");
                List<ItemResponseDTO> itens = controller.listarTodos();
                logger.info("Retornados " + itens.size() + " itens do banco");
                ApiResponse<List<ItemResponseDTO>> response = ApiResponse.sucesso(itens, "Itens recuperados com sucesso");
                String json = response.toJSON();
                logger.info("JSON gerado com " + json.length() + " caracteres");
                enviarResposta(exchange, 200, json);
            } catch (Exception e) {
                logger.error("Erro ao listar itens: " + e.getMessage());
                e.printStackTrace();
                ApiResponse<Void> response = ApiResponse.erro("Erro ao listar itens: " + e.getMessage());
                enviarResposta(exchange, 500, response.toJSON());
            }
        }
    }

    /**
     * Handler para adicionar um novo item.
     */
    class AdicionarItemHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            habilitarCORS(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                enviarResposta(exchange, 204, "");
                return;
            }

            if (!"POST".equals(exchange.getRequestMethod())) {
                ApiResponse<Void> response = ApiResponse.erro("Método não permitido. Use POST");
                enviarResposta(exchange, 405, response.toJSON());
                return;
            }

            try {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                logger.debug("Corpo da requisição: " + body);
                
                ItemDTO itemDTO = parseItemDTO(body);
                controller.adicionarItem(itemDTO);
                
                logger.info("Item adicionado: " + itemDTO.getTitulo());
                ApiResponse<Void> response = ApiResponse.sucesso(null, "Item adicionado com sucesso");
                enviarResposta(exchange, 201, response.toJSON());
            } catch (ItemJaExisteException e) {
                logger.warn("Tentativa de adicionar item duplicado: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro(e.getMessage());
                enviarResposta(exchange, 409, response.toJSON());
            } catch (ValidacaoException e) {
                logger.warn("Validação falhou: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro(e.getMessage());
                enviarResposta(exchange, 400, response.toJSON());
            } catch (Exception e) {
                logger.error("Erro ao adicionar item: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro("Erro ao adicionar item: " + e.getMessage());
                enviarResposta(exchange, 500, response.toJSON());
            }
        }
    }

    /**
     * Handler para emprestar um item.
     */
    class EmprestarItemHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            habilitarCORS(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                enviarResposta(exchange, 204, "");
                return;
            }

            if (!"POST".equals(exchange.getRequestMethod())) {
                ApiResponse<Void> response = ApiResponse.erro("Método não permitido. Use POST");
                enviarResposta(exchange, 405, response.toJSON());
                return;
            }

            try {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String id = extrairCampo(body, "id");
                
                controller.emprestarItem(id);
                logger.info("Item emprestado: ID=" + id);
                
                ApiResponse<Void> response = ApiResponse.sucesso(null, "Item emprestado com sucesso");
                enviarResposta(exchange, 200, response.toJSON());
            } catch (ItemNaoEncontradoException e) {
                logger.warn("Item não encontrado para empréstimo: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro(e.getMessage());
                enviarResposta(exchange, 404, response.toJSON());
            } catch (OperacaoInvalidaException e) {
                logger.warn("Operação de empréstimo inválida: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro(e.getMessage());
                enviarResposta(exchange, 400, response.toJSON());
            } catch (Exception e) {
                logger.error("Erro ao emprestar item: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro("Erro ao emprestar item: " + e.getMessage());
                enviarResposta(exchange, 500, response.toJSON());
            }
        }
    }

    /**
     * Handler para devolver um item.
     */
    class DevolverItemHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            habilitarCORS(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                enviarResposta(exchange, 204, "");
                return;
            }

            if (!"POST".equals(exchange.getRequestMethod())) {
                ApiResponse<Void> response = ApiResponse.erro("Método não permitido. Use POST");
                enviarResposta(exchange, 405, response.toJSON());
                return;
            }

            try {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String id = extrairCampo(body, "id");
                
                controller.devolverItem(id);
                logger.info("Item devolvido: ID=" + id);
                
                ApiResponse<Void> response = ApiResponse.sucesso(null, "Item devolvido com sucesso");
                enviarResposta(exchange, 200, response.toJSON());
            } catch (ItemNaoEncontradoException e) {
                logger.warn("Item não encontrado para devolução: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro(e.getMessage());
                enviarResposta(exchange, 404, response.toJSON());
            } catch (OperacaoInvalidaException e) {
                logger.warn("Operação de devolução inválida: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro(e.getMessage());
                enviarResposta(exchange, 400, response.toJSON());
            } catch (Exception e) {
                logger.error("Erro ao devolver item: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro("Erro ao devolver item: " + e.getMessage());
                enviarResposta(exchange, 500, response.toJSON());
            }
        }
    }

    /**
     * Handler para remover um item.
     */
    class RemoverItemHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            habilitarCORS(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                enviarResposta(exchange, 204, "");
                return;
            }

            if (!"POST".equals(exchange.getRequestMethod())) {
                ApiResponse<Void> response = ApiResponse.erro("Método não permitido. Use POST");
                enviarResposta(exchange, 405, response.toJSON());
                return;
            }

            try {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String id = extrairCampo(body, "id");
                
                controller.removerItem(id);
                logger.info("Item removido: ID=" + id);
                
                ApiResponse<Void> response = ApiResponse.sucesso(null, "Item removido com sucesso");
                enviarResposta(exchange, 200, response.toJSON());
            } catch (ItemNaoEncontradoException e) {
                logger.warn("Item não encontrado para remoção: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro(e.getMessage());
                enviarResposta(exchange, 404, response.toJSON());
            } catch (Exception e) {
                logger.error("Erro ao remover item: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro("Erro ao remover item: " + e.getMessage());
                enviarResposta(exchange, 500, response.toJSON());
            }
        }
    }

    /**
     * Handler para servir arquivos estáticos.
     */
    class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) {
                path = "/index.html";
            }

            Path filePath = Paths.get("src/main/resources/web" + path);
            
            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                byte[] content = Files.readAllBytes(filePath);
                String contentType = determinarContentType(path);
                
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.sendResponseHeaders(200, content.length);
                
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(content);
                }
                logger.debug("Arquivo servido: " + path);
            } else {
                String response = "404 - Arquivo não encontrado";
                exchange.sendResponseHeaders(404, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                logger.warn("Arquivo não encontrado: " + path);
            }
        }
    }

    /**
     * Habilita CORS para permitir requisições do frontend.
     */
    private void habilitarCORS(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    /**
     * Envia resposta HTTP.
     */
    private void enviarResposta(HttpExchange exchange, int statusCode, String resposta) throws IOException {
        byte[] bytes = resposta.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    /**
     * Handler para login
     */
    class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            habilitarCORS(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                enviarResposta(exchange, 204, "");
                return;
            }

            if (!"POST".equals(exchange.getRequestMethod())) {
                ApiResponse<Void> response = ApiResponse.erro("Método não permitido. Use POST");
                enviarResposta(exchange, 405, response.toJSON());
                return;
            }

            try {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                logger.debug("Login requisitado");
                
                String email = extrairCampo(body, "email");
                String senha = extrairCampo(body, "senha");
                
                LoginDTO loginDTO = new LoginDTO(email, senha);
                LoginResponseDTO resultado = autenticacaoController.login(loginDTO);
                
                logger.info("Login realizado com sucesso: " + email);
                ApiResponse<LoginResponseDTO> response = ApiResponse.sucesso(resultado, "Login realizado com sucesso");
                enviarResposta(exchange, 200, response.toJSON());
            } catch (AutenticacaoException e) {
                logger.warn("Falha no login: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro(e.getMessage());
                enviarResposta(exchange, 401, response.toJSON());
            } catch (Exception e) {
                logger.error("Erro ao fazer login: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro("Erro ao fazer login: " + e.getMessage());
                enviarResposta(exchange, 500, response.toJSON());
            }
        }
    }

    /**
     * Handler para logout
     */
    class LogoutHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            habilitarCORS(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                enviarResposta(exchange, 204, "");
                return;
            }

            if (!"POST".equals(exchange.getRequestMethod())) {
                ApiResponse<Void> response = ApiResponse.erro("Método não permitido. Use POST");
                enviarResposta(exchange, 405, response.toJSON());
                return;
            }

            try {
                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    autenticacaoController.logout(token);
                    logger.info("Logout realizado com sucesso");
                }
                
                ApiResponse<Void> response = ApiResponse.sucesso(null, "Logout realizado com sucesso");
                enviarResposta(exchange, 200, response.toJSON());
            } catch (Exception e) {
                logger.error("Erro ao fazer logout: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro("Erro ao fazer logout: " + e.getMessage());
                enviarResposta(exchange, 500, response.toJSON());
            }
        }
    }

    /**
     * Handler para obter dados do usuário logado
     */
    class MeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            habilitarCORS(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                enviarResposta(exchange, 204, "");
                return;
            }

            try {
                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    ApiResponse<Void> response = ApiResponse.erro("Token não fornecido");
                    enviarResposta(exchange, 401, response.toJSON());
                    return;
                }
                
                String token = authHeader.substring(7);
                Usuario usuario = autenticacaoController.validarToken(token);
                
                // Criar response DTO manualmente
                Map<String, Object> usuarioData = new HashMap<>();
                usuarioData.put("nome", usuario.getNome());
                usuarioData.put("email", usuario.getEmail());
                usuarioData.put("tipo", usuario.getTipo().name());
                
                ApiResponse<Map<String, Object>> response = ApiResponse.sucesso(usuarioData, "Usuário recuperado com sucesso");
                enviarResposta(exchange, 200, response.toJSON());
            } catch (AutenticacaoException e) {
                logger.warn("Token inválido: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro(e.getMessage());
                enviarResposta(exchange, 401, response.toJSON());
            } catch (Exception e) {
                logger.error("Erro ao obter usuário: " + e.getMessage());
                ApiResponse<Void> response = ApiResponse.erro("Erro ao obter usuário: " + e.getMessage());
                enviarResposta(exchange, 500, response.toJSON());
            }
        }
    }

    /**
     * Determina o tipo de conteúdo baseado na extensão do arquivo.
     */
    private String determinarContentType(String path) {
        if (path.endsWith(".html")) return "text/html; charset=UTF-8";
        if (path.endsWith(".css")) return "text/css; charset=UTF-8";
        if (path.endsWith(".js")) return "application/javascript; charset=UTF-8";
        if (path.endsWith(".json")) return "application/json; charset=UTF-8";
        return "text/plain; charset=UTF-8";
    }

    /**
     * Extrai um campo do JSON.
     */
    private String extrairCampo(String json, String campo) {
        String chave = "\"" + campo + "\"";
        int inicio = json.indexOf(chave);
        if (inicio == -1) return null;
        
        inicio = json.indexOf(":", inicio) + 1;
        int fim = json.indexOf(",", inicio);
        if (fim == -1) fim = json.indexOf("}", inicio);
        
        String valor = json.substring(inicio, fim).trim();
        return valor.replaceAll("\"", "").trim();
    }

    /**
     * Faz parse de um ItemDTO a partir de JSON.
     */
    private ItemDTO parseItemDTO(String json) {
        String id = extrairCampo(json, "id");
        String titulo = extrairCampo(json, "titulo");
        String tipo = extrairCampo(json, "tipo");
        String autor = extrairCampo(json, "autor");
        String editora = extrairCampo(json, "editora");
        String edicaoStr = extrairCampo(json, "edicao");
        String diretor = extrairCampo(json, "diretor");
        String duracaoStr = extrairCampo(json, "duracao");

        Integer edicao = (edicaoStr != null && !edicaoStr.isEmpty()) ? Integer.parseInt(edicaoStr) : null;
        Integer duracao = (duracaoStr != null && !duracaoStr.isEmpty()) ? Integer.parseInt(duracaoStr) : null;

        ItemDTO dto = new ItemDTO();
        dto.setCodigo(id);
        dto.setTitulo(titulo);
        dto.setTipo(tipo);
        dto.setAutor(autor);
        dto.setEditora(editora);
        dto.setEdicao(edicao);
        dto.setDiretor(diretor);
        dto.setDuracaoMinutos(duracao);
        
        return dto;
    }
}
