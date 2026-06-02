CREATE SCHEMA IF NOT EXISTS biblioteca
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE biblioteca;

CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    senha VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ativo'
);

CREATE TABLE IF NOT EXISTS autores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    especialidade VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS livros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(150) NOT NULL,
    genero VARCHAR(80),
    ano INT NOT NULL,
    total_copias INT NOT NULL DEFAULT 1,
    copias_disponiveis INT NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS emprestimos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    livro_id INT NOT NULL,
    usuario_id INT NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_devolucao_prevista DATE NOT NULL,
    data_devolucao_real DATE,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (livro_id) REFERENCES livros(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Consultas de exemplo (não exigidas para criação do schema)
-- SELECT l.titulo, l.ISBN, l.ano_publicacao, l.status, 
--        c.nome AS categoria, a.nome AS autor, a.nacionalidade
-- FROM livro l
-- INNER JOIN categoria c ON l.id_categoria = c.id_Categoria
-- INNER JOIN autor a ON l.id_autor = a.id_autor;

-- SELECT titulo, ISBN, ano_publicacao
-- FROM livro
-- WHERE status = 'disponível';

-- SELECT e.id_emprestimo, e.data_emprestimo, e.data_prevista_devolucao,
--        u.nome AS usuario, l.titulo AS livro
-- FROM emprestimo e
-- INNER JOIN usuario u ON e.id_usuario = u.id_usuario
-- INNER JOIN livro l ON e.id_livro = l.id_livro
-- WHERE e.status = 'ativo';

-- SELECT e.Id_emprestimo, e.data_emprestimo, e.data_prevista_devolucao,
--        u.Nome AS usuario, u.Email, l.titulo AS livro,
--        DATEDIFF(CURDATE(), e.data_prevista_devolucao) AS dias_atraso
-- FROM emprestimo e
-- INNER JOIN usuario u ON e.id_usuario = u.id_usuario
-- INNER JOIN livro l ON e.id_livro = l.id_livro
-- WHERE e.status = 'ativo' 
--   AND e.data_prevista_devolucao < CURDATE();
  
-- SELECT c.nome AS categoria, COUNT(l.id_livro) AS total_livros
-- FROM categoria c
-- LEFT JOIN livro l ON c.Id_Categoria = l.Id_categoria
-- GROUP BY c.id_categoria, c.nome
-- ORDER BY total_livros DESC;

-- SELECT u.nome, u.email, COUNT(e.id_emprestimo) AS total_emprestimos
-- FROM usuario u
-- INNER JOIN emprestimo e ON u.Id_usuario = e.id_usuario
-- GROUP BY u.id_usuario, u.nome, u.email
-- ORDER BY total_emprestimos DESC
-- LIMIT 10;

-- SELECT l.titulo, COUNT(e.Id_Emprestimo) AS vezes_emprestado
-- FROM livro l
-- INNER JOIN emprestimo e ON l.id_livro = e.id_livro
-- GROUP BY l.id_livro, l.titulo
-- ORDER BY vezes_emprestado DESC
-- LIMIT 10;

-- SELECT AVG(DATEDIFF(data_devolucao, data_emprestimo)) AS media_dias_emprestimo
-- FROM emprestimo
-- WHERE data_devolucao IS NOT NULL;

-- SELECT l.titulo, a.nome AS autor, l.ano_publicacao, l.status
-- FROM livro l
-- INNER JOIN autor a ON l.id_autor = a.id_autor
-- WHERE l.titulo LIKE '%palavra%' 
--    OR a.nome LIKE '%palavra%';
   
-- SELECT DISTINCT u.nome, u.email, u.telefone
-- FROM usuario u
-- INNER JOIN emprestimo e ON u.id_usuario = e.id_usuario
-- WHERE e.status = 'ativo'
--   AND e.data_prevista_devolucao < CURDATE();
   
-- SELECT e.id_emprestimo, l.titulo, e.data_emprestimo, 
--        e.data_prevista_devolucao, e.data_devolucao, e.status
-- FROM emprestimo e
-- INNER JOIN livro l ON e.id_livro = l.id_livro
-- WHERE e.id_usuario = 1  -- Substituir pelo ID do usuário
-- ORDER BY e.data_emprestimo DESC;

-- SELECT YEAR(data_emprestimo) AS ano, 
--        MONTH(data_emprestimo) AS mes,
--        COUNT(*) AS total_emprestimos
-- FROM emprestimo
-- GROUP BY YEAR(data_emprestimo), MONTH(data_emprestimo)
-- ORDER BY ano DESC, mes DESC;

-- SELECT YEAR(ano_publicacao) AS ano, COUNT(*) AS total_livros
-- FROM livro
-- GROUP BY YEAR(ano_publicacao)
-- ORDER BY ano DESC;

-- SELECT l.titulo, l.status,
--        CASE 
--            WHEN e.status = 'ativo' THEN CONCAT('Emprestado até: ', DATE_FORMAT(e.data_prevista_devolucao, '%d/%m/%Y'))
--            ELSE 'Disponível'
--        END AS situacao
-- FROM livro l
-- LEFT JOIN emprestimo e ON l.id_livro = e.id_livro AND e.status = 'ativo'
-- WHERE l.id_livro = 1;  -- Substituir pelo ID do livro
