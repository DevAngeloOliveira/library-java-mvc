package com.biblioteca.controller;

import com.biblioteca.dto.ItemDTO;
import com.biblioteca.dto.ItemResponseDTO;
import com.biblioteca.exception.*;
import com.biblioteca.service.BibliotecaService;
import com.biblioteca.util.Logger;

import java.util.List;

/**
 * Controlador para operações de biblioteca.
 * Gerencia a comunicação entre a camada de apresentação e a camada de serviço.
 * 
 * @author Sistema de Biblioteca
 * @version 2.0
 */
public class BibliotecaController {
    private static final Logger logger = Logger.getLogger(BibliotecaController.class);
    private final BibliotecaService service;

    /**
     * Construtor do controlador.
     * 
     * @param service Serviço de biblioteca
     */
    public BibliotecaController(BibliotecaService service) {
        this.service = service;
        logger.debug("BibliotecaController inicializado");
    }

    /**
     * Lista todos os itens da biblioteca.
     * 
     * @return Lista de DTOs de resposta com informações dos itens
     */
    public List<ItemResponseDTO> listarTodos() {
        logger.debug("Controller: listarTodos()");
        try {
            return service.listarTodos();
        } catch (Exception e) {
            logger.error("Erro no controller ao listar itens: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Adiciona um novo item à biblioteca.
     * 
     * @param itemDTO DTO com dados do item
     * @throws ItemJaExisteException Se o item já existe
     * @throws ValidacaoException Se os dados são inválidos
     * @throws BibliotecaException Se houver erro na operação
     */
    public void adicionarItem(ItemDTO itemDTO) throws BibliotecaException {
        logger.debug("Controller: adicionarItem() - " + itemDTO.getTitulo());
        try {
            service.adicionarItem(itemDTO);
        } catch (BibliotecaException e) {
            logger.error("Erro no controller ao adicionar item: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado no controller: " + e.getMessage());
            throw new BibliotecaException("Erro ao adicionar item", e);
        }
    }

    /**
     * Empresta um item da biblioteca.
     * 
     * @param id ID do item
     * @throws ItemNaoEncontradoException Se o item não existe
     * @throws OperacaoInvalidaException Se o item já está emprestado
     * @throws BibliotecaException Se houver erro na operação
     */
    public void emprestarItem(String id) throws BibliotecaException {
        logger.debug("Controller: emprestarItem() - ID=" + id);
        try {
            service.emprestarItem(id);
        } catch (BibliotecaException e) {
            logger.error("Erro no controller ao emprestar item: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado no controller: " + e.getMessage());
            throw new BibliotecaException("Erro ao emprestar item", e);
        }
    }

    /**
     * Devolve um item à biblioteca.
     * 
     * @param id ID do item
     * @throws ItemNaoEncontradoException Se o item não existe
     * @throws OperacaoInvalidaException Se o item não está emprestado
     * @throws BibliotecaException Se houver erro na operação
     */
    public void devolverItem(String id) throws BibliotecaException {
        logger.debug("Controller: devolverItem() - ID=" + id);
        try {
            service.devolverItem(id);
        } catch (BibliotecaException e) {
            logger.error("Erro no controller ao devolver item: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado no controller: " + e.getMessage());
            throw new BibliotecaException("Erro ao devolver item", e);
        }
    }

    /**
     * Remove um item da biblioteca.
     * 
     * @param id ID do item
     * @throws ItemNaoEncontradoException Se o item não existe
     * @throws BibliotecaException Se houver erro na operação
     */
    public void removerItem(String id) throws BibliotecaException {
        logger.debug("Controller: removerItem() - ID=" + id);
        try {
            service.removerItem(id);
        } catch (BibliotecaException e) {
            logger.error("Erro no controller ao remover item: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado no controller: " + e.getMessage());
            throw new BibliotecaException("Erro ao remover item", e);
        }
    }

    /**
     * Busca um item por código.
     * 
     * @param codigo Código do item
     * @return DTO de resposta com informações do item
     * @throws ItemNaoEncontradoException Se o item não existe
     * @throws BibliotecaException Se houver erro na operação
     */
    public ItemResponseDTO buscarPorCodigo(String codigo) throws BibliotecaException {
        logger.debug("Controller: buscarPorCodigo() - Codigo=" + codigo);
        try {
            // Buscar na lista de todos os itens
            return listarTodos().stream()
                .filter(item -> item.getCodigo().equals(codigo))
                .findFirst()
                .orElseThrow(() -> new ItemNaoEncontradoException("Item com código " + codigo + " não encontrado"));
        } catch (BibliotecaException e) {
            logger.error("Erro no controller ao buscar item: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado no controller: " + e.getMessage());
            throw new BibliotecaException("Erro ao buscar item", e);
        }
    }
}
