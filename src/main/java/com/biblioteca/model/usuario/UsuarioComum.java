package com.biblioteca.model.usuario;

import java.util.Arrays;
import java.util.List;

/**
 * Usuário Comum - Acesso limitado (leitura e empréstimos próprios)
 * Demonstra HERANÇA da classe Usuario
 */
public class UsuarioComum extends Usuario {

    private static final List<Permissao> PERMISSOES = Arrays.asList(
        // Pode apenas visualizar itens
        Permissao.LISTAR_ITENS,
        
        // Pode gerenciar apenas seus próprios empréstimos
        Permissao.EMPRESTAR_PROPRIO_ITEM,
        Permissao.DEVOLVER_PROPRIO_ITEM,
        Permissao.LISTAR_PROPRIOS_EMPRESTIMOS
    );

    private int limiteEmprestimos;

    public UsuarioComum(String id, String nome, String email, String senha) {
        super(id, nome, email, senha, TipoUsuario.USUARIO);
        this.limiteEmprestimos = 3; // Limite padrão
    }

    /**
     * Usuário Comum tem permissões mínimas
     * Demonstra POLIMORFISMO - implementação específica
     */
    @Override
    public boolean temPermissao(Permissao permissao) {
        return PERMISSOES.contains(permissao);
    }

    @Override
    public String getDescricaoTipo() {
        return "Usuário Comum - Acesso de Leitura";
    }

    /**
     * Métodos específicos de Usuário Comum
     */
    public int getLimiteEmprestimos() {
        return limiteEmprestimos;
    }

    public void setLimiteEmprestimos(int limiteEmprestimos) {
        this.limiteEmprestimos = limiteEmprestimos;
    }

    public boolean podePegarMaisLivros(int emprestimosAtuais) {
        return emprestimosAtuais < limiteEmprestimos;
    }
}
