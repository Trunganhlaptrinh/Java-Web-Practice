package dal;

import util.DBConnection; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.CourseTeacher;

public class CourseTeacherDAO {

    // lay danh sach giao vien dang day 1 course (dung khi hien thi detail course)
    public List<CourseTeacher> getTeachersByCourse(int courseId) throws SQLException {
        String sql = "SELECT ct.id, ct.course_id, ct.teacher_id, t.name AS teacher_name "
                + "FROM course_teacher ct JOIN teachers t ON ct.teacher_id = t.id "
                + "WHERE ct.course_id = ? ORDER BY t.name";
        List<CourseTeacher> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public boolean exists(int courseId, int teacherId) throws SQLException {
        String sql = "SELECT 1 FROM course_teacher WHERE course_id = ? AND teacher_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // gan 1 giao vien vao 1 course
    public int assign(int courseId, int teacherId) throws SQLException {
        String sql = "INSERT INTO course_teacher (course_id, teacher_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, courseId);
            ps.setInt(2, teacherId);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    // go 1 giao vien khoi 1 course
    public boolean unassign(int courseId, int teacherId) throws SQLException {
        String sql = "DELETE FROM course_teacher WHERE course_id = ? AND teacher_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, teacherId);
            return ps.executeUpdate() > 0;
        }
    }

    private CourseTeacher mapRow(ResultSet rs) throws SQLException {
        CourseTeacher ct = new CourseTeacher(rs.getInt("id"), rs.getInt("course_id"), rs.getInt("teacher_id"));
        ct.setTeacherName(rs.getString("teacher_name"));
        return ct;
    }
}
