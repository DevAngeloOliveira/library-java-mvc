package com.biblioteca.dto;

import com.biblioteca.model.Sessao;

/**
 * DTO de resposta para login bem-sucedido
 */
public class LoginResponseDTO {
    private String token;
    private UsuarioResponseDTO usuario;
    private String mensagem;

    public LoginResponseDTO(Sessao sessao, String mensagem) {
        this.token = sessao.getToken();
        this.usuario = new UsuarioResponseDTO(sessao.getUsuario());
        this.mensagem = mensagem;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public UsuarioResponseDTO getUsuario() {
        return usuario;
    }

    public String getMensagem() {
        return mensagem;
    }
}
