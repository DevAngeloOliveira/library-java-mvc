package com.biblioteca.exception;

/**
 * Exceção quando usuário não tem permissão para ação
 */
public class PermissaoNegadaException extends BibliotecaException {
    public PermissaoNegadaException(String mensagem) {
        super(mensagem);
    }

    public PermissaoNegadaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
