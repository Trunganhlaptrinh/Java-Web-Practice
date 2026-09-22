package controller;

import com.google.gson.Gson;
import dal.StudentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.Student;
import util.JsonUtil;
import util.Validation;

/**
 * GET    /api/students                -> danh sach tat ca
 * GET    /api/students?id=1           -> chi tiet 1 student
 * GET    /api/students?search=an      -> tim theo mot phan ten
 * POST   /api/students                -> tao moi (body: {"name":"...","semester":"..."})
 * PUT    /api/students                -> cap nhat (body: {"id":1,"name":"...","semester":"..."})
 * DELETE /api/students?id=1           -> xoa
 */
@WebServlet("/api/students")
public class StudentController extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();
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
                Student s = studentDAO.getById(Integer.parseInt(idParam));
                if (s == null) {
                    response.getWriter().write(JsonUtil.error("Khong tim thay Student id=" + idParam));
                    return;
                }
                response.getWriter().write(JsonUtil.success("OK", s));
            } else if (searchParam != null) {
                List<Student> list = studentDAO.search(searchParam.trim());
                response.getWriter().write(JsonUtil.success("OK", list));
            } else {
                List<Student> list = studentDAO.getAll();
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

        Student s = gson.fromJson(request.getReader(), Student.class);

        String err = validation.checkNameFormat(s == null ? null : s.getName(), "Ten student");
        if (err == null) {
            err = validation.checkSemesterFormat(s.getSemester(), "Semester");
        }
        if (err != null) {
            response.getWriter().write(JsonUtil.error(err));
            return;
        }

        try {
            int newId = studentDAO.insert(s);
            s.setId(newId);
            response.getWriter().write(JsonUtil.success("Them thanh cong", s));
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Student s = gson.fromJson(request.getReader(), Student.class);

        String err = validation.checkPositiveInteger(s == null ? null : String.valueOf(s.getId()), "id");
        if (err == null) {
            err = validation.checkNameFormat(s.getName(), "Ten student");
        }
        if (err == null) {
            err = validation.checkSemesterFormat(s.getSemester(), "Semester");
        }
        if (err != null) {
            response.getWriter().write(JsonUtil.error(err));
            return;
        }

        try {
            boolean updated = studentDAO.update(s);
            if (updated) {
                response.getWriter().write(JsonUtil.success("Cap nhat thanh cong", s));
            } else {
                response.getWriter().write(JsonUtil.error("Khong tim thay Student id=" + s.getId()));
            }
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
            boolean deleted = studentDAO.delete(Integer.parseInt(idParam));
            if (deleted) {
                // xoa Student se tu dong xoa theo cac enrollment lien quan
                // (ON DELETE CASCADE), khong can xu ly gi them
                response.getWriter().write(JsonUtil.success("Xoa thanh cong", null));
            } else {
                response.getWriter().write(JsonUtil.error("Khong tim thay Student id=" + idParam));
            }
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
        }
    }
}
