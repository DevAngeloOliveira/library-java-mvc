package com.biblioteca.model;

import com.biblioteca.model.usuario.Usuario;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa uma sessão de usuário autenticado
 */
public class Sessao {
    private String token;
    private Usuario usuario;
    private LocalDateTime dataLogin;
    private LocalDateTime dataExpiracao;
    private boolean ativa;

    public Sessao(Usuario usuario) {
        this.token = UUID.randomUUID().toString();
        this.usuario = usuario;
        this.dataLogin = LocalDateTime.now();
        this.dataExpiracao = LocalDateTime.now().plusHours(8); // 8 horas
        this.ativa = true;
        usuario.registrarAcesso();
    }

    public boolean isValida() {
        return ativa && LocalDateTime.now().isBefore(dataExpiracao);
    }

    public void renovar() {
        this.dataExpiracao = LocalDateTime.now().plusHours(8);
    }

    public void invalidar() {
        this.ativa = false;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDateTime getDataLogin() {
        return dataLogin;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public boolean isAtiva() {
        return ativa;
    }
}
