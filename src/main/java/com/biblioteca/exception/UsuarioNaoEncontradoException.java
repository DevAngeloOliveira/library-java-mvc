package com.biblioteca.exception;

/**
 * Exceção quando usuário não é encontrado
 */
public class UsuarioNaoEncontradoException extends BibliotecaException {
    public UsuarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public UsuarioNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
