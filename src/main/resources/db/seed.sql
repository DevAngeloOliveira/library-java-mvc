-- ============================================
-- Script de Seed - Dados Iniciais
-- Sistema de Biblioteca
-- ============================================

-- Limpar dados existentes (cuidado em produção!)
TRUNCATE TABLE livros, revistas, dvds, itens_biblioteca RESTART IDENTITY CASCADE;

-- ========== LIVROS ==========

-- Livro 1: Clean Code
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado)
VALUES ('LIV001', 'Clean Code', 'LIVRO', FALSE);

INSERT INTO livros (item_id, autor, numero_paginas, isbn)
VALUES (
    (SELECT id FROM itens_biblioteca WHERE codigo = 'LIV001'),
    'Robert C. Martin',
    464,
    '978-0132350884'
);

-- Livro 2: Design Patterns
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado)
VALUES ('LIV002', 'Design Patterns', 'LIVRO', FALSE);

INSERT INTO livros (item_id, autor, numero_paginas, isbn)
VALUES (
    (SELECT id FROM itens_biblioteca WHERE codigo = 'LIV002'),
    'Gang of Four',
    395,
    '978-0201633612'
);

-- Livro 3: Refactoring
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado)
VALUES ('LIV003', 'Refactoring', 'LIVRO', FALSE);

INSERT INTO livros (item_id, autor, numero_paginas, isbn)
VALUES (
    (SELECT id FROM itens_biblioteca WHERE codigo = 'LIV003'),
    'Martin Fowler',
    448,
    '978-0134757599'
);

-- Livro 4: Domain-Driven Design
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado)
VALUES ('LIV004', 'Domain-Driven Design', 'LIVRO', FALSE);

INSERT INTO livros (item_id, autor, numero_paginas, isbn)
VALUES (
    (SELECT id FROM itens_biblioteca WHERE codigo = 'LIV004'),
    'Eric Evans',
    560,
    '978-0321125217'
);

-- Livro 5: The Pragmatic Programmer
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado)
VALUES ('LIV005', 'The Pragmatic Programmer', 'LIVRO', TRUE);

INSERT INTO livros (item_id, autor, numero_paginas, isbn)
VALUES (
    (SELECT id FROM itens_biblioteca WHERE codigo = 'LIV005'),
    'David Thomas e Andrew Hunt',
    352,
    '978-0135957059'
);

-- ========== REVISTAS ==========

-- Revista 1: National Geographic
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado)
VALUES ('REV001', 'National Geographic', 'REVISTA', FALSE);

INSERT INTO revistas (item_id, edicao, mes_ano, editora)
VALUES (
    (SELECT id FROM itens_biblioteca WHERE codigo = 'REV001'),
    145,
    'Dezembro/2025',
    'National Geographic Partners'
);

-- Revista 2: Scientific American
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado)
VALUES ('REV002', 'Scientific American', 'REVISTA', FALSE);

INSERT INTO revistas (item_id, edicao, mes_ano, editora)
VALUES (
    (SELECT id FROM itens_biblioteca WHERE codigo = 'REV002'),
    328,
    'Janeiro/2026',
    'Springer Nature'
);

-- Revista 3: Java Magazine
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado)
VALUES ('REV003', 'Java Magazine', 'REVISTA', TRUE);

INSERT INTO revistas (item_id, edicao, mes_ano, editora)
VALUES (
    (SELECT id FROM itens_biblioteca WHERE codigo = 'REV003'),
    52,
    'Novembro/2025',
    'DevMedia'
);

-- ========== DVDs ==========

-- DVD 1: Matrix
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado)
VALUES ('DVD001', 'Matrix', 'DVD', FALSE);

INSERT INTO dvds (item_id, diretor, duracao_minutos, genero)
VALUES (
    (SELECT id FROM itens_biblioteca WHERE codigo = 'DVD001'),
    'Wachowski Brothers',
    136,
    'Ficcao Cientifica'
);

-- DVD 2: Inception
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado)
VALUES ('DVD002', 'Inception', 'DVD', FALSE);

INSERT INTO dvds (item_id, diretor, duracao_minutos, genero)
VALUES (
    (SELECT id FROM itens_biblioteca WHERE codigo = 'DVD002'),
    'Christopher Nolan',
    148,
    'Ficcao Cientifica'
);

-- DVD 3: The Social Network
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado)
VALUES ('DVD003', 'The Social Network', 'DVD', TRUE);

INSERT INTO dvds (item_id, diretor, duracao_minutos, genero)
VALUES (
    (SELECT id FROM itens_biblioteca WHERE codigo = 'DVD003'),
    'David Fincher',
    120,
    'Drama'
);

-- DVD 4: Interstellar
INSERT INTO itens_biblioteca (codigo, titulo, tipo, emprestado)
VALUES ('DVD004', 'Interstellar', 'DVD', FALSE);

INSERT INTO dvds (item_id, diretor, duracao_minutos, genero)
VALUES (
    (SELECT id FROM itens_biblioteca WHERE codigo = 'DVD004'),
    'Christopher Nolan',
    169,
    'Ficcao Cientifica'
);

-- Confirmar inserções
SELECT 'Seed concluido com sucesso!' AS status;
SELECT 'Total de itens: ' || COUNT(*) AS total FROM itens_biblioteca;
SELECT 'Livros: ' || COUNT(*) AS livros FROM livros;
SELECT 'Revistas: ' || COUNT(*) AS revistas FROM revistas;
SELECT 'DVDs: ' || COUNT(*) AS dvds FROM dvds;
