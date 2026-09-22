package dal;

import util.DBConnection; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Teacher;

public class TeacherDAO {

    // JOIN sang departments de lay san department_name, tra ve cho JS hien thi
    // luon ma khong phai goi them 1 request nua
    private static final String SELECT_JOIN =
            "SELECT t.id, t.name, t.department_id, d.name AS department_name "
            + "FROM teachers t JOIN departments d ON t.department_id = d.id ";

    public List<Teacher> getAll() throws SQLException {
        String sql = SELECT_JOIN + "ORDER BY t.name";
        List<Teacher> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Teacher> getByDepartment(int departmentId) throws SQLException {
        String sql = SELECT_JOIN + "WHERE t.department_id = ? ORDER BY t.name";
        List<Teacher> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public Teacher getById(int id) throws SQLException {
        String sql = SELECT_JOIN + "WHERE t.id = ?";
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

    public int insert(Teacher t) throws SQLException {
        String sql = "INSERT INTO teachers (name, department_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getName());
            ps.setInt(2, t.getDepartmentId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public boolean update(Teacher t) throws SQLException {
        String sql = "UPDATE teachers SET name = ?, department_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            ps.setInt(2, t.getDepartmentId());
            ps.setInt(3, t.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM teachers WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Teacher mapRow(ResultSet rs) throws SQLException {
        Teacher t = new Teacher(rs.getInt("id"), rs.getString("name"), rs.getInt("department_id"));
        t.setDepartmentName(rs.getString("department_name"));
        return t;
    }
}
