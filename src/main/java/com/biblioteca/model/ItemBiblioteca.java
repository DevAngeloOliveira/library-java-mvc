package com.biblioteca.model;

/**
 * Classe abstrata que demonstra ABSTRAÇÃO (Model - MVC)
 * Define um contrato que todas as subclasses devem seguir
 */
public abstract class ItemBiblioteca {
    // ENCAPSULAMENTO: atributos privados com getters/setters
    private String titulo;
    private String codigo;
    private boolean emprestado;
    
    public ItemBiblioteca(String titulo, String codigo) {
        this.titulo = titulo;
        this.codigo = codigo;
        this.emprestado = false;
    }
    
    // Getters e Setters (ENCAPSULAMENTO)
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public boolean isEmprestado() {
        return emprestado;
    }
    
    public void setEmprestado(boolean emprestado) {
        this.emprestado = emprestado;
    }
    
    // Método abstrato - ABSTRAÇÃO
    public abstract String getTipo();
}
