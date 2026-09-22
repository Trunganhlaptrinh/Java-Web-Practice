package dal;

import util.DBConnection; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Course;

public class CourseDAO {

    private static final String SELECT_JOIN =
            "SELECT c.id, c.name, c.department_id, d.name AS department_name "
            + "FROM courses c JOIN departments d ON c.department_id = d.id ";

    public List<Course> getAll() throws SQLException {
        String sql = SELECT_JOIN + "ORDER BY c.name";
        List<Course> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // dung cho chuc nang search cua Course (giong findAndSort ban cu, nhung
    // khong can sap xep rieng vi da ORDER BY trong SQL)
    public List<Course> search(String keyword) throws SQLException {
        String sql = SELECT_JOIN + "WHERE c.name LIKE ? ORDER BY c.name";
        List<Course> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public Course getById(int id) throws SQLException {
        String sql = SELECT_JOIN + "WHERE c.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public int insert(Course c) throws SQLException {
        String sql = "INSERT INTO courses (name, department_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setInt(2, c.getDepartmentId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public boolean update(Course c) throws SQLException {
        String sql = "UPDATE courses SET name = ?, department_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setInt(2, c.getDepartmentId());
            ps.setInt(3, c.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // se nem SQLException (loi FK RESTRICT) neu con enrollment tham chieu toi
    // course nay -> Controller bat loi va tra message "khong the xoa, con
    // sinh vien dang ky"
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM courses WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Course mapRow(ResultSet rs) throws SQLException {
        Course c = new Course(rs.getInt("id"), rs.getString("name"), rs.getInt("department_id"));
        c.setDepartmentName(rs.getString("department_name"));
        return c;
    }
}
