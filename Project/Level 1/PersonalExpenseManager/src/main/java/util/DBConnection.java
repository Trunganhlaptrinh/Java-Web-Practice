package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = getDatabaseUrl();
    private static final String USER = getEnvOrDefault("DB_USER", "MYSQL_ADDON_USER", "root");
    private static final String PASSWORD = getEnvOrDefault("DB_PASSWORD", "MYSQL_ADDON_PASSWORD", "root");

    private DBConnection() {
    }

    private static String getDatabaseUrl() {
        String configuredUrl = getEnvOrDefault("DB_URL", null);
        if (configuredUrl != null) {
            return configuredUrl;
        }

        String host = getEnvOrDefault("MYSQL_ADDON_HOST", null);
        if (host != null) {
            String port = getEnvOrDefault("MYSQL_ADDON_PORT", "3306");
            String database = getEnvOrDefault("MYSQL_ADDON_DB", "personal_expense_manager");
            return "jdbc:mysql://" + host + ":" + port + "/" + database
                    + "?sslMode=REQUIRED&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
        }

        return "jdbc:mysql://localhost:3306/personal_expense_manager"
                + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
                + "&useUnicode=true&characterEncoding=UTF-8";
    }

    private static String getEnvOrDefault(String primaryKey, String fallbackKey, String defaultValue) {
        String value = getEnvOrDefault(primaryKey, null);
        if (value != null) {
            return value;
        }
        return fallbackKey == null ? defaultValue : getEnvOrDefault(fallbackKey, defaultValue);
    }

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException exception) {
            throw new SQLException("Không tìm thấy MySQL driver.", exception);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
