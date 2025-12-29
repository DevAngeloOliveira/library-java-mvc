package com.biblioteca;

import com.biblioteca.controller.AutenticacaoController;
import com.biblioteca.dto.*;
import com.biblioteca.exception.BibliotecaException;
import com.biblioteca.model.usuario.*;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.repository.UsuarioRepositoryImpl;
import com.biblioteca.service.AutenticacaoService;

import java.util.List;

/**
 * Demonstração do Sistema de Autenticação
 * Mostra hierarquia de usuários e controle de permissões
 */
public class MainAutenticacao {
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("  SISTEMA DE AUTENTICAÇÃO - HIERARQUIA DE USUÁRIOS");
        System.out.println("=".repeat(70));
        System.out.println();

        // Setup
        UsuarioRepository repository = new UsuarioRepositoryImpl();
        AutenticacaoService service = new AutenticacaoService(repository);
        AutenticacaoController controller = new AutenticacaoController(service);

        try {
            // ========== CRIAR USUÁRIOS INICIAIS ==========
            System.out.println(">>> CRIANDO USUÁRIOS NO SISTEMA <<<\n");

            // Criar Admin primeiro (sem autenticação para bootstrap)
            Usuario adminBootstrap = new Admin("ADM001", "João Silva", "admin@biblioteca.com", "admin123");
            repository.salvar(adminBootstrap);
            System.out.println("[OK] Admin criado: " + adminBootstrap.getEmail());

            // Login como Admin
            LoginDTO loginAdmin = new LoginDTO("admin@biblioteca.com", "admin123");
            LoginResponseDTO adminLogin = controller.login(loginAdmin);
            String tokenAdmin = adminLogin.getToken();
            System.out.println("[OK] Login Admin realizado");
            System.out.println("    Token: " + tokenAdmin.substring(0, 20) + "...");
            System.out.println("    Tipo: " + adminLogin.getUsuario().getDescricaoTipo());
            System.out.println();

            // Admin cria Bibliotecário
            UsuarioDTO bibliotecarioDTO = new UsuarioDTO();
            bibliotecarioDTO.setNome("Maria Santos");
            bibliotecarioDTO.setEmail("maria@biblioteca.com");
            bibliotecarioDTO.setSenha("maria123");
            bibliotecarioDTO.setTipo("BIBLIOTECARIO");
            controller.criarUsuario(bibliotecarioDTO, tokenAdmin);
            System.out.println("[OK] Bibliotecário criado pelo Admin");

            // Admin cria Usuário Comum
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setNome("Carlos Souza");
            usuarioDTO.setEmail("carlos@email.com");
            usuarioDTO.setSenha("carlos123");
            usuarioDTO.setTipo("USUARIO");
            controller.criarUsuario(usuarioDTO, tokenAdmin);
            System.out.println("[OK] Usuário Comum criado pelo Admin");
            System.out.println();

            // ========== DEMONSTRAR HIERARQUIA E PERMISSÕES ==========
            System.out.println("=".repeat(70));
            System.out.println(">>> DEMONSTRANDO HIERARQUIA E PERMISSÕES <<<\n");

            // Admin lista todos os usuários
            System.out.println("[ADMIN] Listando todos os usuários:");
            List<UsuarioResponseDTO> usuarios = controller.listarUsuarios(tokenAdmin);
            for (UsuarioResponseDTO u : usuarios) {
                System.out.println("  - " + u.getNome() + " (" + u.getTipo() + ") - " + u.getEmail());
            }
            System.out.println();

            // Login como Bibliotecário
            LoginDTO loginBibliotecario = new LoginDTO("maria@biblioteca.com", "maria123");
            LoginResponseDTO bibliotecarioLogin = controller.login(loginBibliotecario);
            String tokenBibliotecario = bibliotecarioLogin.getToken();
            System.out.println("[OK] Login Bibliotecário realizado");
            System.out.println("    Tipo: " + bibliotecarioLogin.getUsuario().getDescricaoTipo());
            System.out.println();

            // Bibliotecário tenta listar usuários
            System.out.println("[BIBLIOTECARIO] Listando usuários:");
            List<UsuarioResponseDTO> usuariosBib = controller.listarUsuarios(tokenBibliotecario);
            System.out.println("    Pode visualizar " + usuariosBib.size() + " usuários");
            System.out.println();

            // Bibliotecário tenta criar usuário (vai falhar)
            System.out.println("[BIBLIOTECARIO] Tentando criar novo usuário:");
            try {
                UsuarioDTO novoUsuario = new UsuarioDTO();
                novoUsuario.setNome("Pedro Lima");
                novoUsuario.setEmail("pedro@email.com");
                novoUsuario.setSenha("pedro123");
                novoUsuario.setTipo("USUARIO");
                controller.criarUsuario(novoUsuario, tokenBibliotecario);
            } catch (BibliotecaException e) {
                System.out.println("    [ERRO] " + e.getMessage());
            }
            System.out.println();

            // Login como Usuário Comum
            LoginDTO loginUsuario = new LoginDTO("carlos@email.com", "carlos123");
            LoginResponseDTO usuarioLogin = controller.login(loginUsuario);
            String tokenUsuario = usuarioLogin.getToken();
            System.out.println("[OK] Login Usuário Comum realizado");
            System.out.println("    Tipo: " + usuarioLogin.getUsuario().getDescricaoTipo());
            System.out.println();

            // Usuário tenta listar usuários (vai falhar)
            System.out.println("[USUARIO] Tentando listar usuários:");
            try {
                controller.listarUsuarios(tokenUsuario);
            } catch (BibliotecaException e) {
                System.out.println("    [ERRO] " + e.getMessage());
            }
            System.out.println();

            // ========== DEMONSTRAR POLIMORFISMO ==========
            System.out.println("=".repeat(70));
            System.out.println(">>> DEMONSTRANDO POLIMORFISMO (Permissões Dinâmicas) <<<\n");

            Usuario admin = repository.buscarPorEmail("admin@biblioteca.com").get();
            Usuario bibliotecario = repository.buscarPorEmail("maria@biblioteca.com").get();
            Usuario usuarioComum = repository.buscarPorEmail("carlos@email.com").get();

            System.out.println("Testando permissão CRIAR_USUARIO:");
            System.out.println("  Admin: " + admin.temPermissao(Permissao.CRIAR_USUARIO));
            System.out.println("  Bibliotecário: " + bibliotecario.temPermissao(Permissao.CRIAR_USUARIO));
            System.out.println("  Usuário: " + usuarioComum.temPermissao(Permissao.CRIAR_USUARIO));
            System.out.println();

            System.out.println("Testando permissão EMPRESTAR_QUALQUER_ITEM:");
            System.out.println("  Admin: " + admin.temPermissao(Permissao.EMPRESTAR_QUALQUER_ITEM));
            System.out.println("  Bibliotecário: " + bibliotecario.temPermissao(Permissao.EMPRESTAR_QUALQUER_ITEM));
            System.out.println("  Usuário: " + usuarioComum.temPermissao(Permissao.EMPRESTAR_QUALQUER_ITEM));
            System.out.println();

            System.out.println("Testando permissão EMPRESTAR_PROPRIO_ITEM:");
            System.out.println("  Admin: " + admin.temPermissao(Permissao.EMPRESTAR_PROPRIO_ITEM));
            System.out.println("  Bibliotecário: " + bibliotecario.temPermissao(Permissao.EMPRESTAR_PROPRIO_ITEM));
            System.out.println("  Usuário: " + usuarioComum.temPermissao(Permissao.EMPRESTAR_PROPRIO_ITEM));
            System.out.println();

            // ========== DEMONSTRAR ENCAPSULAMENTO ==========
            System.out.println("=".repeat(70));
            System.out.println(">>> DEMONSTRANDO ENCAPSULAMENTO <<<\n");

            if (usuarioComum instanceof UsuarioComum) {
                UsuarioComum uc = (UsuarioComum) usuarioComum;
                System.out.println("Usuário Comum - Limite de Empréstimos:");
                System.out.println("  Limite atual: " + uc.getLimiteEmprestimos());
                System.out.println("  Pode pegar mais livros (0 empréstimos): " + uc.podePegarMaisLivros(0));
                System.out.println("  Pode pegar mais livros (3 empréstimos): " + uc.podePegarMaisLivros(3));
                System.out.println("  Pode pegar mais livros (5 empréstimos): " + uc.podePegarMaisLivros(5));
            }
            System.out.println();

            // ========== LOGOUT ==========
            System.out.println("=".repeat(70));
            System.out.println(">>> ENCERRANDO SESSÕES <<<\n");

            controller.logout(tokenAdmin);
            System.out.println("[OK] Logout Admin");

            controller.logout(tokenBibliotecario);
            System.out.println("[OK] Logout Bibliotecário");

            controller.logout(tokenUsuario);
            System.out.println("[OK] Logout Usuário");

            System.out.println();
            System.out.println("=".repeat(70));
            System.out.println("  CONCEITOS DE POO DEMONSTRADOS:");
            System.out.println("  [✓] HERANÇA - Usuario → Admin, Bibliotecario, UsuarioComum");
            System.out.println("  [✓] POLIMORFISMO - temPermissao() implementado diferente em cada tipo");
            System.out.println("  [✓] ENCAPSULAMENTO - Atributos privados com getters/setters");
            System.out.println("  [✓] ABSTRAÇÃO - Classe abstrata Usuario com métodos abstratos");
            System.out.println("  [✓] MVC - Model (Usuario), Service, Controller separados");
            System.out.println("  [✓] Repository Pattern - UsuarioRepository abstrai persistência");
            System.out.println("  [✓] DTO Pattern - Transferência de dados entre camadas");
            System.out.println("  [✓] Exception Handling - Hierarquia de exceções customizadas");
            System.out.println("=".repeat(70));

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
