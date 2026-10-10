package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = getDatabaseUrl();
    private static final String USER = getEnvOrDefault("DB_USER", "root");
    private static final String PASSWORD = getEnvOrDefault("DB_PASSWORD", "root");

    private DBConnection() {}

    private static String getDatabaseUrl() {
        String configuredUrl = getEnvOrDefault("DB_URL", null);
        if (configuredUrl != null) {
            return configuredUrl;
        }
        
        String host = getEnvOrDefault("DB_HOST", "localhost");
        String port = getEnvOrDefault("DB_PORT", "3306");
        String dbName = getEnvOrDefault("DB_NAME", "room_finder");
        
        return "jdbc:mysql://" + host + ":" + port + "/" + dbName 
                + "?useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    }
    
    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.trim().isEmpty()) ? defaultValue : value;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Khong tim thay driver MySQL", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
