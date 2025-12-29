package com.biblioteca.repository;

import com.biblioteca.model.usuario.Usuario;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementação em memória do repositório de usuários
 */
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();

    @Override
    public void salvar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.values().stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email))
            .findFirst();
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios.values());
    }

    @Override
    public void atualizar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    @Override
    public void remover(String id) {
        usuarios.remove(id);
    }

    @Override
    public boolean existeEmail(String email) {
        return usuarios.values().stream()
            .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
