create schema biblioteca; 
use biblioteca;

create table usuario (
	id_usuario int auto_increment primary key,
	nome varchar(100) not null,
	email varchar(100) not null,
	telefone varchar(15) not null,
	senha varchar(255),
	status varchar(20) default 'ativo', 
    constraint unique_email UNIQUE (email)
);

create table autor(
	id_autor int auto_increment primary key,
	nome varchar(200) not null,
	nacionalidade varchar(100) not null
);

create table categoria(
	id_categoria int auto_increment primary key,
	nome varchar(100) not null,
	descricao varchar(100) not null
);

create table editora (
	id_editora int auto_increment primary key, 
    nome varchar(80),
    cidade varchar(80), 
    pais varchar(80)
); 

create table livro(
	id_livro int auto_increment primary key,
	titulo varchar(100) not null,
	ISBN varchar(20) not null, 
	ano_publicacao date not null,
	status varchar(100) not null,
	id_categoria int not null,
	id_autor int not null,
    id_editora int not null, 
	foreign key (id_categoria) references categoria(id_categoria),
	foreign key (id_autor) references autor(id_autor), 
    foreign key (id_editora) references editora(id_editora), 
      constraint unique_isbn UNIQUE (ISBN),
    constraint chk_livro_status CHECK (status IN ('disponível', 'emprestado', 'manutenção'))
);

create table emprestimo(
	id_emprestimo int  auto_increment primary key,
	data_emprestimo date not null,
	data_prevista_devolucao date not null,
	data_devolucao date null,
	status varchar(100) not null,
	id_usuario int not null,
	id_livro int not null,
	foreign key (id_usuario) references usuario(id_usuario),
	foreign key (id_livro) references livro(id_livro),
	constraint chk_datas_emprestimo CHECK (data_prevista_devolucao >= data_emprestimo)
);

select * from usuario; 
-- Listar livro com autores e categorias --

SELECT l.titulo, l.ISBN, l.ano_publicacao, l.status, 
       c.nome AS categoria, a.nome AS autor, a.nacionalidade
FROM livro l
INNER JOIN categoria c ON l.id_categoria = c.id_categoria
INNER JOIN autor a ON l.id_autor = a.id_autor;

-- Listar livros disponiveis para emprestimo --

SELECT titulo, ISBN, ano_publicacao
FROM livro
WHERE status = 'disponível';

-- Emprestimos Ativos --

SELECT e.id_emprestimo, e.data_emprestimo, e.data_prevista_devolucao,
       u.nome AS usuario, l.titulo AS livro
FROM emprestimo e
INNER JOIN usuario u ON e.id_usuario = u.id_usuario
INNER JOIN livro l ON e.id_livro = l.id_livro
WHERE e.status = 'ativo';

-- Emprestimos atrasados --

SELECT e.Id_emprestimo, e.data_emprestimo, e.data_prevista_devolucao,
       u.Nome AS usuario, u.Email, l.titulo AS livro,
       DATEDIFF(CURDATE(), e.data_prevista_devolucao) AS dias_atraso
FROM emprestimo e
INNER JOIN usuario u ON e.id_usuario = u.id_usuario
INNER JOIN livro l ON e.id_livro = l.id_livro
WHERE e.status = 'ativo' 
  AND e.data_prevista_devolucao < CURDATE();
  
  -- Total de livros por categoria --
  SELECT c.nome AS categoria, COUNT(l.id_livro) AS total_livros
FROM categoria c
LEFT JOIN livro l ON c.id_categoria = l.id_categoria
GROUP BY c.id_categoria, c.nome
ORDER BY total_livros DESC;

-- Usuarios com mais emprestimos --
SELECT u.nome, u.email, COUNT(e.id_emprestimo) AS total_emprestimos
FROM usuario u
INNER JOIN emprestimo e ON u.Id_usuario = e.id_usuario
GROUP BY u.id_usuario, u.nome, u.email
ORDER BY total_emprestimos DESC
LIMIT 10;

-- Livros mais emprestados --
SELECT l.titulo, COUNT(e.Id_Emprestimo) AS vezes_emprestado
FROM livro l
INNER JOIN emprestimo e ON l.id_livro = e.id_livro
GROUP BY l.id_livro, l.titulo
ORDER BY vezes_emprestado DESC
LIMIT 10;

-- Medias de dias de devolucao --
SELECT AVG(DATEDIFF(data_devolucao, data_emprestimo)) AS media_dias_emprestimo
FROM emprestimo
WHERE data_devolucao IS NOT NULL;

-- Buscar livros por titulo ou autor --
SELECT l.titulo, a.nome AS autor, l.ano_publicacao, l.status
FROM livro l
INNER JOIN autor a ON l.id_autor = a.id_autor
WHERE l.titulo LIKE '%palavra%' 
   OR a.nome LIKE '%palavra%';
   
-- Usuários com emprestimos pendentes --
SELECT DISTINCT u.nome, u.email, u.telefone
FROM usuario u
INNER JOIN emprestimo e ON u.id_usuario = e.id_usuario
WHERE e.status = 'ativo'
  AND e.data_prevista_devolucao < CURDATE();
  
-- Historico de emprestimo de um usuário --
SELECT e.id_emprestimo, l.titulo, e.data_emprestimo, 
       e.data_prevista_devolucao, e.data_devolucao, e.status
FROM emprestimo e
INNER JOIN livro l ON e.id_livro = l.id_livro
WHERE e.id_usuario = 1  -- Substituir pelo ID do usuário
ORDER BY e.data_emprestimo DESC;

-- Emprestimo por mes/ano--
SELECT YEAR(data_emprestimo) AS ano, 
       MONTH(data_emprestimo) AS mes,
       COUNT(*) AS total_emprestimos
FROM emprestimo
GROUP BY YEAR(data_emprestimo), MONTH(data_emprestimo)
ORDER BY ano DESC, mes DESC;

-- Livros ppr anos de publicação --

SELECT YEAR(ano_publicacao) AS ano, COUNT(*) AS total_livros
FROM livro
GROUP BY YEAR(ano_publicacao)
ORDER BY ano DESC;

-- disponibilidade de livro --
SELECT l.titulo, l.status,
       CASE 
           WHEN e.status = 'ativo' THEN 'Emprestado até: ' || DATE_FORMAT(e.data_prevista_devolucao, '%d/%m/%Y')
           ELSE 'Disponível'
       END AS situacao
FROM livro l
LEFT JOIN emprestimo e ON l.id_livro = e.id_livro AND e.status = 'ativo'
WHERE l.id_livro = 1;  -- Substituir pelo ID do livro


-- Simplifica a busca de quem está com livros pendentes em aberto hoje
CREATE VIEW vw_emprestimos_ativos AS
SELECT e.id_emprestimo, e.data_emprestimo, e.data_prevista_devolucao,
       u.nome AS usuario, u.email, l.titulo AS livro
FROM emprestimo e
INNER JOIN usuario u ON e.id_usuario = u.id_usuario
INNER JOIN livro l ON e.id_livro = l.id_livro
WHERE e.status = 'ativo';


-- Mostra o total de livro por categoria
CREATE VIEW vw_total_livros_por_categoria AS
SELECT 
    c.nome AS nome_categoria, 
    COUNT(l.id_livro) AS quantidade_livros
FROM categoria c
LEFT JOIN livro l ON c.id_categoria = l.id_categoria
GROUP BY c.id_categoria, c.nome
ORDER BY quantidade_livros DESC LIMIT 10; 


-- Monitora os emprestimos atrasados
CREATE VIEW vw_emprestimos_atrasados AS
SELECT e.id_emprestimo, e.data_emprestimo, e.data_prevista_devolucao,
       u.nome AS usuario, u.email, u.telefone, l.titulo AS livro,
       DATEDIFF(CURDATE(), e.data_prevista_devolucao) AS dias_atraso
FROM emprestimo e
INNER JOIN usuario u ON e.id_usuario = u.id_usuario
INNER JOIN livro l ON e.id_livro = l.id_livro
WHERE e.status = 'ativo' AND e.data_prevista_devolucao < CURDATE();