-- ============================================
-- Script DDL - Criação das Tabelas
-- Sistema de Biblioteca
-- ============================================

-- Criação do schema (opcional)
-- CREATE SCHEMA IF NOT EXISTS biblioteca;
-- SET search_path TO biblioteca;

-- Tabela de Itens da Biblioteca (tabela principal)
CREATE TABLE IF NOT EXISTS itens_biblioteca (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('LIVRO', 'REVISTA', 'DVD')),
    emprestado BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela específica para Livros
CREATE TABLE IF NOT EXISTS livros (
    id SERIAL PRIMARY KEY,
    item_id INTEGER UNIQUE NOT NULL,
    autor VARCHAR(255) NOT NULL,
    numero_paginas INTEGER,
    isbn VARCHAR(20),
    FOREIGN KEY (item_id) REFERENCES itens_biblioteca(id) ON DELETE CASCADE
);

-- Tabela específica para Revistas
CREATE TABLE IF NOT EXISTS revistas (
    id SERIAL PRIMARY KEY,
    item_id INTEGER UNIQUE NOT NULL,
    edicao INTEGER,
    mes_ano VARCHAR(50),
    editora VARCHAR(255),
    FOREIGN KEY (item_id) REFERENCES itens_biblioteca(id) ON DELETE CASCADE
);

-- Tabela específica para DVDs
CREATE TABLE IF NOT EXISTS dvds (
    id SERIAL PRIMARY KEY,
    item_id INTEGER UNIQUE NOT NULL,
    diretor VARCHAR(255),
    duracao_minutos INTEGER,
    genero VARCHAR(100),
    FOREIGN KEY (item_id) REFERENCES itens_biblioteca(id) ON DELETE CASCADE
);

-- Índices para melhorar performance
CREATE INDEX IF NOT EXISTS idx_itens_codigo ON itens_biblioteca(codigo);
CREATE INDEX IF NOT EXISTS idx_itens_tipo ON itens_biblioteca(tipo);
CREATE INDEX IF NOT EXISTS idx_itens_emprestado ON itens_biblioteca(emprestado);
CREATE INDEX IF NOT EXISTS idx_livros_item_id ON livros(item_id);
CREATE INDEX IF NOT EXISTS idx_revistas_item_id ON revistas(item_id);
CREATE INDEX IF NOT EXISTS idx_dvds_item_id ON dvds(item_id);

-- Função para atualizar o campo updated_at automaticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para atualizar updated_at
CREATE TRIGGER update_itens_updated_at
    BEFORE UPDATE ON itens_biblioteca
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Comentários nas tabelas
COMMENT ON TABLE itens_biblioteca IS 'Tabela principal que armazena todos os itens da biblioteca';
COMMENT ON TABLE livros IS 'Tabela com informações específicas de livros';
COMMENT ON TABLE revistas IS 'Tabela com informações específicas de revistas';
COMMENT ON TABLE dvds IS 'Tabela com informações específicas de DVDs';
