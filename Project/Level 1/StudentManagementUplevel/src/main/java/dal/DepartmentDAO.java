package dal;

import util.DBConnection; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Department;

public class DepartmentDAO {

    public List<Department> getAll() throws SQLException {
        String sql = "SELECT id, name FROM departments ORDER BY name";
        List<Department> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Department getById(int id) throws SQLException {
        String sql = "SELECT id, name FROM departments WHERE id = ?";
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

    public int insert(Department d) throws SQLException {
        String sql = "INSERT INTO departments (name) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getName());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public boolean update(Department d) throws SQLException {
        String sql = "UPDATE departments SET name = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getName());
            ps.setInt(2, d.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM departments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // dung de check trung ten truoc khi insert/update, tra ve than thien hon
    // la de nem SQLIntegrityConstraintViolationException tu constraint UNIQUE
    public boolean existsByName(String name, Integer excludeId) throws SQLException {
        String sql = (excludeId == null)
                ? "SELECT 1 FROM departments WHERE name = ?"
                : "SELECT 1 FROM departments WHERE name = ? AND id <> ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            if (excludeId != null) {
                ps.setInt(2, excludeId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Department mapRow(ResultSet rs) throws SQLException {
        return new Department(rs.getInt("id"), rs.getString("name"));
    }
}
