package com.biblioteca.exception;

/**
 * Excecao lancada para operacoes invalidas
 */
public class OperacaoInvalidaException extends BibliotecaException {
    
    public OperacaoInvalidaException(String mensagem) {
        super("OPERACAO_INVALIDA", mensagem);
    }
}
