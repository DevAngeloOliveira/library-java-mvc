package com.biblioteca.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Gerenciador de configuracoes da aplicacao
 * Centraliza acesso a propriedades de configuracao
 */
public class ConfigManager {
    
    private static ConfigManager instance;
    private Properties properties;
    
    private ConfigManager() {
        carregarPropriedades();
    }
    
    /**
     * Obtem instancia singleton
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    /**
     * Carrega propriedades dos arquivos de configuracao
     */
    private void carregarPropriedades() {
        properties = new Properties();
        
        // Carregar database.properties
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Aviso: Nao foi possivel carregar database.properties");
        }
        
        // Carregar application.properties se existir
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            // Arquivo opcional, ignorar se nao existir
        }
    }
    
    /**
     * Obtem propriedade como String
     */
    public String get(String chave) {
        return properties.getProperty(chave);
    }
    
    /**
     * Obtem propriedade com valor padrao
     */
    public String get(String chave, String valorPadrao) {
        return properties.getProperty(chave, valorPadrao);
    }
    
    /**
     * Obtem propriedade como Integer
     */
    public int getInt(String chave, int valorPadrao) {
        String valor = properties.getProperty(chave);
        if (valor != null) {
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                return valorPadrao;
            }
        }
        return valorPadrao;
    }
    
    /**
     * Obtem propriedade como Boolean
     */
    public boolean getBoolean(String chave, boolean valorPadrao) {
        String valor = properties.getProperty(chave);
        if (valor != null) {
            return Boolean.parseBoolean(valor);
        }
        return valorPadrao;
    }
    
    /**
     * Verifica se propriedade existe
     */
    public boolean contains(String chave) {
        return properties.containsKey(chave);
    }
}
