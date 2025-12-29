package com.biblioteca;

import com.biblioteca.config.DatabaseConfig;
import com.biblioteca.controller.BibliotecaController;
import com.biblioteca.dto.ItemDTO;
import com.biblioteca.dto.ItemResponseDTO;
import com.biblioteca.exception.BibliotecaException;
import com.biblioteca.repository.ItemRepository;
import com.biblioteca.repository.ItemRepositoryImpl;
import com.biblioteca.repository.ItemRepositorySQLite;
import com.biblioteca.service.BibliotecaService;
import com.biblioteca.util.Logger;
import com.biblioteca.view.BibliotecaView;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe Principal - Demonstra a Arquitetura MVC completa
 * 
 * ARQUITETURA MVC:
 * - Model: Classes de domínio (Livro, Revista, DVD, ItemBiblioteca)
 * - View: BibliotecaView (apresentação)
 * - Controller: BibliotecaController (coordenação)
 * 
 * PADRÕES DE PROJETO:
 * - Repository Pattern: Abstração de persistência
 * - Service Layer: Lógica de negócio
 * - DTO Pattern: Transferência de dados entre camadas
 * 
 * PERSISTÊNCIA:
 * - Suporta memória (ItemRepositoryImpl) ou SQLite (ItemRepositorySQLite)
 * - Configure USE_DATABASE = true para usar SQLite
 */
public class Main {
    // Configuração: true = SQLite, false = Memória
    private static final boolean USE_DATABASE = true;
    
    public static void main(String[] args) {
        BibliotecaView view = new BibliotecaView();
        
        // Escolher implementação do repositório
        ItemRepository repository;
        if (USE_DATABASE) {
            // Testar conexão com banco
            if (!DatabaseConfig.testarConexao()) {
                view.exibirErro("Nao foi possivel conectar ao SQLite!");
                view.exibirMensagem("Verifique o arquivo database.properties");
                view.exibirMensagem("Usando repositorio em memoria...\n");
                repository = new ItemRepositoryImpl();
            } else {
                view.exibirSucesso("Conectado ao SQLite com sucesso!");
                repository = new ItemRepositorySQLite();
            }
        } else {
            view.exibirMensagem("Usando repositorio em memoria\n");
            repository = new ItemRepositoryImpl();
        }
        
        // Injeção de Dependências (DI) manual
        BibliotecaService service = new BibliotecaService(repository);
        BibliotecaController controller = new BibliotecaController(service);
        
        // Exibe cabeçalho
        view.exibirCabecalho();
        
        // ========== ADICIONAR ITENS ==========
        view.exibirTitulo("ADICIONANDO ITENS AO ACERVO");
        
        try {
            // Adicionar Livro
            ItemDTO livroDTO = new ItemDTO();
            livroDTO.setTipo("LIVRO");
            livroDTO.setTitulo("Clean Code");
            livroDTO.setCodigo("LIV001");
            livroDTO.setAutor("Robert C. Martin");
            controller.adicionarItem(livroDTO);
            view.exibirSucesso("Item adicionado: " + livroDTO.getTitulo());
            
            // Adicionar outro Livro
            ItemDTO livroDTO2 = new ItemDTO();
            livroDTO2.setTipo("LIVRO");
            livroDTO2.setTitulo("Design Patterns");
            livroDTO2.setCodigo("LIV002");
            livroDTO2.setAutor("Gang of Four");
            controller.adicionarItem(livroDTO2);
            view.exibirSucesso("Item adicionado: " + livroDTO2.getTitulo());
            
            // Adicionar Revista
            ItemDTO revistaDTO = new ItemDTO();
            revistaDTO.setTipo("REVISTA");
            revistaDTO.setTitulo("National Geographic");
            revistaDTO.setCodigo("REV001");
            revistaDTO.setEdicao(145);
            revistaDTO.setEditora("National Geographic Partners");
            controller.adicionarItem(revistaDTO);
            view.exibirSucesso("Item adicionado: " + revistaDTO.getTitulo());
            
            // Adicionar DVD
            ItemDTO dvdDTO = new ItemDTO();
            dvdDTO.setTipo("DVD");
            dvdDTO.setTitulo("Matrix");
            dvdDTO.setCodigo("DVD001");
            dvdDTO.setDiretor("Wachowski Brothers");
            dvdDTO.setDuracaoMinutos(136);
            controller.adicionarItem(dvdDTO);
            view.exibirSucesso("Item adicionado: " + dvdDTO.getTitulo());
        } catch (BibliotecaException e) {
            view.exibirErro("Erro ao adicionar item: " + e.getMessage());
        }
        
        // ========== LISTAR TODOS OS ITENS ==========
        view.exibirTitulo("ACERVO COMPLETO DA BIBLIOTECA");
        List<ItemResponseDTO> todosItens = controller.listarTodos();
        view.exibirListaItens(todosItens);
        
        // ========== OPERAÇÕES DE EMPRÉSTIMO ==========
        view.exibirTitulo("OPERACOES DE EMPRESTIMO");
        
        try {
            controller.emprestarItem("LIV001");
            view.exibirMensagem("Item LIV001 emprestado com sucesso");
            
            controller.emprestarItem("DVD001");
            view.exibirMensagem("Item DVD001 emprestado com sucesso");
            
            controller.emprestarItem("REV001");
            view.exibirMensagem("Item REV001 emprestado com sucesso");
            
            // Tentando emprestar item já emprestado
            view.exibirLinha();
            view.exibirMensagem("Tentando emprestar novamente:");
            try {
                controller.emprestarItem("LIV001");
            } catch (BibliotecaException e) {
                view.exibirErro(e.getMessage());
            }
        } catch (BibliotecaException e) {
            view.exibirErro("Erro ao emprestar: " + e.getMessage());
        }
        
        // ========== LISTAR ITENS DISPONÍVEIS E EMPRESTADOS ==========
        view.exibirTitulo("ITENS DISPONIVEIS");
        List<ItemResponseDTO> disponiveis = controller.listarTodos()
            .stream()
            .filter(item -> !item.isEmprestado())
            .collect(Collectors.toList());
        view.exibirListaResumida(disponiveis);
        
        view.exibirTitulo("ITENS EMPRESTADOS");
        List<ItemResponseDTO> emprestados = controller.listarTodos()
            .stream()
            .filter(ItemResponseDTO::isEmprestado)
            .collect(Collectors.toList());
        view.exibirListaResumida(emprestados);
        
        // ========== OPERAÇÕES DE DEVOLUÇÃO ==========
        view.exibirTitulo("OPERACOES DE DEVOLUCAO");
        try {
            controller.devolverItem("LIV001");
            view.exibirMensagem("Item LIV001 devolvido com sucesso");
        } catch (BibliotecaException e) {
            view.exibirErro("Erro ao devolver: " + e.getMessage());
        }
        
        // ========== STATUS FINAL DO ACERVO ==========
        view.exibirTitulo("STATUS FINAL DO ACERVO");
        List<ItemResponseDTO> statusFinal = controller.listarTodos();
        view.exibirListaResumida(statusFinal);
        
        // ========== EXIBIR CONCEITOS ==========
        view.exibirConceitosPOO();
        
        // Fechar conexões se estiver usando banco de dados
        if (USE_DATABASE) {
            DatabaseConfig.close();
            view.exibirMensagem("\nConexao com banco de dados encerrada.");
        }
    }
}
