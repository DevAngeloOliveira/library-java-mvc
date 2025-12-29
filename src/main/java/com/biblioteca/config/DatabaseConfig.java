package com.biblioteca.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe de configuração do banco de dados SQLite
 * Gerencia conexões com o banco de dados local
 */
public class DatabaseConfig {
    private static String DATABASE_URL;
    
    static {
        try {
            inicializarConfiguracao();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar conexao com o banco de dados", e);
        }
    }
    
    private static void inicializarConfiguracao() throws IOException {
        Properties props = carregarPropriedades();
        DATABASE_URL = props.getProperty("db.url");
        
        // Carregar driver SQLite
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver SQLite nao encontrado", e);
        }
    }
    
    private static Properties carregarPropriedades() throws IOException {
        Properties props = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new IOException("Arquivo database.properties nao encontrado");
            }
            props.load(input);
        }
        return props;
    }
    
    /**
     * Obtém uma conexão com o banco de dados
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
    
    /**
     * Verifica se a conexão está ativa
     */
    public static boolean testarConexao() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Fecha conexões (SQLite não precisa de pool, então esse método é vazio)
     */
    public static void close() {
        // SQLite não precisa fechar pool de conexões
    }
}
