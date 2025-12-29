package com.biblioteca.repository;

import com.biblioteca.model.ItemBiblioteca;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementação do Repository usando armazenamento em memória
 * Simula um banco de dados para fins educacionais
 */
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<String, ItemBiblioteca> database;
    
    public ItemRepositoryImpl() {
        this.database = new HashMap<>();
    }
    
    @Override
    public void salvar(ItemBiblioteca item) {
        if (item == null || item.getCodigo() == null) {
            throw new IllegalArgumentException("Item ou codigo nao pode ser nulo");
        }
        database.put(item.getCodigo(), item);
    }
    
    @Override
    public Optional<ItemBiblioteca> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(database.get(codigo));
    }
    
    @Override
    public List<ItemBiblioteca> buscarTodos() {
        return new ArrayList<>(database.values());
    }
    
    @Override
    public List<ItemBiblioteca> buscarPorTipo(String tipo) {
        return database.values().stream()
            .filter(item -> item.getTipo().equalsIgnoreCase(tipo))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ItemBiblioteca> buscarDisponiveis() {
        return database.values().stream()
            .filter(item -> !item.isEmprestado())
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ItemBiblioteca> buscarEmprestados() {
        return database.values().stream()
            .filter(ItemBiblioteca::isEmprestado)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean remover(String codigo) {
        return database.remove(codigo) != null;
    }
    
    @Override
    public void atualizar(ItemBiblioteca item) {
        if (item == null || item.getCodigo() == null) {
            throw new IllegalArgumentException("Item ou codigo nao pode ser nulo");
        }
        if (!database.containsKey(item.getCodigo())) {
            throw new IllegalStateException("Item nao encontrado para atualizacao");
        }
        database.put(item.getCodigo(), item);
    }
}
