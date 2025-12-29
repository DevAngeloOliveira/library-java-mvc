package com.biblioteca.model;

/**
 * Classe que demonstra HERANÇA (Model - MVC)
 * DVD herda de ItemBiblioteca
 */
public class DVD extends ItemBiblioteca {
    private String diretor;
    private int duracaoMinutos;
    private String genero;
    
    public DVD(String titulo, String codigo, String diretor, int duracaoMinutos, String genero) {
        super(titulo, codigo);
        this.diretor = diretor;
        this.duracaoMinutos = duracaoMinutos;
        this.genero = genero;
    }
    
    // Getters e Setters
    public String getDiretor() {
        return diretor;
    }
    
    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }
    
    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }
    
    public void setDuracaoMinutos(int duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    @Override
    public String getTipo() {
        return "DVD";
    }
}
