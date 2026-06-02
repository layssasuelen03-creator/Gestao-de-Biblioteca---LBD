**Sobre o Projeto**

O projeto Gestão de Biblioteca - LBD foi desenvolvido com foco em organização, controle e gerenciamento de acervo bibliográfico.

A aplicação possui interface gráfica desenvolvida em Java Swing, arquitetura organizada em camadas e integração com banco de dados.

O sistema permite:

- Cadastro de livros
- Cadastro de autores
- Cadastro de usuários
- Controle de empréstimos
- Registro de devoluções
- Organização por categorias
- Dashboard com informações do sistema
- Controle de estoque
- Relatórios
- Sistema de login

<br>

**Requisitos para Executar o Projeto**

Para que o sistema funcione corretamente no computador, é necessário possuir alguns softwares instalados.

✅ **Java JDK**

O sistema foi desenvolvido utilizando Java Swing, portanto é necessário instalar:

JDK 17 ou superior

O Java é responsável por:

Executar a aplicação
Compilar o projeto
Fornecer suporte ao Java Swing
Verificar instalação do Java

Abra o terminal e execute:

java -version

E:

javac -version

Se aparecer a versão instalada, o Java está funcionando corretamente.

<br>

✅ **VS Code + Extensões Java**

Para desenvolver ou editar o projeto, recomenda-se utilizar:

- Visual Studio Code
- Extensões necessárias:
- Extension Pack for Java
- Debugger for Java
- Language Support for Java

<br>

✅ **Banco de Dados**

O projeto utiliza integração com banco de dados MySQL.

Instale:

- MySQL Server

<br>

✅ **Driver JDBC**

Também é necessário adicionar o driver JDBC do MySQL no projeto.

Arquivo necessário:

mysql-connector-j-x.x.x.jar

O arquivo deve ser colocado na pasta:

lib/

<br>

✅ **Configuração do banco**

A conexão pode ser configurada de três maneiras:

1) Variáveis de ambiente
   - `BIBLIOTECA_DB_URL`
   - `BIBLIOTECA_DB_USER`
   - `BIBLIOTECA_DB_PASSWORD`

2) Propriedades do sistema Java
   - `-Djdbc.url=...`
   - `-Djdbc.user=...`
   - `-Djdbc.password=...`

3) Arquivo de propriedades opcional
   - `db/db.properties` ou `db.properties`
   - Exemplo:
     ```
     jdbc.url=jdbc:mysql://localhost:3306/biblioteca?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
     jdbc.user=root
     jdbc.password=senha
     ```

<br>

✅ **Executando o Sistema**

Após configurar tudo:

- Abra o projeto no VS Code
- Configure o banco de dados usando `db/schema.sql`
- Execute a aplicação a partir da classe `br.com.biblioteca.Main`
