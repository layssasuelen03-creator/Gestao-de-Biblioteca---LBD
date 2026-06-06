# Relatório do Projeto — Gestão de Biblioteca

**Disciplina:** Laboratório de Banco de Dados  
**Tema:** Sistema de Gerenciamento de Biblioteca  
**Tecnologias:** Java · Java Swing · JDBC · MySQL 

---

## 1. Descrição do sistema

O sistema Gestão de Biblioteca permite o controle digital do acervo e das operações de uma biblioteca. O problema resolvido é o acompanhamento manual e sujeito a erros de empréstimos, devoluções e disponibilidade de livros — o sistema centraliza essas operações com integridade garantida pelo banco de dados.

O escopo cobre:

- Cadastro e manutenção de livros, autores, editoras e categorias
- Cadastro de usuários com controle de status (ativo/inativo)
- Registro e controle de empréstimos com a validação de disponibilidade
- Registro de devoluções com a restauração automática do status do livro
- Relatórios e dashboard com indicadores operacionais
- Autenticação de acesso via login com verificação no banco

---

## 2. Modelagem do banco de dados

### 2.1 Tabelas e relacionamentos

O banco possui 6 tabelas:

| Tabela | Descrição |
|---|---|
| `usuario` | Membros da biblioteca que realizam empréstimos |
| `autor` | Autores dos livros cadastrados |
| `categoria` | Categorias/gêneros literários |
| `editora` | Editoras dos livros |
| `livro` | Acervo da biblioteca (referencia o autor, categoria e editora) |
| `emprestimo` | Registro de empréstimos ativos e histórico |

### 2.2 Relacionamento N:N

O relacionamento muitos-para-muitos ocorre entre `usuario` e `livro`: um usuário pode realizar vários empréstimos ao longo do tempo e um livro pode ser emprestado para vários usuários em momentos diferentes. Esse relacionamento é resolvido pela entidade associativa `emprestimo`, que além de conectar as duas entidades armazena atributos próprios do relacionamento: data de empréstimo, data prevista de devolução, data efetiva de devolução e status.

### 2.3 Restrições de integridade

Restrições implementadas no banco:

- `UNIQUE (email)` na tabela `usuario` — e-mail único por cadastro
- `UNIQUE (ISBN)` na tabela `livro` — ISBN não pode se repetir
- `CHECK (status IN ('disponível', 'emprestado', 'manutenção'))` na tabela `livro`
- `CHECK (data_prevista_devolucao >= data_emprestimo)` na tabela `emprestimo`
- `NOT NULL` em todos os campos obrigatórios
- Chaves estrangeiras com integridade referencial entre todas as tabelas relacionadas

### 2.4 Views

Três views foram criadas para simplificar consultas recorrentes:

- `vw_emprestimos_ativos` — lista todos os empréstimos em aberto com nome do usuário e título do livro
- `vw_emprestimos_atrasados` — filtra empréstimos cujo prazo já venceu, incluindo os dias de atraso
- `vw_total_livros_por_categoria` — contagem de livros agrupados por categoria

---

## 3. Arquitetura do back-end

O projeto adota separação em camadas:

| Camada | Pacote | Responsabilidade |
|---|---|---|
| Conexão | `connection` | `ConnectionFactory` — gerencia a conexão JDBC com o MySQL |
| Modelo | `model` | Classes POJO que mapeiam as entidades do banco |
| Acesso a dados | `dao` | Um DAO por entidade, com operações CRUD via `PreparedStatement` |
| Serviço | `service` | Validações e regras de negócio antes de chamar os DAOs |
| Interface | `ui` | Painéis Swing, componentes reutilizáveis e tela principal |

Todos os DAOs utilizam exclusivamente `PreparedStatement` — nenhuma query é construída por concatenação de strings.

---

## 4. Regras de negócio implementadas

**Regra 1 — Prazo de empréstimo (código Java, `EmprestimoService`):**  
Ao registrar um empréstimo, o sistema valida que a data prevista de devolução é futura (mínimo 1 dia) e que não ultrapassa 30 dias a partir da data do empréstimo. Caso contrário, uma exceção com mensagem amigável é lançada e o empréstimo não é registrado.

**Regra 2 — Disponibilidade do livro (banco de dados + código Java, `EmprestimoDAO`):**  
Antes de inserir o empréstimo, o sistema verifica com `SELECT ... FOR UPDATE` se o livro está com status `'disponível'`. Se não estiver, a operação é revertida com rollback. Ao confirmar o empréstimo, o status do livro é atualizado para `'emprestado'` na mesma transação. Na devolução, o status retorna para `'disponível'` automaticamente. A restrição `CHECK` no banco garante que o campo `status` só aceite os valores `'disponível'`, `'emprestado'` ou `'manutenção'`.

---

## 5. Interface gráfica

A interface foi desenvolvida em Java Swing com o look and feel FlatLaf para uma aparência moderna. Os componentes visuais reutilizáveis estão centralizados na classe `Components` e as cores e fontes no `Theme`, evitando duplicação de código.

Painéis implementados:

- **Login** — autenticação com verificação no banco
- **Dashboard** — cards com totais de livros, usuários e empréstimos ativos
- **Livros** — listagem, cadastro, edição e exclusão com busca por título/autor
- **Autores, Categorias, Editoras** — CRUD completo para cada entidade
- **Usuários** — cadastro e controle de status
- **Empréstimos** — registro com seleção de livro e usuário por combo dinâmico
- **Devoluções** — lista de empréstimos ativos com ação de devolução
- **Estoque** — visão geral do status de cada livro no acervo
- **Relatórios** — indicadores e gráfico de livros por categoria

---

## 6. Organização dos arquivos entregues

```
docs/   → DER conceitual, modelo lógico, diagrama de classes (Mermaid + PNG), este relatório
db/     → schema.sql (DDL completo com views e consultas de exemplo)
src/    → código-fonte Java organizado em camadas
lib/    → dependências (mysql-connector-j, flatlaf)
README.md → instruções de configuração e execução
```