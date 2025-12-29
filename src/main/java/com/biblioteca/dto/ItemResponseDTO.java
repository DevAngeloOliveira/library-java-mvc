package com.biblioteca.dto;

/**
 * DTO para resposta com informações de um Item
 * Usado para retornar dados para a camada de apresentação
 */
public class ItemResponseDTO {
    private String tipo;
    private String titulo;
    private String codigo;
    private boolean emprestado;
    private String detalhes;
    
    public ItemResponseDTO(String tipo, String titulo, String codigo, boolean emprestado, String detalhes) {
        this.tipo = tipo;
        this.titulo = titulo;
        this.codigo = codigo;
        this.emprestado = emprestado;
        this.detalhes = detalhes;
    }
    
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
    
    public boolean isEmprestado() {
        return emprestado;
    }
    
    public void setEmprestado(boolean emprestado) {
        this.emprestado = emprestado;
    }
    
    public String getDetalhes() {
        return detalhes;
    }
    
    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }
}
