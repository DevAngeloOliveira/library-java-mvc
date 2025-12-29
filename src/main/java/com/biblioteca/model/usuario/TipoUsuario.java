package com.biblioteca.model.usuario;

/**
 * Enum para tipos de usuário
 */
public enum TipoUsuario {
    ADMIN("Administrador"),
    BIBLIOTECARIO("Bibliotecário"),
    USUARIO("Usuário Comum");

    private final String descricao;

    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
