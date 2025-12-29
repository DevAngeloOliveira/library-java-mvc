package com.biblioteca.repository;

import com.biblioteca.model.usuario.Admin;
import com.biblioteca.model.usuario.Bibliotecario;
import com.biblioteca.model.usuario.TipoUsuario;
import com.biblioteca.model.usuario.Usuario;
import com.biblioteca.model.usuario.UsuarioComum;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementação em memória do repositório de usuários
 */
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();

    /**
     * Construtor que inicializa usuários de demonstração
     */
    public UsuarioRepositoryImpl() {
        inicializarUsuariosDemonstracao();
    }

    /**
     * Inicializa usuários de demonstração para testes
     */
    private void inicializarUsuariosDemonstracao() {
        // Admin
        Usuario admin = new Admin(
            UUID.randomUUID().toString(),
            "Administrador",
            "admin@biblioteca.com",
            "senha123"
        );
        usuarios.put(admin.getId(), admin);

        // Bibliotecário
        Usuario bibliotecario = new Bibliotecario(
            UUID.randomUUID().toString(),
            "João Silva",
            "bibliotecario@biblioteca.com",
            "senha123"
        );
        usuarios.put(bibliotecario.getId(), bibliotecario);

        // Usuário Comum
        Usuario usuarioComum = new UsuarioComum(
            UUID.randomUUID().toString(),
            "Maria Santos",
            "usuario@biblioteca.com",
            "senha123"
        );
        usuarios.put(usuarioComum.getId(), usuarioComum);
    }

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
