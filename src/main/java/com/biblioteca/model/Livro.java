package com.biblioteca.model;

/**
 * Classe que demonstra HERANÇA (Model - MVC)
 * Livro herda de ItemBiblioteca
 */
public class Livro extends ItemBiblioteca {
    private String autor;
    private int numeroPaginas;
    private String isbn;
    
    public Livro(String titulo, String codigo, String autor, int numeroPaginas, String isbn) {
        super(titulo, codigo);
        this.autor = autor;
        this.numeroPaginas = numeroPaginas;
        this.isbn = isbn;
    }
    
    // Getters e Setters
    public String getAutor() {
        return autor;
    }
    
    public void setAutor(String autor) {
        this.autor = autor;
    }
    
    public int getNumeroPaginas() {
        return numeroPaginas;
    }
    
    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    @Override
    public String getTipo() {
        return "LIVRO";
    }
}
