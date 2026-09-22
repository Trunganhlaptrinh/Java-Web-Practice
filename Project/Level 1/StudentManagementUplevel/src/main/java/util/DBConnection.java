package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Chiu trach nhiem duy nhat: mo ket noi toi MySQL.
 * URL/USER/PASSWORD doc tu bien moi truong (docker-compose se set) - neu
 * khong co bien moi truong nao (vd chay truc tiep trong NetBeans) thi fallback
 * ve gia tri mac dinh cho localhost, khong can sua code qua lai giua 2 moi truong.
 */
public class DBConnection {

    private static final String URL = getEnvOrDefault("DB_URL",
            "jdbc:mysql://localhost:3306/student_course_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
    private static final String USER = getEnvOrDefault("DB_USER", "root");
    private static final String PASSWORD = getEnvOrDefault("DB_PASSWORD", "root");

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    public static Connection getConnection() throws SQLException {
        try {
            // nap driver tuong minh; tren Tomcat/Servlet hay bi "No suitable driver"
            // neu jar chua nam trong WEB-INF/lib
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Khong tim thay MySQL driver (mysql-connector-j) trong WEB-INF/lib. "
                    + "Kiem tra pom.xml roi Clean and Build.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
