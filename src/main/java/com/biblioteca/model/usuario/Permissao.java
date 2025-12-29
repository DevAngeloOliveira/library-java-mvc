package com.biblioteca.model.usuario;

/**
 * Enum de permissões do sistema
 */
public enum Permissao {
    // Permissões de Usuários
    CRIAR_USUARIO,
    EDITAR_USUARIO,
    EXCLUIR_USUARIO,
    LISTAR_USUARIOS,
    
    // Permissões de Itens
    CRIAR_ITEM,
    EDITAR_ITEM,
    EXCLUIR_ITEM,
    LISTAR_ITENS,
    
    // Permissões de Empréstimos
    EMPRESTAR_QUALQUER_ITEM,
    EMPRESTAR_PROPRIO_ITEM,
    DEVOLVER_QUALQUER_ITEM,
    DEVOLVER_PROPRIO_ITEM,
    LISTAR_TODOS_EMPRESTIMOS,
    LISTAR_PROPRIOS_EMPRESTIMOS,
    
    // Permissões de Relatórios
    GERAR_RELATORIOS,
    VISUALIZAR_ESTATISTICAS
}
