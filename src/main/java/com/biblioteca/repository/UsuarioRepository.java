package com.biblioteca.repository;

import com.biblioteca.model.usuario.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Interface do repositório de usuários
 * Segue o Repository Pattern
 */
public interface UsuarioRepository {
    void salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(String id);
    Optional<Usuario> buscarPorEmail(String email);
    List<Usuario> listarTodos();
    void atualizar(Usuario usuario);
    void remover(String id);
    boolean existeEmail(String email);
}
