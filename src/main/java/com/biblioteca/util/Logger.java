package com.biblioteca.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Sistema de logging simples e profissional
 * Substitui System.out.println por logs estruturados
 */
public class Logger {
    
    private final String className;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private Logger(String className) {
        this.className = className;
    }
    
    /**
     * Obtem um logger para a classe especificada
     */
    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz.getSimpleName());
    }
    
    /**
     * Log de informacao
     */
    public void info(String mensagem) {
        log("INFO", mensagem);
    }
    
    /**
     * Log de aviso
     */
    public void warn(String mensagem) {
        log("WARN", mensagem);
    }
    
    /**
     * Log de erro
     */
    public void error(String mensagem) {
        log("ERROR", mensagem);
    }
    
    /**
     * Log de erro com excecao
     */
    public void error(String mensagem, Throwable t) {
        log("ERROR", mensagem + " - " + t.getMessage());
        if (isDebugEnabled()) {
            t.printStackTrace();
        }
    }
    
    /**
     * Log de debug (apenas se debug estiver habilitado)
     */
    public void debug(String mensagem) {
        if (isDebugEnabled()) {
            log("DEBUG", mensagem);
        }
    }
    
    /**
     * Verifica se debug esta habilitado
     */
    private boolean isDebugEnabled() {
        String debug = System.getProperty("biblioteca.debug", "false");
        return "true".equalsIgnoreCase(debug);
    }
    
    /**
     * Formata e imprime a mensagem de log
     */
    private void log(String level, String mensagem) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logMessage = String.format("[%s] [%s] [%s] %s", 
            timestamp, level, className, mensagem);
        
        if ("ERROR".equals(level)) {
            System.err.println(logMessage);
        } else {
            System.out.println(logMessage);
        }
    }
}
