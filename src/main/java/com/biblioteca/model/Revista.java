package com.biblioteca.model;

/**
 * Classe que demonstra HERANÇA (Model - MVC)
 * Revista herda de ItemBiblioteca
 */
public class Revista extends ItemBiblioteca {
    private int edicao;
    private String mesAno;
    private String editora;
    
    public Revista(String titulo, String codigo, int edicao, String mesAno, String editora) {
        super(titulo, codigo);
        this.edicao = edicao;
        this.mesAno = mesAno;
        this.editora = editora;
    }
    
    // Getters e Setters
    public int getEdicao() {
        return edicao;
    }
    
    public void setEdicao(int edicao) {
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
    
    @Override
    public String getTipo() {
        return "REVISTA";
    }
}
