package com.biblioteca.service;

import com.biblioteca.dto.ItemDTO;
import com.biblioteca.dto.ItemResponseDTO;
import com.biblioteca.exception.*;
import com.biblioteca.model.*;
import com.biblioteca.repository.ItemRepository;
import com.biblioteca.util.Logger;
import com.biblioteca.util.Validador;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service - Camada de Lógica de Negócio
 * Contém as regras de negócio e validações da aplicação
 * 
 * @author Sistema de Biblioteca
 * @version 1.0
 */
public class BibliotecaService {
    private static final Logger logger = Logger.getLogger(BibliotecaService.class);
    private final ItemRepository repository;
    
    public BibliotecaService(ItemRepository repository) {
        this.repository = repository;
        logger.info("BibliotecaService inicializado");
    }
    
    /**
     * Adiciona um novo item ao acervo
     * @param dto Dados do item a ser adicionado
     * @return ItemResponseDTO com os dados do item adicionado
     * @throws ValidacaoException se os dados forem invalidos
     * @throws ItemJaExisteException se o codigo ja existir
     */
    public ItemResponseDTO adicionarItem(ItemDTO dto) {
        logger.debug("Adicionando item: " + dto.getCodigo());
        
        // Validar dados
        validarItemDTO(dto);
        
        ItemBiblioteca item = criarItemDoDTO(dto);
        
        // Valida se já existe item com o mesmo código
        Optional<ItemBiblioteca> existente = repository.buscarPorCodigo(dto.getCodigo());
        if (existente.isPresent()) {
            logger.warn("Tentativa de adicionar item duplicado: " + dto.getCodigo());
            throw new ItemJaExisteException(dto.getCodigo());
        }
        
        repository.salvar(item);
        logger.info("Item adicionado com sucesso: " + dto.getCodigo());
        return converterParaResponseDTO(item);
    }
    
    /**
     * Empresta um item do acervo
     * @param codigo Codigo do item a ser emprestado
     * @return ItemResponseDTO com os dados atualizados do item
     * @throws ItemNaoEncontradoException se o item nao existir
     * @throws OperacaoInvalidaException se o item ja estiver emprestado
     */
    public ItemResponseDTO emprestarItem(String codigo) {
        logger.debug("Tentando emprestar item: " + codigo);
        Validador.validarNaoVazio(codigo, "codigo");
        
        ItemBiblioteca item = repository.buscarPorCodigo(codigo)
            .orElseThrow(() -> new ItemNaoEncontradoException(codigo));
        
        if (item.isEmprestado()) {
            logger.warn("Tentativa de emprestar item ja emprestado: " + codigo);
            throw new OperacaoInvalidaException("Item ja esta emprestado");
        }
        
        item.setEmprestado(true);
        repository.atualizar(item);
        logger.info("Item emprestado com sucesso: " + codigo);
        return converterParaResponseDTO(item);
    }
    
    /**
     * Devolve um item emprestado
     * @param codigo Codigo do item a ser devolvido
     * @return ItemResponseDTO com os dados atualizados do item
     * @throws ItemNaoEncontradoException se o item nao existir
     * @throws OperacaoInvalidaException se o item nao estiver emprestado
     */
    public ItemResponseDTO devolverItem(String codigo) {
        logger.debug("Tentando devolver item: " + codigo);
        Validador.validarNaoVazio(codigo, "codigo");
        
        ItemBiblioteca item = repository.buscarPorCodigo(codigo)
            .orElseThrow(() -> new ItemNaoEncontradoException(codigo));
        
        if (!item.isEmprestado()) {
            logger.warn("Tentativa de devolver item nao emprestado: " + codigo);
            throw new OperacaoInvalidaException("Item nao esta emprestado");
        }
        
        item.setEmprestado(false);
        repository.atualizar(item);
        logger.info("Item devolvido com sucesso: " + codigo);
        return converterParaResponseDTO(item);
    }
    
    /**
     * Busca um item por código
     * @param codigo Codigo do item
     * @return Optional contendo o item se encontrado
     */
    public Optional<ItemResponseDTO> buscarPorCodigo(String codigo) {
        return repository.buscarPorCodigo(codigo)
            .map(this::converterParaResponseDTO);
    }
    
    /**
     * Lista todos os itens do acervo
     * @return Lista de todos os itens
     */
    public List<ItemResponseDTO> listarTodos() {
        return repository.buscarTodos().stream()
            .map(this::converterParaResponseDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista itens disponíveis
     * @return Lista de itens disponiveis para emprestimo
     */
    public List<ItemResponseDTO> listarDisponiveis() {
        return repository.buscarDisponiveis().stream()
            .map(this::converterParaResponseDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista itens emprestados
     * @return Lista de itens atualmente emprestados
     */
    public List<ItemResponseDTO> listarEmprestados() {
        return repository.buscarEmprestados().stream()
            .map(this::converterParaResponseDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Remove um item do acervo
     * @param codigo Codigo do item a ser removido
     * @return true se removido com sucesso
     */
    public boolean removerItem(String codigo) {
        return repository.remover(codigo);
    }
    
    // Métodos auxiliares privados
    
    /**
     * Valida os dados do DTO antes de criar o item
     */
    private void validarItemDTO(ItemDTO dto) {
        Validador.validarNaoVazio(dto.getCodigo(), "codigo");
        Validador.validarNaoVazio(dto.getTitulo(), "titulo");
        Validador.validarNaoVazio(dto.getTipo(), "tipo");
        
        switch (dto.getTipo().toUpperCase()) {
            case "LIVRO":
                Validador.validarNaoVazio(dto.getAutor(), "autor");
                Validador.validarPositivo(dto.getNumeroPaginas(), "numeroPaginas");
                break;
            case "REVISTA":
                Validador.validarPositivo(dto.getEdicao(), "edicao");
                Validador.validarNaoVazio(dto.getMesAno(), "mesAno");
                break;
            case "DVD":
                Validador.validarNaoVazio(dto.getDiretor(), "diretor");
                Validador.validarPositivo(dto.getDuracaoMinutos(), "duracaoMinutos");
                break;
            default:
                throw new ValidacaoException("tipo", "deve ser LIVRO, REVISTA ou DVD");
        }
    }
    
    private ItemBiblioteca criarItemDoDTO(ItemDTO dto) {
        switch (dto.getTipo().toUpperCase()) {
            case "LIVRO":
                return new Livro(
                    dto.getTitulo(),
                    dto.getCodigo(),
                    dto.getAutor(),
                    dto.getNumeroPaginas(),
                    dto.getIsbn()
                );
            case "REVISTA":
                return new Revista(
                    dto.getTitulo(),
                    dto.getCodigo(),
                    dto.getEdicao(),
                    dto.getMesAno(),
                    dto.getEditora()
                );
            case "DVD":
                return new DVD(
                    dto.getTitulo(),
                    dto.getCodigo(),
                    dto.getDiretor(),
                    dto.getDuracaoMinutos(),
                    dto.getGenero()
                );
            default:
                throw new ValidacaoException("tipo", "Tipo de item invalido: " + dto.getTipo());
        }
    }
    
    private ItemResponseDTO converterParaResponseDTO(ItemBiblioteca item) {
        String detalhes = construirDetalhes(item);
        return new ItemResponseDTO(
            item.getTipo(),
            item.getTitulo(),
            item.getCodigo(),
            item.isEmprestado(),
            detalhes
        );
    }
    
    private String construirDetalhes(ItemBiblioteca item) {
        StringBuilder detalhes = new StringBuilder();
        
        if (item instanceof Livro) {
            Livro livro = (Livro) item;
            detalhes.append("Autor: ").append(livro.getAutor());
            detalhes.append(", Paginas: ").append(livro.getNumeroPaginas());
            if (livro.getIsbn() != null && !livro.getIsbn().isEmpty()) {
                detalhes.append(", ISBN: ").append(livro.getIsbn());
            }
        } else if (item instanceof Revista) {
            Revista revista = (Revista) item;
            detalhes.append("Edicao: ").append(revista.getEdicao());
            detalhes.append(", Mes/Ano: ").append(revista.getMesAno());
            if (revista.getEditora() != null && !revista.getEditora().isEmpty()) {
                detalhes.append(", Editora: ").append(revista.getEditora());
            }
        } else if (item instanceof DVD) {
            DVD dvd = (DVD) item;
            detalhes.append("Diretor: ").append(dvd.getDiretor());
            detalhes.append(", Duracao: ").append(dvd.getDuracaoMinutos()).append(" min");
            if (dvd.getGenero() != null && !dvd.getGenero().isEmpty()) {
                detalhes.append(", Genero: ").append(dvd.getGenero());
            }
        }
        
        return detalhes.toString();
    }
}
