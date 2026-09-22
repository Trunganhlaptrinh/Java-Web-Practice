package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dal.CourseTeacherDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import model.CourseTeacher;
import util.JsonUtil;
import util.Validation;

/**
 * GET    /api/course-teachers?courseId=1              -> ds giao vien dang day course nay
 * POST   /api/course-teachers                          -> gan giao vien (body: {"courseId":1,"teacherId":2})
 * DELETE /api/course-teachers?courseId=1&teacherId=2   -> go giao vien khoi course
 */
@WebServlet("/api/course-teachers")
public class CourseTeacherController extends HttpServlet {

    private final CourseTeacherDAO courseTeacherDAO = new CourseTeacherDAO();
    private final Validation validation = new Validation();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String courseIdParam = request.getParameter("courseId");
        String err = validation.checkPositiveInteger(courseIdParam, "courseId");
        if (err != null) {
            response.getWriter().write(JsonUtil.error(err));
            return;
        }

        try {
            List<CourseTeacher> list = courseTeacherDAO.getTeachersByCourse(Integer.parseInt(courseIdParam));
            response.getWriter().write(JsonUtil.success("OK", list));
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        JsonObject body = gson.fromJson(request.getReader(), JsonObject.class);
        String courseIdStr = body != null && body.has("courseId") ? body.get("courseId").getAsString() : null;
        String teacherIdStr = body != null && body.has("teacherId") ? body.get("teacherId").getAsString() : null;

        String err = validation.checkPositiveInteger(courseIdStr, "courseId");
        if (err == null) {
            err = validation.checkPositiveInteger(teacherIdStr, "teacherId");
        }
        if (err != null) {
            response.getWriter().write(JsonUtil.error(err));
            return;
        }

        int courseId = Integer.parseInt(courseIdStr);
        int teacherId = Integer.parseInt(teacherIdStr);

        try {
            if (courseTeacherDAO.exists(courseId, teacherId)) {
                response.getWriter().write(JsonUtil.error("Giao vien nay da duoc gan cho course nay roi"));
                return;
            }
            courseTeacherDAO.assign(courseId, teacherId);
            response.getWriter().write(JsonUtil.success("Gan giao vien thanh cong", null));
        } catch (SQLIntegrityConstraintViolationException e) {
            response.getWriter().write(JsonUtil.error("courseId hoac teacherId khong ton tai"));
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String courseIdParam = request.getParameter("courseId");
        String teacherIdParam = request.getParameter("teacherId");

        String err = validation.checkPositiveInteger(courseIdParam, "courseId");
        if (err == null) {
            err = validation.checkPositiveInteger(teacherIdParam, "teacherId");
        }
        if (err != null) {
            response.getWriter().write(JsonUtil.error(err));
            return;
        }

        try {
            boolean removed = courseTeacherDAO.unassign(
                    Integer.parseInt(courseIdParam), Integer.parseInt(teacherIdParam));
            if (removed) {
                response.getWriter().write(JsonUtil.success("Go giao vien thanh cong", null));
            } else {
                response.getWriter().write(JsonUtil.error("Khong tim thay lien ket course-teacher nay"));
            }
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
        }
    }
}
