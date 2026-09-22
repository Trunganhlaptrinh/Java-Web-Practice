package dal;

import util.DBConnection; 
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Enrollment;

public class EnrollmentDAO {

    // JOIN ca students va courses de tra ve san ten, JS khong phai lookup rieng
    private static final String SELECT_JOIN =
            "SELECT e.id, e.student_id, e.course_id, e.enrolled_date, "
            + "s.name AS student_name, c.name AS course_name "
            + "FROM enrollments e "
            + "JOIN students s ON e.student_id = s.id "
            + "JOIN courses c ON e.course_id = c.id ";

    public List<Enrollment> getAll() throws SQLException {
        String sql = SELECT_JOIN + "ORDER BY e.enrolled_date DESC";
        List<Enrollment> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // xem cac mon 1 sinh vien da dang ky
    public List<Enrollment> getByStudent(int studentId) throws SQLException {
        String sql = SELECT_JOIN + "WHERE e.student_id = ? ORDER BY e.enrolled_date DESC";
        List<Enrollment> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    // xem cac sinh vien da dang ky 1 mon
    public List<Enrollment> getByCourse(int courseId) throws SQLException {
        String sql = SELECT_JOIN + "WHERE e.course_id = ? ORDER BY e.enrolled_date DESC";
        List<Enrollment> list = new ArrayList<>();
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

    // check dang ky trung truoc khi insert (bo sung cho UNIQUE constraint duoi DB,
    // de Controller tra loi loi than thien thay vi de SQLException bung len)
    public boolean exists(int studentId, int courseId) throws SQLException {
        String sql = "SELECT 1 FROM enrollments WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int insert(Enrollment e) throws SQLException {
        String sql = "INSERT INTO enrollments (student_id, course_id, enrolled_date) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, e.getStudentId());
            ps.setInt(2, e.getCourseId());
            ps.setDate(3, Date.valueOf(e.getEnrolledDate())); // enrolledDate dang "yyyy-MM-dd"
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    // huy dang ky (theo id cua ban ghi enrollment)
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM enrollments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Enrollment mapRow(ResultSet rs) throws SQLException {
        Enrollment e = new Enrollment(
                rs.getInt("id"),
                rs.getInt("student_id"),
                rs.getInt("course_id"),
                rs.getDate("enrolled_date").toString());
        e.setStudentName(rs.getString("student_name"));
        e.setCourseName(rs.getString("course_name"));
        return e;
    }
}
