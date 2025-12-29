package com.biblioteca.dto;

import com.biblioteca.model.usuario.Usuario;

/**
 * DTO de resposta para usuário (sem senha)
 */
public class UsuarioResponseDTO {
    private String id;
    private String nome;
    private String email;
    private String tipo;
    private String descricaoTipo;
    private boolean ativo;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.tipo = usuario.getTipo().name();
        this.descricaoTipo = usuario.getDescricaoTipo();
        this.ativo = usuario.isAtivo();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricaoTipo() {
        return descricaoTipo;
    }

    public boolean isAtivo() {
        return ativo;
    }
}
