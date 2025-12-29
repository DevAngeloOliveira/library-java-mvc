package com.biblioteca.exception;

/**
 * Exceção para erros de autenticação
 */
public class AutenticacaoException extends BibliotecaException {
    public AutenticacaoException(String mensagem) {
        super(mensagem);
    }

    public AutenticacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
