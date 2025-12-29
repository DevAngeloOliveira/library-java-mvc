package com.biblioteca.model.usuario;

import java.util.Arrays;
import java.util.List;

/**
 * Bibliotecário - Gerencia itens e empréstimos
 * Demonstra HERANÇA da classe Usuario
 */
public class Bibliotecario extends Usuario {

    private static final List<Permissao> PERMISSOES = Arrays.asList(
        // Pode gerenciar itens
        Permissao.CRIAR_ITEM,
        Permissao.EDITAR_ITEM,
        Permissao.EXCLUIR_ITEM,
        Permissao.LISTAR_ITENS,
        
        // Pode gerenciar empréstimos de todos
        Permissao.EMPRESTAR_QUALQUER_ITEM,
        Permissao.DEVOLVER_QUALQUER_ITEM,
        Permissao.LISTAR_TODOS_EMPRESTIMOS,
        
        // Pode visualizar estatísticas
        Permissao.VISUALIZAR_ESTATISTICAS,
        
        // Pode listar usuários mas não gerenciar
        Permissao.LISTAR_USUARIOS
    );

    public Bibliotecario(String id, String nome, String email, String senha) {
        super(id, nome, email, senha, TipoUsuario.BIBLIOTECARIO);
    }

    /**
     * Bibliotecário tem permissões limitadas
     * Demonstra POLIMORFISMO - implementação específica
     */
    @Override
    public boolean temPermissao(Permissao permissao) {
        return PERMISSOES.contains(permissao);
    }

    @Override
    public String getDescricaoTipo() {
        return "Bibliotecário - Gerencia Acervo e Empréstimos";
    }

    /**
     * Método específico de Bibliotecário
     */
    public boolean podeGerenciarAcervo() {
        return true;
    }
}
