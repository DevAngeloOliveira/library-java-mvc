package com.biblioteca.util;

import com.biblioteca.exception.ValidacaoException;

/**
 * Utilitario para validacao de dados
 */
public class Validador {
    
    /**
     * Valida se uma string nao e nula ou vazia
     */
    public static void validarNaoVazio(String valor, String nomeCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacaoException(nomeCampo, "nao pode ser vazio");
        }
    }
    
    /**
     * Valida se um numero e positivo
     */
    public static void validarPositivo(int valor, String nomeCampo) {
        if (valor <= 0) {
            throw new ValidacaoException(nomeCampo, "deve ser maior que zero");
        }
    }
    
    /**
     * Valida tamanho minimo de string
     */
    public static void validarTamanhoMinimo(String valor, int tamanhoMinimo, String nomeCampo) {
        validarNaoVazio(valor, nomeCampo);
        if (valor.trim().length() < tamanhoMinimo) {
            throw new ValidacaoException(nomeCampo, 
                "deve ter no minimo " + tamanhoMinimo + " caracteres");
        }
    }
    
    /**
     * Valida tamanho maximo de string
     */
    public static void validarTamanhoMaximo(String valor, int tamanhoMaximo, String nomeCampo) {
        if (valor != null && valor.length() > tamanhoMaximo) {
            throw new ValidacaoException(nomeCampo, 
                "deve ter no maximo " + tamanhoMaximo + " caracteres");
        }
    }
    
    /**
     * Valida formato de codigo (ex: LIV001, REV001, DVD001)
     */
    public static void validarFormatoCodigo(String codigo) {
        validarNaoVazio(codigo, "codigo");
        if (!codigo.matches("^[A-Z]{3}\\d{3}$")) {
            throw new ValidacaoException("codigo", 
                "deve estar no formato XXX999 (ex: LIV001)");
        }
    }
    
    /**
     * Valida se objeto nao e nulo
     */
    public static void validarNaoNulo(Object objeto, String nomeCampo) {
        if (objeto == null) {
            throw new ValidacaoException(nomeCampo, "nao pode ser nulo");
        }
    }
}
