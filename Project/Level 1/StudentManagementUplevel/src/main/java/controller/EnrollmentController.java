package controller;

import com.google.gson.Gson;
import dal.EnrollmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import model.Enrollment;
import util.JsonUtil;
import util.Validation;

/**
 * GET    /api/enrollments                 -> danh sach tat ca dang ky
 * GET    /api/enrollments?studentId=1     -> cac mon 1 sinh vien da dang ky
 * GET    /api/enrollments?courseId=1      -> cac sinh vien da dang ky 1 mon
 * POST   /api/enrollments                 -> dang ky mon
 *                                            (body: {"studentId":1,"courseId":2,"enrolledDate":"2026-09-19"})
 *                                            enrolledDate co the bo trong -> mac dinh la hom nay
 * DELETE /api/enrollments?id=1            -> huy dang ky (theo id cua enrollment)
 */
@WebServlet("/api/enrollments")
public class EnrollmentController extends HttpServlet {

    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final Validation validation = new Validation();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String studentIdParam = request.getParameter("studentId");
        String courseIdParam = request.getParameter("courseId");
        try {
            if (studentIdParam != null) {
                String err = validation.checkPositiveInteger(studentIdParam, "studentId");
                if (err != null) {
                    response.getWriter().write(JsonUtil.error(err));
                    return;
                }
                List<Enrollment> list = enrollmentDAO.getByStudent(Integer.parseInt(studentIdParam.trim()));
                response.getWriter().write(JsonUtil.success("OK", list));
            } else if (courseIdParam != null) {
                String err = validation.checkPositiveInteger(courseIdParam, "courseId");
                if (err != null) {
                    response.getWriter().write(JsonUtil.error(err));
                    return;
                }
                List<Enrollment> list = enrollmentDAO.getByCourse(Integer.parseInt(courseIdParam.trim()));
                response.getWriter().write(JsonUtil.success("OK", list));
            } else {
                List<Enrollment> list = enrollmentDAO.getAll();
                response.getWriter().write(JsonUtil.success("OK", list));
            }
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Enrollment e = gson.fromJson(request.getReader(), Enrollment.class);

        String err = validation.checkPositiveInteger(e == null ? null : String.valueOf(e.getStudentId()), "studentId");
        if (err == null) {
            err = validation.checkPositiveInteger(String.valueOf(e.getCourseId()), "courseId");
        }
        if (err != null) {
            response.getWriter().write(JsonUtil.error(err));
            return;
        }

        // khong gui enrolledDate thi lay ngay hom nay, co gui thi phai dung yyyy-MM-dd
        if (e.getEnrolledDate() == null || e.getEnrolledDate().trim().isEmpty()) {
            e.setEnrolledDate(LocalDate.now().toString());
        } else {
            err = validation.checkDateFormat(e.getEnrolledDate(), "enrolledDate");
            if (err != null) {
                response.getWriter().write(JsonUtil.error(err));
                return;
            }
            e.setEnrolledDate(e.getEnrolledDate().trim());
        }

        try {
            // business rule: khong cho dang ky trung (student + course)
            if (enrollmentDAO.exists(e.getStudentId(), e.getCourseId())) {
                response.getWriter().write(JsonUtil.error("Sinh vien nay da dang ky mon hoc nay roi"));
                return;
            }
            int newId = enrollmentDAO.insert(e);
            e.setId(newId);
            response.getWriter().write(JsonUtil.success("Dang ky thanh cong", e));
        } catch (SQLIntegrityConstraintViolationException ex) {
            // FK fail -> studentId hoac courseId khong ton tai
            response.getWriter().write(JsonUtil.error("studentId hoac courseId khong ton tai"));
        } catch (SQLException ex) {
            response.getWriter().write(JsonUtil.error("Loi database: " + ex.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String idParam = request.getParameter("id");
        String err = validation.checkPositiveInteger(idParam, "id");
        if (err != null) {
            response.getWriter().write(JsonUtil.error(err));
            return;
        }

        try {
            boolean deleted = enrollmentDAO.delete(Integer.parseInt(idParam.trim()));
            if (deleted) {
                response.getWriter().write(JsonUtil.success("Huy dang ky thanh cong", null));
            } else {
                response.getWriter().write(JsonUtil.error("Khong tim thay Enrollment id=" + idParam));
            }
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
        }
    }
}
