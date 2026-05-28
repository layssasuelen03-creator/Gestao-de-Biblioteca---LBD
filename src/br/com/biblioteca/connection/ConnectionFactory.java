package br.com.biblioteca.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//Responsável pela onexão JDBC com MySQL
//Obs: Deve ser alterado a URL, USER e PASSWORD conforme seu ambiente
public class ConnectionFactory {

    private static final String URL      = "jdbc:mysql://localhost:3306/biblioteca?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER     = "root";
    private static final String PASSWORD = "";   // sua senha do MySQL

    private ConnectionFactory() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL não encontrado. Adicione mysql-connector-j ao /lib: " + e.getMessage());
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try { conn.close(); }
            catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
