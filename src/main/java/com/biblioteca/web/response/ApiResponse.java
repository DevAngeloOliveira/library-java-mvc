package com.biblioteca.web.response;

import com.biblioteca.dto.ItemResponseDTO;
import java.util.List;

/**
 * Padrao de resposta para APIs
 * Fornece estrutura consistente para todas as respostas
 */
public class ApiResponse<T> {
    private boolean sucesso;
    private T dados;
    private String mensagem;
    private String codigo;
    private long timestamp;
    
    private ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * Cria resposta de sucesso com dados
     */
    public static <T> ApiResponse<T> sucesso(T dados) {
        ApiResponse<T> response = new ApiResponse<>();
        response.sucesso = true;
        response.dados = dados;
        return response;
    }
    
    /**
     * Cria resposta de sucesso com mensagem
     */
    public static <T> ApiResponse<T> sucesso(String mensagem) {
        ApiResponse<T> response = new ApiResponse<>();
        response.sucesso = true;
        response.mensagem = mensagem;
        return response;
    }
    
    /**
     * Cria resposta de sucesso com dados e mensagem
     */
    public static <T> ApiResponse<T> sucesso(T dados, String mensagem) {
        ApiResponse<T> response = new ApiResponse<>();
        response.sucesso = true;
        response.dados = dados;
        response.mensagem = mensagem;
        return response;
    }
    
    /**
     * Cria resposta de erro
     */
    public static <T> ApiResponse<T> erro(String codigo, String mensagem) {
        ApiResponse<T> response = new ApiResponse<>();
        response.sucesso = false;
        response.codigo = codigo;
        response.mensagem = mensagem;
        return response;
    }
    
    /**
     * Cria resposta de erro simples
     */
    public static <T> ApiResponse<T> erro(String mensagem) {
        return erro("ERROR", mensagem);
    }
    
    /**
     * Converte para JSON manualmente (sem dependencias externas)
     */
    public String toJSON() {
        StringBuilder json = new StringBuilder("{");
        json.append("\"sucesso\":").append(sucesso).append(",");
        json.append("\"timestamp\":").append(timestamp);
        
        if (mensagem != null) {
            json.append(",\"mensagem\":\"").append(escaparJSON(mensagem)).append("\"");
        }
        
        if (codigo != null) {
            json.append(",\"codigo\":\"").append(escaparJSON(codigo)).append("\"");
        }
        
        if (dados != null) {
            json.append(",\"dados\":");
            if (dados instanceof List) {
                json.append(converterListaParaJSON((List<?>) dados));
            } else if (dados instanceof ItemResponseDTO) {
                json.append(converterItemParaJSON((ItemResponseDTO) dados));
            } else {
                json.append("\"").append(escaparJSON(dados.toString())).append("\"");
            }
        }
        
        json.append("}");
        return json.toString();
    }
    
    private String converterListaParaJSON(List<?> lista) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i) instanceof ItemResponseDTO) {
                json.append(converterItemParaJSON((ItemResponseDTO) lista.get(i)));
            }
            if (i < lista.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }
    
    private String converterItemParaJSON(ItemResponseDTO item) {
        StringBuilder json = new StringBuilder("{");
        json.append("\"codigo\":\"").append(escaparJSON(item.getCodigo())).append("\",");
        json.append("\"titulo\":\"").append(escaparJSON(item.getTitulo())).append("\",");
        json.append("\"tipo\":\"").append(escaparJSON(item.getTipo())).append("\",");
        json.append("\"detalhes\":\"").append(escaparJSON(item.getDetalhes())).append("\",");
        json.append("\"emprestado\":").append(item.isEmprestado());
        json.append("}");
        return json.toString();
    }
    
    private String escaparJSON(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    // Getters
    public boolean isSucesso() { return sucesso; }
    public T getDados() { return dados; }
    public String getMensagem() { return mensagem; }
    public String getCodigo() { return codigo; }
    public long getTimestamp() { return timestamp; }
}
