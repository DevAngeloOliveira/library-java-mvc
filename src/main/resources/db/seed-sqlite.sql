-- ============================================
-- Script de Seed - Dados Iniciais (SQLite)
-- Sistema de Biblioteca
-- ============================================

-- Limpar dados existentes
DELETE FROM livros;
DELETE FROM revistas;
DELETE FROM dvds;
DELETE FROM itens_biblioteca;

-- Resetar sequências
DELETE FROM sqlite_sequence WHERE name IN ('itens_biblioteca', 'livros', 'revistas', 'dvds');

-- ========== LIVROS ==========

-- Livro 1: Clean Code
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado) VALUES ('LIV001', 'Clean Code', 'LIVRO', 0);
INSERT INTO livros (item_id, autor, numero_paginas, isbn) VALUES (last_insert_rowid(), 'Robert C. Martin', 464, '978-0132350884');

-- Livro 2: Design Patterns
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado) VALUES ('LIV002', 'Design Patterns', 'LIVRO', 0);
INSERT INTO livros (item_id, autor, numero_paginas, isbn) VALUES (last_insert_rowid(), 'Gang of Four', 395, '978-0201633612');

-- Livro 3: Refactoring
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado) VALUES ('LIV003', 'Refactoring', 'LIVRO', 0);
INSERT INTO livros (item_id, autor, numero_paginas, isbn) VALUES (last_insert_rowid(), 'Martin Fowler', 448, '978-0134757599');

-- Livro 4: Domain-Driven Design
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado) VALUES ('LIV004', 'Domain-Driven Design', 'LIVRO', 0);
INSERT INTO livros (item_id, autor, numero_paginas, isbn) VALUES (last_insert_rowid(), 'Eric Evans', 560, '978-0321125217');

-- Livro 5: The Pragmatic Programmer (emprestado)
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado) VALUES ('LIV005', 'The Pragmatic Programmer', 'LIVRO', 1);
INSERT INTO livros (item_id, autor, numero_paginas, isbn) VALUES (last_insert_rowid(), 'David Thomas e Andrew Hunt', 352, '978-0135957059');

-- ========== REVISTAS ==========

-- Revista 1: National Geographic
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado) VALUES ('REV001', 'National Geographic', 'REVISTA', 0);
INSERT INTO revistas (item_id, edicao, mes_ano, editora) VALUES (last_insert_rowid(), 145, 'Dezembro/2025', 'National Geographic Partners');

-- Revista 2: Scientific American
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado) VALUES ('REV002', 'Scientific American', 'REVISTA', 0);
INSERT INTO revistas (item_id, edicao, mes_ano, editora) VALUES (last_insert_rowid(), 328, 'Janeiro/2026', 'Springer Nature');

-- Revista 3: Java Magazine (emprestada)
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado) VALUES ('REV003', 'Java Magazine', 'REVISTA', 1);
INSERT INTO revistas (item_id, edicao, mes_ano, editora) VALUES (last_insert_rowid(), 52, 'Novembro/2025', 'DevMedia');

-- ========== DVDs ==========

-- DVD 1: Matrix
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado) VALUES ('DVD001', 'Matrix', 'DVD', 0);
INSERT INTO dvds (item_id, diretor, duracao_minutos, genero) VALUES (last_insert_rowid(), 'Wachowski Brothers', 136, 'Ficcao Cientifica');

-- DVD 2: Inception
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado) VALUES ('DVD002', 'Inception', 'DVD', 0);
INSERT INTO dvds (item_id, diretor, duracao_minutos, genero) VALUES (last_insert_rowid(), 'Christopher Nolan', 148, 'Ficcao Cientifica');

-- DVD 3: The Social Network (emprestado)
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado) VALUES ('DVD003', 'The Social Network', 'DVD', 1);
INSERT INTO dvds (item_id, diretor, duracao_minutos, genero) VALUES (last_insert_rowid(), 'David Fincher', 120, 'Drama');

-- DVD 4: Interstellar
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado) VALUES ('DVD004', 'Interstellar', 'DVD', 0);
INSERT INTO dvds (item_id, diretor, duracao_minutos, genero) VALUES (last_insert_rowid(), 'Christopher Nolan', 169, 'Ficcao Cientifica');

-- Verificar
SELECT 'Seed concluido com sucesso!' AS status;
SELECT 'Total de itens: ' || COUNT(*) AS total FROM itens_biblioteca;
SELECT 'Livros: ' || COUNT(*) AS livros FROM livros;
SELECT 'Revistas: ' || COUNT(*) AS revistas FROM revistas;
SELECT 'DVDs: ' || COUNT(*) AS dvds FROM dvds;
