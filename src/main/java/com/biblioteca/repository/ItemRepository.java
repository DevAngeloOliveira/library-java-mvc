package com.biblioteca.repository;

import com.biblioteca.model.ItemBiblioteca;
import java.util.List;
import java.util.Optional;

/**
 * Interface Repository - padrão Repository
 * Define o contrato para persistência de dados
 */
public interface ItemRepository {
    void salvar(ItemBiblioteca item);
    Optional<ItemBiblioteca> buscarPorCodigo(String codigo);
    List<ItemBiblioteca> buscarTodos();
    List<ItemBiblioteca> buscarPorTipo(String tipo);
    List<ItemBiblioteca> buscarDisponiveis();
    List<ItemBiblioteca> buscarEmprestados();
    boolean remover(String codigo);
    void atualizar(ItemBiblioteca item);
}
