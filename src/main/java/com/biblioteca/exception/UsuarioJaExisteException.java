package com.biblioteca.exception;

/**
 * Exceção quando tenta criar usuário com email duplicado
 */
public class UsuarioJaExisteException extends BibliotecaException {
    public UsuarioJaExisteException(String mensagem) {
        super(mensagem);
    }

    public UsuarioJaExisteException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
