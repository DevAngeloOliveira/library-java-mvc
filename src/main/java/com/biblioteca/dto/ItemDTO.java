package com.biblioteca.dto;

/**
 * DTO (Data Transfer Object) para criar um novo Item
 * Usado para transferir dados entre camadas sem expor as entidades do domínio
 */
public class ItemDTO {
    private String tipo; // "LIVRO", "REVISTA", "DVD"
    private String titulo;
    private String codigo;
    
    // Atributos específicos de Livro
    private String autor;
    private Integer numeroPaginas;
    private String isbn;
    
    // Atributos específicos de Revista
    private Integer edicao;
    private String mesAno;
    private String editora;
    
    // Atributos específicos de DVD
    private String diretor;
    private Integer duracaoMinutos;
    private String genero;
    
    // Construtor vazio
    public ItemDTO() {}
    
    // Getters e Setters
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getAutor() {
        return autor;
    }
    
    public void setAutor(String autor) {
        this.autor = autor;
    }
    
    public Integer getNumeroPaginas() {
        return numeroPaginas;
    }
    
    public void setNumeroPaginas(Integer numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public Integer getEdicao() {
        return edicao;
    }
    
    public void setEdicao(Integer edicao) {
        this.edicao = edicao;
    }
    
    public String getMesAno() {
        return mesAno;
    }
    
    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
    }
    
    public String getEditora() {
        return editora;
    }
    
    public void setEditora(String editora) {
        this.editora = editora;
    }
    
    public String getDiretor() {
        return diretor;
    }
    
    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }
    
    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }
    
    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
}
