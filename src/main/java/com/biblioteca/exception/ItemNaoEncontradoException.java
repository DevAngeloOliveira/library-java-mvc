package com.biblioteca.exception;

/**
 * Excecao lancada quando um item nao e encontrado
 */
public class ItemNaoEncontradoException extends BibliotecaException {
    
    public ItemNaoEncontradoException(String codigo) {
        super("ITEM_NAO_ENCONTRADO", "Item com codigo '" + codigo + "' nao foi encontrado");
    }
}
