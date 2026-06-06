# Gestão de Biblioteca — LBD

Sistema de gerenciamento de acervo bibliográfico desenvolvido com Java Swing e MySQL, como projeto da disciplina de Laboratório de Banco de Dados.

---

## Sobre o Projeto

O projeto Gestão de Biblioteca - LBD foi desenvolvido com foco em organização, controle e gerenciamento de acervo bibliográfico.

A aplicação possui interface gráfica desenvolvida em Java Swing, arquitetura organizada em camadas e integração com banco de dados MySQL.

O sistema permite:

- Cadastro de livros, autores, editoras e categorias
- Cadastro e gerenciamento de usuários
- Controle de empréstimos com a verificação da disponibilidade
- Registro de devoluções com a atualização automática do status do livro
- Dashboard com informações do sistema em tempo real
- Controle de estoque
- Relatórios
- Sistema de login com autenticação via banco de dados

---

## Estrutura do projeto

```
Gestão de Biblioteca - LBD/
├── db/
│   └── schema.sql              # DDL completo (tabelas, restrições, views, consultas)
├── docs/
│   ├── DER-modelo-conceitual.png
│   ├── DER-modelo-logico.png
│   ├── diagrama-classes.mermaid
│   ├── diagrama-de-classes-final.png
│   └── relatorio.md
├── lib/
│   ├── mysql-connector-j-9.7.0.jar
│   └── flatlaf-3.7.1.jar
├── src/
│   └── br/com/biblioteca/
│       ├── connection/         # ConnectionFactory (JDBC)
│       ├── model/              # Entidades do domínio
│       ├── dao/                # Acesso ao banco com PreparedStatement
│       ├── service/            # Regras de negócio
│       └── ui/                 # Painéis Swing e tela principal
└── README.md
```

---

## Requisitos para Executar o Projeto

Para que o sistema funcione corretamente no computador, é necessário possuir alguns softwares instalados.

<br>

✅ **Java JDK**

O sistema foi desenvolvido utilizando Java Swing, portanto é necessário instalar o JDK 17 ou superior.

O Java é responsável por executar a aplicação, compilar o projeto e fornecer suporte ao Java Swing.

Para verificar se o Java já está instalado, abra o terminal e execute:

```bash
java -version
javac -version
```

Se aparecer a versão instalada (por exemplo, `openjdk version "21.0.8"` e `javac 21.0.8`), o Java está funcionando corretamente. Caso não esteja instalado, [clique aqui para baixar o JDK](https://www.oracle.com/br/java/technologies/downloads/).

<br>

✅ **VS Code + Extensões Java**

Para desenvolver ou editar o projeto, recomenda-se utilizar o Visual Studio Code com as seguintes extensões:

- Extension Pack for Java
- Debugger for Java
- Language Support for Java

<br>

✅ **Banco de Dados**

O projeto utiliza integração com banco de dados MySQL. Verifique se o MySQL Server está instalado com:

```bash
mysql --version
```

Caso não esteja instalado, [clique aqui para baixar](https://dev.mysql.com/downloads/mysql/).

<br>

✅ **Driver JDBC e bibliotecas**

O driver JDBC e a biblioteca de interface já estão incluídos na pasta `lib/` do projeto — **não é necessário baixar nada separadamente**:

| Arquivo | Finalidade |
|---|---|
| `mysql-connector-j-9.7.0.jar` | Driver JDBC — permite que o Java se comunique com o MySQL |
| `flatlaf-3.7.1.jar` | Look and feel moderno para o Swing |

> ⚠️ O driver JDBC **não substitui** o MySQL Server. O banco precisa estar instalado e rodando na máquina. Se preferir baixar uma versão diferente do conector, [clique aqui](https://dev.mysql.com/downloads/connector/j/) e escolha "Platform Independent". Coloque o arquivo `.jar` na pasta `lib/`.

---

## Executando o Sistema

Após instalar o Java e o MySQL, siga os passos abaixo:

<br>

**1. Criar o banco de dados**

Abra o MySQL Workbench (ou outro cliente MySQL) e execute todo o conteúdo do arquivo:

```
db/schema.sql
```

Esse script cria o schema `biblioteca`, todas as tabelas, restrições e views automaticamente. Não é necessário criar nada manualmente antes.

<br>

**2. Configurar a conexão com o banco**

Abra o arquivo:

```
src/br/com/biblioteca/connection/ConnectionFactory.java
```

Localize as constantes abaixo e configure com as credenciais do seu MySQL local:

```java
private static final String USER     = "root";       // Seu usuário do MySQL
private static final String PASSWORD = "";            // SUA SENHA DO MYSQL AQUI
```

Coloque em `PASSWORD` a senha que você definiu ao instalar o MySQL. Se o banco estiver em outra porta ou com outro usuário, ajuste também a constante `URL`.

<br>

**3. Criar o primeiro usuário de acesso**

O login autentica contra a tabela `usuario` do banco. Para entrar no sistema pela primeira vez, insira um usuário com o seguinte comando SQL no MySQL Workbench:

```sql
INSERT INTO biblioteca.usuario (nome, email, telefone, senha, status)
VALUES ('Admin', 'admin@biblioteca.com', '(00) 00000-0000', 'admin123', 'ativo');
```

<br>

**4. Executar o sistema**

Abra o projeto no VS Code com as extensões Java instaladas e execute a classe principal:

```
src/br/com/biblioteca/Main.java
```

A tela de login será exibida. Use as credenciais do usuário criado no passo anterior.

<br>

**Configurando os JARs no classpath (VS Code)**

Se o VS Code não reconhecer as bibliotecas da pasta `lib/` automaticamente, abra ou crie o arquivo `.vscode/settings.json` e adicione:

```json
{
  "java.project.referencedLibraries": [
    "lib/**/*.jar"
  ]
}
```

**Executando pelo terminal**

Se preferir compilar e executar pelo terminal, na raiz do projeto execute:

```bash
# Windows
javac -cp "lib/*" -d bin -sourcepath src src/br/com/biblioteca/Main.java
java -cp "bin;lib/*" br.com.biblioteca.Main

# Linux / macOS
javac -cp "lib/*" -d bin -sourcepath src src/br/com/biblioteca/Main.java
java -cp "bin:lib/*" br.com.biblioteca.Main
```

> Crie a pasta `bin/` manualmente antes de compilar, caso ela não exista: `mkdir bin`