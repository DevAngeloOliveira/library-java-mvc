package com.biblioteca.service;

import com.biblioteca.dto.*;
import com.biblioteca.exception.*;
import com.biblioteca.model.Sessao;
import com.biblioteca.model.usuario.*;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.util.Logger;
import com.biblioteca.util.Validador;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço de autenticação e gerenciamento de usuários
 * Camada de lógica de negócio
 */
public class AutenticacaoService {
    private static final Logger logger = Logger.getLogger(AutenticacaoService.class);
    private final UsuarioRepository repository;
    private final Map<String, Sessao> sessoes = new ConcurrentHashMap<>();

    public AutenticacaoService(UsuarioRepository repository) {
        this.repository = repository;
        logger.info("AutenticacaoService inicializado");
    }

    /**
     * Realiza login do usuário
     */
    public LoginResponseDTO login(LoginDTO loginDTO) throws AutenticacaoException, ValidacaoException {
        logger.info("Tentativa de login: " + loginDTO.getEmail());

        // Validação
        Validador.validarNaoVazio(loginDTO.getEmail(), "Email é obrigatório");
        Validador.validarNaoVazio(loginDTO.getSenha(), "Senha é obrigatória");

        // Buscar usuário
        Usuario usuario = repository.buscarPorEmail(loginDTO.getEmail())
            .orElseThrow(() -> new AutenticacaoException("Email ou senha inválidos"));

        // Verificar se está ativo
        if (!usuario.isAtivo()) {
            logger.warn("Tentativa de login com usuário inativo: " + loginDTO.getEmail());
            throw new AutenticacaoException("Usuário inativo");
        }

        // Validar senha
        if (!usuario.validarSenha(loginDTO.getSenha())) {
            logger.warn("Senha incorreta para: " + loginDTO.getEmail());
            throw new AutenticacaoException("Email ou senha inválidos");
        }

        // Criar sessão
        Sessao sessao = new Sessao(usuario);
        sessoes.put(sessao.getToken(), sessao);

        logger.info("Login bem-sucedido: " + usuario.getEmail() + " (" + usuario.getTipo() + ")");
        return new LoginResponseDTO(sessao, "Login realizado com sucesso");
    }

    /**
     * Realiza logout do usuário
     */
    public void logout(String token) {
        Sessao sessao = sessoes.get(token);
        if (sessao != null) {
            sessao.invalidar();
            sessoes.remove(token);
            logger.info("Logout realizado: " + sessao.getUsuario().getEmail());
        }
    }

    /**
     * Valida token e retorna sessão
     */
    public Sessao validarToken(String token) throws AutenticacaoException {
        if (token == null || token.isEmpty()) {
            throw new AutenticacaoException("Token não fornecido");
        }

        Sessao sessao = sessoes.get(token);
        if (sessao == null || !sessao.isValida()) {
            throw new AutenticacaoException("Sessão inválida ou expirada");
        }

        sessao.renovar();
        return sessao;
    }

    /**
     * Cria novo usuário
     */
    public UsuarioResponseDTO criarUsuario(UsuarioDTO dto, Usuario usuarioLogado) 
            throws UsuarioJaExisteException, ValidacaoException, PermissaoNegadaException {
        
        logger.info("Criando novo usuário: " + dto.getEmail());

        // Verificar permissão
        if (!usuarioLogado.temPermissao(Permissao.CRIAR_USUARIO)) {
            throw new PermissaoNegadaException("Você não tem permissão para criar usuários");
        }

        // Validações
        Validador.validarNaoVazio(dto.getNome(), "Nome é obrigatório");
        Validador.validarNaoVazio(dto.getEmail(), "Email é obrigatório");
        Validador.validarNaoVazio(dto.getSenha(), "Senha é obrigatória");
        Validador.validarNaoVazio(dto.getTipo(), "Tipo de usuário é obrigatório");

        // Verificar se email já existe
        if (repository.existeEmail(dto.getEmail())) {
            throw new UsuarioJaExisteException("Email já cadastrado: " + dto.getEmail());
        }

        // Criar usuário baseado no tipo
        String id = "USR" + System.currentTimeMillis();
        Usuario novoUsuario = criarUsuarioPorTipo(id, dto);

        // Salvar
        repository.salvar(novoUsuario);
        logger.info("Usuário criado com sucesso: " + novoUsuario.getId());

        return new UsuarioResponseDTO(novoUsuario);
    }

    /**
     * Factory method para criar usuário baseado no tipo
     */
    private Usuario criarUsuarioPorTipo(String id, UsuarioDTO dto) throws ValidacaoException {
        TipoUsuario tipo;
        try {
            tipo = TipoUsuario.valueOf(dto.getTipo().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidacaoException("Tipo de usuário inválido: " + dto.getTipo());
        }

        switch (tipo) {
            case ADMIN:
                return new Admin(id, dto.getNome(), dto.getEmail(), dto.getSenha());
            case BIBLIOTECARIO:
                return new Bibliotecario(id, dto.getNome(), dto.getEmail(), dto.getSenha());
            case USUARIO:
                return new UsuarioComum(id, dto.getNome(), dto.getEmail(), dto.getSenha());
            default:
                throw new ValidacaoException("Tipo de usuário não suportado");
        }
    }

    /**
     * Lista todos os usuários
     */
    public List<UsuarioResponseDTO> listarUsuarios(Usuario usuarioLogado) throws PermissaoNegadaException {
        if (!usuarioLogado.temPermissao(Permissao.LISTAR_USUARIOS)) {
            throw new PermissaoNegadaException("Você não tem permissão para listar usuários");
        }

        return repository.listarTodos().stream()
            .map(UsuarioResponseDTO::new)
            .toList();
    }

    /**
     * Busca usuário por ID
     */
    public UsuarioResponseDTO buscarUsuario(String id, Usuario usuarioLogado) 
            throws UsuarioNaoEncontradoException, PermissaoNegadaException {
        
        if (!usuarioLogado.temPermissao(Permissao.LISTAR_USUARIOS)) {
            throw new PermissaoNegadaException("Você não tem permissão para visualizar usuários");
        }

        Usuario usuario = repository.buscarPorId(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado: " + id));

        return new UsuarioResponseDTO(usuario);
    }

    /**
     * Atualiza usuário
     */
    public UsuarioResponseDTO atualizarUsuario(String id, UsuarioDTO dto, Usuario usuarioLogado) 
            throws UsuarioNaoEncontradoException, PermissaoNegadaException, ValidacaoException {
        
        logger.info("Atualizando usuário: " + id);

        if (!usuarioLogado.temPermissao(Permissao.EDITAR_USUARIO)) {
            throw new PermissaoNegadaException("Você não tem permissão para editar usuários");
        }

        Usuario usuario = repository.buscarPorId(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado: " + id));

        // Atualizar campos
        if (dto.getNome() != null && !dto.getNome().isEmpty()) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            usuario.setEmail(dto.getEmail());
        }
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            usuario.setSenha(dto.getSenha());
        }

        repository.atualizar(usuario);
        logger.info("Usuário atualizado: " + id);

        return new UsuarioResponseDTO(usuario);
    }

    /**
     * Remove usuário
     */
    public void removerUsuario(String id, Usuario usuarioLogado) 
            throws UsuarioNaoEncontradoException, PermissaoNegadaException {
        
        logger.info("Removendo usuário: " + id);

        if (!usuarioLogado.temPermissao(Permissao.EXCLUIR_USUARIO)) {
            throw new PermissaoNegadaException("Você não tem permissão para excluir usuários");
        }

        // Verificar se usuário existe
        repository.buscarPorId(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado: " + id));

        repository.remover(id);
        logger.info("Usuário removido: " + id);
    }

    /**
     * Desativa usuário (soft delete)
     */
    public void desativarUsuario(String id, Usuario usuarioLogado) 
            throws UsuarioNaoEncontradoException, PermissaoNegadaException {
        
        logger.info("Desativando usuário: " + id);

        if (!usuarioLogado.temPermissao(Permissao.EDITAR_USUARIO)) {
            throw new PermissaoNegadaException("Você não tem permissão para desativar usuários");
        }

        Usuario usuario = repository.buscarPorId(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado: " + id));

        usuario.setAtivo(false);
        repository.atualizar(usuario);
        logger.info("Usuário desativado: " + id);
    }

    /**
     * Ativa usuário
     */
    public void ativarUsuario(String id, Usuario usuarioLogado) 
            throws UsuarioNaoEncontradoException, PermissaoNegadaException {
        
        logger.info("Ativando usuário: " + id);

        if (!usuarioLogado.temPermissao(Permissao.EDITAR_USUARIO)) {
            throw new PermissaoNegadaException("Você não tem permissão para ativar usuários");
        }

        Usuario usuario = repository.buscarPorId(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado: " + id));

        usuario.setAtivo(true);
        repository.atualizar(usuario);
        logger.info("Usuário ativado: " + id);
    }
}
