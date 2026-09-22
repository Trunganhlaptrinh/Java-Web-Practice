package controller;

import com.google.gson.Gson;
import dal.CourseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import model.Course;
import util.JsonUtil;
import util.Validation;

/**
 * GET    /api/courses                 -> danh sach tat ca
 * GET    /api/courses?id=1            -> chi tiet 1 course
 * GET    /api/courses?search=java     -> tim theo mot phan ten
 * POST   /api/courses                 -> tao moi (body: {"name":"...","departmentId":1})
 * PUT    /api/courses                 -> cap nhat (body: {"id":1,"name":"...","departmentId":1})
 * DELETE /api/courses?id=1            -> xoa
 */
@WebServlet("/api/courses")
public class CourseController extends HttpServlet {

    private final CourseDAO courseDAO = new CourseDAO();
    private final Validation validation = new Validation();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String idParam = request.getParameter("id");
        String searchParam = request.getParameter("search");
        try {
            if (idParam != null) {
                Course c = courseDAO.getById(Integer.parseInt(idParam));
                if (c == null) {
                    response.getWriter().write(JsonUtil.error("Khong tim thay Course id=" + idParam));
                    return;
                }
                response.getWriter().write(JsonUtil.success("OK", c));
            } else if (searchParam != null) {
                List<Course> list = courseDAO.search(searchParam.trim());
                response.getWriter().write(JsonUtil.success("OK", list));
            } else {
                List<Course> list = courseDAO.getAll();
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
        
        // model được gọi ở đây, định hình dữ liệu 
        Course c = gson.fromJson(request.getReader(), Course.class);

        String err = validation.checkGeneralNameFormat(c == null ? null : c.getName(), "Ten course");
        if (err == null) {
            err = validation.checkPositiveInteger(c == null ? null : String.valueOf(c.getDepartmentId()), "departmentId");
        }
        if (err != null) {
            response.getWriter().write(JsonUtil.error(err));
            return;
        }

        try {
            int newId = courseDAO.insert(c);
            c.setId(newId);
            response.getWriter().write(JsonUtil.success("Them thanh cong", c));
        } catch (SQLIntegrityConstraintViolationException e) {
            response.getWriter().write(JsonUtil.error("Department id=" + c.getDepartmentId() + " khong ton tai"));
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Course c = gson.fromJson(request.getReader(), Course.class);

        String err = validation.checkPositiveInteger(c == null ? null : String.valueOf(c.getId()), "id");
        if (err == null) {
            err = validation.checkGeneralNameFormat(c.getName(), "Ten course");
        }
        if (err == null) {
            err = validation.checkPositiveInteger(String.valueOf(c.getDepartmentId()), "departmentId");
        }
        if (err != null) {
            response.getWriter().write(JsonUtil.error(err));
            return;
        }

        try {
            boolean updated = courseDAO.update(c);
            if (updated) {
                response.getWriter().write(JsonUtil.success("Cap nhat thanh cong", c));
            } else {
                response.getWriter().write(JsonUtil.error("Khong tim thay Course id=" + c.getId()));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            response.getWriter().write(JsonUtil.error("Department id=" + c.getDepartmentId() + " khong ton tai"));
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
            boolean deleted = courseDAO.delete(Integer.parseInt(idParam));
            if (deleted) {
                response.getWriter().write(JsonUtil.success("Xoa thanh cong", null));
            } else {
                response.getWriter().write(JsonUtil.error("Khong tim thay Course id=" + idParam));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            // ON DELETE RESTRICT tu enrollments -> con sinh vien dang ky mon nay
            response.getWriter().write(JsonUtil.error("Khong the xoa: dang co sinh vien dang ky mon hoc nay"));
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
        }
    }
}
