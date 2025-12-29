-- ============================================
-- Script DDL - Criação das Tabelas (SQLite)
-- Sistema de Biblioteca
-- ============================================

-- Tabela de Itens da Biblioteca (tabela principal)
CREATE TABLE IF NOT EXISTS itens_biblioteca (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('LIVRO', 'REVISTA', 'DVD')),
    emprestado INTEGER DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabela específica para Livros
CREATE TABLE IF NOT EXISTS livros (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    item_id INTEGER UNIQUE NOT NULL,
    autor VARCHAR(255) NOT NULL,
    numero_paginas INTEGER,
    isbn VARCHAR(20),
    FOREIGN KEY (item_id) REFERENCES itens_biblioteca(id) ON DELETE CASCADE
);

-- Tabela específica para Revistas
CREATE TABLE IF NOT EXISTS revistas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    item_id INTEGER UNIQUE NOT NULL,
    edicao INTEGER,
    mes_ano VARCHAR(50),
    editora VARCHAR(255),
    FOREIGN KEY (item_id) REFERENCES itens_biblioteca(id) ON DELETE CASCADE
);

-- Tabela específica para DVDs
CREATE TABLE IF NOT EXISTS dvds (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
