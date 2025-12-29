package com.biblioteca.test;

import com.biblioteca.exception.*;
import com.biblioteca.model.*;
import com.biblioteca.repository.ItemRepository;
import com.biblioteca.repository.ItemRepositoryImpl;
import com.biblioteca.service.BibliotecaService;
import com.biblioteca.dto.ItemDTO;
import com.biblioteca.dto.ItemResponseDTO;

/**
 * Testes unitarios basicos para o sistema de biblioteca
 * Demonstra testes sem framework externo
 */
public class BibliotecaTest {
    
    private static int totalTestes = 0;
    private static int testesPassaram = 0;
    private static int testesFalharam = 0;
    
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("  EXECUTANDO TESTES UNITARIOS");
        System.out.println("==============================================\n");
        
        testarCriacaoLivro();
        testarEmprestimoItem();
        testarDevolucaoItem();
        testarItemNaoEncontrado();
        testarItemDuplicado();
        testarValidacaoCampos();
        
        System.out.println("\n==============================================");
        System.out.println("  RESULTADO DOS TESTES");
        System.out.println("==============================================");
        System.out.println("Total:    " + totalTestes);
        System.out.println("Passaram: " + testesPassaram + " ✓");
        System.out.println("Falharam: " + testesFalharam + " ✗");
        System.out.println("==============================================\n");
        
        if (testesFalharam > 0) {
            System.exit(1);
        }
    }
    
    private static void testarCriacaoLivro() {
        iniciarTeste("Criacao de Livro");
        try {
            Livro livro = new Livro("Java Efetivo", "LIV999", "Joshua Bloch", 416, "978-0134685991");
            
            assertEquals("Java Efetivo", livro.getTitulo(), "Titulo incorreto");
            assertEquals("LIV999", livro.getCodigo(), "Codigo incorreto");
            assertEquals("Joshua Bloch", livro.getAutor(), "Autor incorreto");
            assertEquals(416, livro.getNumeroPaginas(), "Numero de paginas incorreto");
            assertEquals(false, livro.isEmprestado(), "Livro nao deve estar emprestado inicialmente");
            
            testePassou();
        } catch (Exception e) {
            testeFalhou(e);
        }
    }
    
    private static void testarEmprestimoItem() {
        iniciarTeste("Emprestimo de Item");
        try {
            ItemRepository repository = new ItemRepositoryImpl();
            BibliotecaService service = new BibliotecaService(repository);
            
            // Adicionar item
            ItemDTO dto = criarLivroDTO("LIV777", "Test Book");
            service.adicionarItem(dto);
            
            // Emprestar
            ItemResponseDTO resultado = service.emprestarItem("LIV777");
            assertEquals(true, resultado.isEmprestado(), "Item deveria estar emprestado");
            
            testePassou();
        } catch (Exception e) {
            testeFalhou(e);
        }
    }
    
    private static void testarDevolucaoItem() {
        iniciarTeste("Devolucao de Item");
        try {
            ItemRepository repository = new ItemRepositoryImpl();
            BibliotecaService service = new BibliotecaService(repository);
            
            // Adicionar e emprestar
            ItemDTO dto = criarLivroDTO("LIV888", "Test Book 2");
            service.adicionarItem(dto);
            service.emprestarItem("LIV888");
            
            // Devolver
            ItemResponseDTO resultado = service.devolverItem("LIV888");
            assertEquals(false, resultado.isEmprestado(), "Item deveria estar disponivel");
            
            testePassou();
        } catch (Exception e) {
            testeFalhou(e);
        }
    }
    
    private static void testarItemNaoEncontrado() {
        iniciarTeste("Item Nao Encontrado - Deve lancar excecao");
        try {
            ItemRepository repository = new ItemRepositoryImpl();
            BibliotecaService service = new BibliotecaService(repository);
            
            try {
                service.emprestarItem("INVALIDO");
                testeFalhou(new Exception("Deveria ter lancado ItemNaoEncontradoException"));
            } catch (ItemNaoEncontradoException e) {
                // Esperado
                testePassou();
            }
        } catch (Exception e) {
            testeFalhou(e);
        }
    }
    
    private static void testarItemDuplicado() {
        iniciarTeste("Item Duplicado - Deve lancar excecao");
        try {
            ItemRepository repository = new ItemRepositoryImpl();
            BibliotecaService service = new BibliotecaService(repository);
            
            ItemDTO dto = criarLivroDTO("LIV555", "Duplicate Book");
            service.adicionarItem(dto);
            
            try {
                service.adicionarItem(dto);
                testeFalhou(new Exception("Deveria ter lancado ItemJaExisteException"));
            } catch (ItemJaExisteException e) {
                // Esperado
                testePassou();
            }
        } catch (Exception e) {
            testeFalhou(e);
        }
    }
    
    private static void testarValidacaoCampos() {
        iniciarTeste("Validacao de Campos - Deve lancar excecao");
        try {
            ItemRepository repository = new ItemRepositoryImpl();
            BibliotecaService service = new BibliotecaService(repository);
            
            ItemDTO dto = new ItemDTO();
            dto.setTipo("LIVRO");
            dto.setCodigo("LIV666");
            dto.setTitulo("");  // Titulo vazio - invalido
            
            try {
                service.adicionarItem(dto);
                testeFalhou(new Exception("Deveria ter lancado ValidacaoException"));
            } catch (ValidacaoException e) {
                // Esperado
                testePassou();
            }
        } catch (Exception e) {
            testeFalhou(e);
        }
    }
    
    // Metodos auxiliares
    
    private static ItemDTO criarLivroDTO(String codigo, String titulo) {
        ItemDTO dto = new ItemDTO();
        dto.setTipo("LIVRO");
        dto.setCodigo(codigo);
        dto.setTitulo(titulo);
        dto.setAutor("Autor Teste");
        dto.setNumeroPaginas(100);
        dto.setIsbn("123-456");
        return dto;
    }
    
    private static void iniciarTeste(String nome) {
        totalTestes++;
        System.out.print("[" + totalTestes + "] " + nome + "... ");
    }
    
    private static void testePassou() {
        testesPassaram++;
        System.out.println("OK ✓");
    }
    
    private static void testeFalhou(Exception e) {
        testesFalharam++;
        System.out.println("FALHOU ✗");
        System.out.println("    Erro: " + e.getMessage());
    }
    
    private static void assertEquals(Object esperado, Object obtido, String mensagem) {
        if (esperado == null && obtido == null) return;
        if (esperado != null && !esperado.equals(obtido)) {
            throw new RuntimeException(mensagem + " - Esperado: " + esperado + ", Obtido: " + obtido);
        }
    }
}
