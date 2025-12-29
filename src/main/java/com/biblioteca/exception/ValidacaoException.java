package com.biblioteca.exception;

/**
 * Excecao lancada para validacoes de dados
 */
public class ValidacaoException extends BibliotecaException {
    
    public ValidacaoException(String campo, String mensagem) {
        super("VALIDACAO_ERROR", "Campo '" + campo + "': " + mensagem);
    }
    
    public ValidacaoException(String mensagem) {
        super("VALIDACAO_ERROR", mensagem);
    }
}
