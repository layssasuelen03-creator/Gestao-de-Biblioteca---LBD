package br.com.biblioteca.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Responsável pela conexão JDBC com MySQL.
// Configurações podem ser informadas por:
// 1) variáveis de ambiente: BIBLIOTECA_DB_URL, BIBLIOTECA_DB_USER, BIBLIOTECA_DB_PASSWORD
// 2) propriedades do sistema: -Djdbc.url=... -Djdbc.user=... -Djdbc.password=...
// 3) arquivo db/db.properties ou db.properties
public class ConnectionFactory {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = loadConfig();
        URL = getSetting(props, "jdbc.url", "BIBLIOTECA_DB_URL", "DATABASE_URL",
                "jdbc:mysql://localhost:3306/biblioteca?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        USER = getSetting(props, "jdbc.user", "BIBLIOTECA_DB_USER", "DATABASE_USER", "root");
        PASSWORD = getSetting(props, "jdbc.password", "BIBLIOTECA_DB_PASSWORD", "DATABASE_PASSWORD", "");
    }

    private ConnectionFactory() {}

    private static Properties loadConfig() {
        Properties props = new Properties();
        String[] paths = {"db/db.properties", "db.properties"};
        for (String path : paths) {
            try (FileInputStream in = new FileInputStream(path)) {
                props.load(in);
                break;
            } catch (IOException ignored) {
            }
        }
        return props;
    }

    private static String getSetting(Properties props, String propName, String envName1, String envName2, String defaultValue) {
        String value = System.getProperty(propName);
        if (value == null || value.isBlank()) value = System.getenv(envName1);
        if (value == null || value.isBlank()) value = System.getenv(envName2);
        if (value == null || value.isBlank()) value = props.getProperty(propName);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL não encontrado. Adicione mysql-connector-j ao classpath ou à pasta lib.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try { conn.close(); }
            catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
