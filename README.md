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

- Executar a aplicação
- Compilar o projeto
- Fornecer suporte ao Java Swing
- Verificar instalação do Java

Para verificar se o Java já está na máquina, abra o terminal e execute:

`java -version`
e também:
`javac -version`

Se aparecer a versão instalada (por exemplo, `openjdk version "21.0.8"` e `javac 21.0.8`), o Java está instalado e funcionando corretamente.
Caso não esteja instalado, [clique aqui para baixar o JDK](https://www.oracle.com/br/java/technologies/downloads/)

<br>

✅ **VS Code + Extensões Java**

Para desenvolver ou editar o projeto, recomenda-se utilizar:

- Visual Studio Code

Além disso, recomenda-se instalar as extensões abaixo no VS Code:

- Extension Pack for Java
- Debugger for Java
- Language Support for Java

<br>

✅ **Banco de Dados**

O projeto utiliza integração com banco de dados MySQL.

Verifique se o MySQL Server está instalado com `mysql --version`. Caso não esteja, baixe [aqui](https://dev.mysql.com/downloads/mysql/)

<br>

✅ **Driver JDBC**

Também é necessário adicionar o driver JDBC do MySQL no projeto.

O arquivo necessário `mysql-connector-j-x.x.x.jar` pode ser baixado [aqui](https://dev.mysql.com/downloads/connector/j/)
Para Windows, escolha "Platform Independent".

O arquivo deve ser colocado na pasta:

lib/

<br>

✅ **Executando o Sistema**

Após baixar todos os softwares, drivers e extensões necessárias, é hora de executar o programa. Siga os passos abaixo:

1. Abra o MySQL Workbench e execute todo o conteúdo de `schema.sql`.
2. Configure a conexão com o banco:
   Abra o arquivo `src/br/biblioteca/connection/ConnectionFactory.java` e altere as configurações.
   `private static final String USER = "root";        // Seu usuário do MySQL
   private static final String PASSWORD = "";        // SUA SENHA DO MYSQL AQUI!`
   Coloque em PASSWORD a senha que definiu ao instalar o MySQL.
   
4. Execute o sistema:
   Para isso, existem duas opções.
   Pela IDE -descrever como fazer pela ide-, ou
   Pelo terminal:
   Compile com `javac -cp ` e em seguida
   execute com 





