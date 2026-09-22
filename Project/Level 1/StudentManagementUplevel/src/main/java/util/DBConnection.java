package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Chiu trach nhiem duy nhat: mo ket noi toi MySQL.
 * Cac DAO se goi DBConnection.getConnection() moi khi can, dung xong tu dong
 * dong nho try-with-resources ben phia DAO (khong giu connection dung chung).
 */
public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/student_course_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root"; // TODO: doi thanh user MySQL cua ban
    private static final String PASSWORD = "root"; // TODO: doi thanh password MySQL cua ban

    public static Connection getConnection() throws SQLException {
        try {
            // nap driver tuong minh; tren Tomcat/Servlet hay bi "No suitable driver"
            // neu jar chua nam trong WEB-INF/lib
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // nem SQLException de Controller bat va hien thi len giao dien
            throw new SQLException("Khong tim thay MySQL driver (mysql-connector-j) trong WEB-INF/lib. "
                    + "Kiem tra pom.xml roi Clean and Build.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}