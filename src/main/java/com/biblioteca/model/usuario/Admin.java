package com.biblioteca.model.usuario;

/**
 * Admin - Permissões totais no sistema
 * Demonstra HERANÇA da classe Usuario
 */
public class Admin extends Usuario {

    public Admin(String id, String nome, String email, String senha) {
        super(id, nome, email, senha, TipoUsuario.ADMIN);
    }

    /**
     * Admin tem TODAS as permissões
     * Demonstra POLIMORFISMO - implementação específica
     */
    @Override
    public boolean temPermissao(Permissao permissao) {
        return true; // Admin pode tudo
    }

    @Override
    public String getDescricaoTipo() {
        return "Administrador do Sistema - Acesso Total";
    }

    /**
     * Método específico de Admin
     */
    public boolean podeGerenciarSistema() {
        return true;
    }
}
