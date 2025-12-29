package com.biblioteca.exception;

/**
 * Excecao base para o sistema de biblioteca
 */
public class BibliotecaException extends RuntimeException {
    private final String codigo;
    
    public BibliotecaException(String mensagem) {
        super(mensagem);
        this.codigo = "BIBLIOTECA_ERROR";
    }
    
    public BibliotecaException(String codigo, String mensagem) {
        super(mensagem);
        this.codigo = codigo;
    }
    
    public BibliotecaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.codigo = "BIBLIOTECA_ERROR";
    }
    
    public String getCodigo() {
        return codigo;
    }
}
