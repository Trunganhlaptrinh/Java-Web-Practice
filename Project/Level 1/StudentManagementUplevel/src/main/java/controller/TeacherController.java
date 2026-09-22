package controller;

import com.google.gson.Gson;
import dal.TeacherDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import model.Teacher;
import util.JsonUtil;
import util.Validation;

/**
 * GET    /api/teachers                    -> danh sach tat ca
 * GET    /api/teachers?id=1               -> chi tiet 1 teacher
 * GET    /api/teachers?departmentId=1     -> danh sach teacher theo department
 * POST   /api/teachers                    -> tao moi (body: {"name":"...","departmentId":1})
 * PUT    /api/teachers                    -> cap nhat (body: {"id":1,"name":"...","departmentId":1})
 * DELETE /api/teachers?id=1               -> xoa
 */
@WebServlet("/api/teachers")
public class TeacherController extends HttpServlet {

    private final TeacherDAO teacherDAO = new TeacherDAO();
    private final Validation validation = new Validation();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String idParam = request.getParameter("id");
        String departmentIdParam = request.getParameter("departmentId");
        try {
            if (idParam != null) {
                Teacher t = teacherDAO.getById(Integer.parseInt(idParam));
                if (t == null) {
                    response.getWriter().write(JsonUtil.error("Khong tim thay Teacher id=" + idParam));
                    return;
                }
                response.getWriter().write(JsonUtil.success("OK", t));
            } else if (departmentIdParam != null) {
                List<Teacher> list = teacherDAO.getByDepartment(Integer.parseInt(departmentIdParam));
                response.getWriter().write(JsonUtil.success("OK", list));
            } else {
                List<Teacher> list = teacherDAO.getAll();
                response.getWriter().write(JsonUtil.success("OK", list));
            }
        } catch (NumberFormatException e) {
            response.getWriter().write(JsonUtil.error("id khong hop le"));
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Teacher t = gson.fromJson(request.getReader(), Teacher.class);

        String err = validation.checkNameFormat(t == null ? null : t.getName(), "Ten teacher");
        if (err == null) {
            err = validation.checkPositiveInteger(t == null ? null : String.valueOf(t.getDepartmentId()), "departmentId");
        }
        if (err != null) {
            response.getWriter().write(JsonUtil.error(err));
            return;
        }

        try {
            int newId = teacherDAO.insert(t);
            t.setId(newId);
            response.getWriter().write(JsonUtil.success("Them thanh cong", t));
        } catch (SQLIntegrityConstraintViolationException e) {
            response.getWriter().write(JsonUtil.error("Department id=" + t.getDepartmentId() + " khong ton tai"));
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Teacher t = gson.fromJson(request.getReader(), Teacher.class);

        String err = validation.checkPositiveInteger(t == null ? null : String.valueOf(t.getId()), "id");
        if (err == null) {
            err = validation.checkNameFormat(t.getName(), "Ten teacher");
        }
        if (err == null) {
            err = validation.checkPositiveInteger(String.valueOf(t.getDepartmentId()), "departmentId");
        }
        if (err != null) {
            response.getWriter().write(JsonUtil.error(err));
            return;
        }

        try {
            boolean updated = teacherDAO.update(t);
            if (updated) {
                response.getWriter().write(JsonUtil.success("Cap nhat thanh cong", t));
            } else {
                response.getWriter().write(JsonUtil.error("Khong tim thay Teacher id=" + t.getId()));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            response.getWriter().write(JsonUtil.error("Department id=" + t.getDepartmentId() + " khong ton tai"));
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
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
            boolean deleted = teacherDAO.delete(Integer.parseInt(idParam));
            if (deleted) {
                // xoa Teacher se tu dong xoa theo cac dong course_teacher lien quan
                // (ON DELETE CASCADE), khong can xu ly gi them
                response.getWriter().write(JsonUtil.success("Xoa thanh cong", null));
            } else {
                response.getWriter().write(JsonUtil.error("Khong tim thay Teacher id=" + idParam));
            }
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
        }
    }
}
