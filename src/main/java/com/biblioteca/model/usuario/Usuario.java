package com.biblioteca.model.usuario;

import java.time.LocalDateTime;

/**
 * Classe abstrata base para hierarquia de usuários
 * Demonstra ABSTRAÇÃO e HERANÇA
 */
public abstract class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha; // Em produção, usar hash
    private TipoUsuario tipo;
    private boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime ultimoAcesso;

    public Usuario(String id, String nome, String email, String senha, TipoUsuario tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
        this.ativo = true;
        this.dataCriacao = LocalDateTime.now();
    }

    /**
     * Método abstrato - cada tipo de usuário tem permissões diferentes
     * Demonstra POLIMORFISMO
     */
    public abstract boolean temPermissao(Permissao permissao);

    /**
     * Método abstrato - descrição específica de cada tipo
     */
    public abstract String getDescricaoTipo();

    /**
     * Valida credenciais de login
     */
    public boolean validarSenha(String senha) {
        return this.senha.equals(senha);
    }

    /**
     * Registra acesso do usuário
     */
    public void registrarAcesso() {
        this.ultimoAcesso = LocalDateTime.now();
    }

    // Getters e Setters (ENCAPSULAMENTO)
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] - %s (%s)", nome, id, email, tipo);
    }
}
