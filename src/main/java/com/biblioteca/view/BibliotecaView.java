package com.biblioteca.view;

import com.biblioteca.dto.ItemResponseDTO;
import java.util.List;

/**
 * View - Camada de Apresentação (MVC)
 * Responsável por exibir informações ao usuário
 */
public class BibliotecaView {
    
    public void exibirCabecalho() {
        System.out.println("==============================================");
        System.out.println("  SISTEMA DE BIBLIOTECA - Arquitetura MVC");
        System.out.println("==============================================\n");
    }
    
    public void exibirTitulo(String titulo) {
        System.out.println("\n" + titulo);
        System.out.println("=============================================");
    }
    
    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
    
    public void exibirSucesso(String mensagem) {
        System.out.println("[SUCESSO] " + mensagem);
    }
    
    public void exibirErro(String mensagem) {
        System.out.println("[ERRO] " + mensagem);
    }
    
    public void exibirItem(ItemResponseDTO item) {
        System.out.println("\n=== " + item.getTipo() + " ===");
        System.out.println("Titulo: " + item.getTitulo());
        System.out.println("Codigo: " + item.getCodigo());
        System.out.println("Detalhes: " + item.getDetalhes());
        System.out.println("Status: " + (item.isEmprestado() ? "[EMPRESTADO]" : "[DISPONIVEL]"));
    }
    
    public void exibirListaItens(List<ItemResponseDTO> itens) {
        if (itens.isEmpty()) {
            System.out.println("Nenhum item encontrado.");
            return;
        }
        
        for (ItemResponseDTO item : itens) {
            exibirItem(item);
        }
    }
    
    public void exibirListaResumida(List<ItemResponseDTO> itens) {
        if (itens.isEmpty()) {
            System.out.println("Nenhum item encontrado.");
            return;
        }
        
        for (ItemResponseDTO item : itens) {
            System.out.printf("- %s [%s] - %s%n", 
                item.getTitulo(), 
                item.getCodigo(),
                item.isEmprestado() ? "EMPRESTADO" : "DISPONIVEL"
            );
        }
    }
    
    public void exibirConceitosPOO() {
        System.out.println("\n==============================================");
        System.out.println("  CONCEITOS DEMONSTRADOS:");
        System.out.println("  [OK] POO - Encapsulamento, Heranca, Polimorfismo, Abstracao");
        System.out.println("  [OK] MVC - Model, View, Controller");
        System.out.println("  [OK] Repository Pattern - Persistencia em memoria");
        System.out.println("  [OK] Service Layer - Logica de negocio");
        System.out.println("  [OK] DTO Pattern - Transfer de dados entre camadas");
        System.out.println("==============================================");
    }
    
    public void exibirLinha() {
        System.out.println("---------------------------------------------");
    }
}
