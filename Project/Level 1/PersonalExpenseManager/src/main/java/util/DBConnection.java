package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = getEnvOrDefault(
            "DB_URL",
            "jdbc:mysql://localhost:3306/personal_expense_manager?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8");
    private static final String USER = getEnvOrDefault("DB_USER", "root");
    private static final String PASSWORD = getEnvOrDefault("DB_PASSWORD", "root");

    private DBConnection() {
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
