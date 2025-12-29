package com.biblioteca.exception;

/**
 * Excecao lancada quando um item ja existe
 */
public class ItemJaExisteException extends BibliotecaException {
    
    public ItemJaExisteException(String codigo) {
        super("ITEM_JA_EXISTE", "Ja existe um item com o codigo: " + codigo);
    }
}
