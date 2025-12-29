package com.biblioteca.repository;

import com.biblioteca.config.DatabaseConfig;
import com.biblioteca.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do Repository usando SQLite
 * Realiza operações CRUD no banco de dados
 */
public class ItemRepositorySQLite implements ItemRepository {
    
    @Override
    public void salvar(ItemBiblioteca item) {
        String sqlItem = "INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlItem, Statement.RETURN_GENERATED_KEYS)) {
            
            conn.setAutoCommit(false);
            
            try {
                // Inserir item principal
                pstmt.setString(1, item.getCodigo());
                pstmt.setString(2, item.getTitulo());
                pstmt.setString(3, item.getTipo());
                pstmt.setBoolean(4, item.isEmprestado());
                pstmt.executeUpdate();
                
                // Obter ID gerado
                int itemId;
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        itemId = rs.getInt(1);
                    } else {
                        throw new SQLException("Falha ao obter ID do item");
                    }
                }
                
                // Inserir dados específicos do tipo
                salvarDadosEspecificos(conn, itemId, item);
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar item: " + e.getMessage(), e);
        }
    }
    
    private void salvarDadosEspecificos(Connection conn, int itemId, ItemBiblioteca item) throws SQLException {
        if (item instanceof Livro) {
            Livro livro = (Livro) item;
            String sql = "INSERT INTO livros (item_id, autor, numero_paginas, isbn) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, itemId);
                pstmt.setString(2, livro.getAutor());
                pstmt.setInt(3, livro.getNumeroPaginas());
                pstmt.setString(4, livro.getIsbn());
                pstmt.executeUpdate();
            }
        } else if (item instanceof Revista) {
            Revista revista = (Revista) item;
            String sql = "INSERT INTO revistas (item_id, edicao, mes_ano, editora) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, itemId);
                pstmt.setInt(2, revista.getEdicao());
                pstmt.setString(3, revista.getMesAno());
                pstmt.setString(4, revista.getEditora());
                pstmt.executeUpdate();
            }
        } else if (item instanceof DVD) {
            DVD dvd = (DVD) item;
            String sql = "INSERT INTO dvds (item_id, diretor, duracao_minutos, genero) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, itemId);
                pstmt.setString(2, dvd.getDiretor());
                pstmt.setInt(3, dvd.getDuracaoMinutos());
                pstmt.setString(4, dvd.getGenero());
                pstmt.executeUpdate();
            }
        }
    }
    
    @Override
    public Optional<ItemBiblioteca> buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM itens_biblioteca WHERE codigo = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(construirItem(conn, rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar item: " + e.getMessage(), e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<ItemBiblioteca> buscarTodos() {
        String sql = "SELECT * FROM itens_biblioteca ORDER BY codigo";
        List<ItemBiblioteca> itens = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                itens.add(construirItem(conn, rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens: " + e.getMessage(), e);
        }
        
        return itens;
    }
    
    @Override
    public List<ItemBiblioteca> buscarPorTipo(String tipo) {
        String sql = "SELECT * FROM itens_biblioteca WHERE tipo = ? ORDER BY codigo";
        List<ItemBiblioteca> itens = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tipo.toUpperCase());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    itens.add(construirItem(conn, rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens por tipo: " + e.getMessage(), e);
        }
        
        return itens;
    }
    
    @Override
    public List<ItemBiblioteca> buscarDisponiveis() {
        String sql = "SELECT * FROM itens_biblioteca WHERE emprestado = FALSE ORDER BY codigo";
        List<ItemBiblioteca> itens = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                itens.add(construirItem(conn, rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens disponiveis: " + e.getMessage(), e);
        }
        
        return itens;
    }
    
    @Override
    public List<ItemBiblioteca> buscarEmprestados() {
        String sql = "SELECT * FROM itens_biblioteca WHERE emprestado = TRUE ORDER BY codigo";
        List<ItemBiblioteca> itens = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                itens.add(construirItem(conn, rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens emprestados: " + e.getMessage(), e);
        }
        
        return itens;
    }
    
    @Override
    public boolean remover(String codigo) {
        String sql = "DELETE FROM itens_biblioteca WHERE codigo = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover item: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void atualizar(ItemBiblioteca item) {
        String sql = "UPDATE itens_biblioteca SET titulo = ?, emprestado = ? WHERE codigo = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, item.getTitulo());
            pstmt.setBoolean(2, item.isEmprestado());
            pstmt.setString(3, item.getCodigo());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Item nao encontrado para atualizacao");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar item: " + e.getMessage(), e);
        }
    }
    
    private ItemBiblioteca construirItem(Connection conn, ResultSet rs) throws SQLException {
        int itemId = rs.getInt("id");
        String codigo = rs.getString("codigo");
        String titulo = rs.getString("titulo");
        String tipo = rs.getString("tipo");
        boolean emprestado = rs.getBoolean("emprestado");
        
        ItemBiblioteca item = null;
        
        switch (tipo) {
            case "LIVRO":
                item = construirLivro(conn, itemId, codigo, titulo);
                break;
            case "REVISTA":
                item = construirRevista(conn, itemId, codigo, titulo);
                break;
            case "DVD":
                item = construirDVD(conn, itemId, codigo, titulo);
                break;
        }
        
        if (item != null) {
            item.setEmprestado(emprestado);
        }
        
        return item;
    }
    
    private Livro construirLivro(Connection conn, int itemId, String codigo, String titulo) throws SQLException {
        String sql = "SELECT * FROM livros WHERE item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Livro(
                        titulo,
                        codigo,
                        rs.getString("autor"),
                        rs.getInt("numero_paginas"),
                        rs.getString("isbn")
                    );
                }
            }
        }
        return null;
    }
    
    private Revista construirRevista(Connection conn, int itemId, String codigo, String titulo) throws SQLException {
        String sql = "SELECT * FROM revistas WHERE item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Revista(
                        titulo,
                        codigo,
                        rs.getInt("edicao"),
                        rs.getString("mes_ano"),
                        rs.getString("editora")
                    );
                }
            }
        }
        return null;
    }
    
    private DVD construirDVD(Connection conn, int itemId, String codigo, String titulo) throws SQLException {
        String sql = "SELECT * FROM dvds WHERE item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new DVD(
                        titulo,
                        codigo,
                        rs.getString("diretor"),
                        rs.getInt("duracao_minutos"),
                        rs.getString("genero")
                    );
                }
            }
        }
        return null;
    }
}
