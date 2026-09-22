package dal;

import util.DBConnection; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Student;

// DAO làm việc với controller, lấy dữ liệu từ database và trả cho controller


public class StudentDAO {
    
    // Lấy tất cả sinh viên
    public List<Student> getAll() throws SQLException {
        String sql = "SELECT id, name, semester FROM students ORDER BY name";
        
        // tạo list chứa sinh viên từ database và chuyển vào thành dữ liệu trong list
        // db là 1 | Nguyen Van An | HK1 2026 --> dự liệu trong list là Student(1, "Nguyen Van An", "HK1 2026")
        List<Student> list = new ArrayList<>();
        
        // tiếp tục gọi qua DBconnect để tiếp tục lấy db
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }
    // Tìm sinh viên theo tên
    // tim theo mot phan ten, sap xep A-Z (giu dung nghiep vu tu ban console cu)
    public List<Student> search(String keyword) throws SQLException {
        String sql = "SELECT id, name, semester FROM students WHERE name LIKE ? ORDER BY name";
        List<Student> list = new ArrayList<>();
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

    // Lấy 1 sinh viên theo ID
    public Student getById(int id) throws SQLException {
        String sql = "SELECT id, name, semester FROM students WHERE id = ?";
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

    public int insert(Student s) throws SQLException {
        String sql = "INSERT INTO students (name, semester) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getSemester());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public boolean update(Student s) throws SQLException {
        String sql = "UPDATE students SET name = ?, semester = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getSemester());
            ps.setInt(3, s.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    
    // Chuyển dữ liệu SQL thành object Student
    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(rs.getInt("id"), rs.getString("name"), rs.getString("semester"));
    }
}
