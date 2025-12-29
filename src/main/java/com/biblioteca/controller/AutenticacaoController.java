package com.biblioteca.controller;

import com.biblioteca.dto.*;
import com.biblioteca.exception.*;
import com.biblioteca.model.Sessao;
import com.biblioteca.model.usuario.Usuario;
import com.biblioteca.service.AutenticacaoService;
import com.biblioteca.util.Logger;

import java.util.List;

/**
 * Controller para autenticação e gerenciamento de usuários
 */
public class AutenticacaoController {
    private static final Logger logger = Logger.getLogger(AutenticacaoController.class);
    private final AutenticacaoService service;

    public AutenticacaoController(AutenticacaoService service) {
        this.service = service;
        logger.debug("AutenticacaoController inicializado");
    }

    /**
     * Login
     */
    public LoginResponseDTO login(LoginDTO loginDTO) throws BibliotecaException {
        logger.debug("Controller: login()");
        try {
            return service.login(loginDTO);
        } catch (BibliotecaException e) {
            logger.error("Erro no controller ao fazer login: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado no controller: " + e.getMessage());
            throw new BibliotecaException("Erro ao fazer login", e);
        }
    }

    /**
     * Logout
     */
    public void logout(String token) {
        logger.debug("Controller: logout()");
        service.logout(token);
    }

    /**
     * Valida token e retorna usuário da sessão
     */
    public Usuario validarToken(String token) throws BibliotecaException {
        try {
            Sessao sessao = service.validarToken(token);
            return sessao.getUsuario();
        } catch (BibliotecaException e) {
            logger.error("Erro ao validar token: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Criar usuário
     */
    public UsuarioResponseDTO criarUsuario(UsuarioDTO dto, String token) throws BibliotecaException {
        logger.debug("Controller: criarUsuario() - " + dto.getEmail());
        try {
            Usuario usuarioLogado = validarToken(token);
            return service.criarUsuario(dto, usuarioLogado);
        } catch (BibliotecaException e) {
            logger.error("Erro no controller ao criar usuário: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado no controller: " + e.getMessage());
            throw new BibliotecaException("Erro ao criar usuário", e);
        }
    }

    /**
     * Listar usuários
     */
    public List<UsuarioResponseDTO> listarUsuarios(String token) throws BibliotecaException {
        logger.debug("Controller: listarUsuarios()");
        try {
            Usuario usuarioLogado = validarToken(token);
            return service.listarUsuarios(usuarioLogado);
        } catch (BibliotecaException e) {
            logger.error("Erro no controller ao listar usuários: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado no controller: " + e.getMessage());
            throw new BibliotecaException("Erro ao listar usuários", e);
        }
    }

    /**
     * Buscar usuário por ID
     */
    public UsuarioResponseDTO buscarUsuario(String id, String token) throws BibliotecaException {
        logger.debug("Controller: buscarUsuario() - " + id);
        try {
            Usuario usuarioLogado = validarToken(token);
            return service.buscarUsuario(id, usuarioLogado);
        } catch (BibliotecaException e) {
            logger.error("Erro no controller ao buscar usuário: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado no controller: " + e.getMessage());
            throw new BibliotecaException("Erro ao buscar usuário", e);
        }
    }

    /**
     * Atualizar usuário
     */
    public UsuarioResponseDTO atualizarUsuario(String id, UsuarioDTO dto, String token) throws BibliotecaException {
        logger.debug("Controller: atualizarUsuario() - " + id);
        try {
            Usuario usuarioLogado = validarToken(token);
            return service.atualizarUsuario(id, dto, usuarioLogado);
        } catch (BibliotecaException e) {
            logger.error("Erro no controller ao atualizar usuário: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado no controller: " + e.getMessage());
            throw new BibliotecaException("Erro ao atualizar usuário", e);
        }
    }

    /**
     * Remover usuário
     */
    public void removerUsuario(String id, String token) throws BibliotecaException {
        logger.debug("Controller: removerUsuario() - " + id);
        try {
            Usuario usuarioLogado = validarToken(token);
            service.removerUsuario(id, usuarioLogado);
        } catch (BibliotecaException e) {
            logger.error("Erro no controller ao remover usuário: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado no controller: " + e.getMessage());
            throw new BibliotecaException("Erro ao remover usuário", e);
        }
    }

    /**
     * Desativar usuário
     */
    public void desativarUsuario(String id, String token) throws BibliotecaException {
        logger.debug("Controller: desativarUsuario() - " + id);
        try {
            Usuario usuarioLogado = validarToken(token);
            service.desativarUsuario(id, usuarioLogado);
        } catch (BibliotecaException e) {
            logger.error("Erro no controller ao desativar usuário: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado no controller: " + e.getMessage());
            throw new BibliotecaException("Erro ao desativar usuário", e);
        }
    }

    /**
     * Ativar usuário
     */
    public void ativarUsuario(String id, String token) throws BibliotecaException {
        logger.debug("Controller: ativarUsuario() - " + id);
        try {
            Usuario usuarioLogado = validarToken(token);
            service.ativarUsuario(id, usuarioLogado);
        } catch (BibliotecaException e) {
            logger.error("Erro no controller ao ativar usuário: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado no controller: " + e.getMessage());
            throw new BibliotecaException("Erro ao ativar usuário", e);
        }
    }
}
