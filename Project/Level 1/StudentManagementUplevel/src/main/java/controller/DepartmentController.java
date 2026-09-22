package controller;

import com.google.gson.Gson;
import dal.DepartmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import model.Department;
import util.JsonUtil;
import util.Validation;

/**
 * API cho Department, JSON qua AJAX (khong dung JSP).
 * GET    /api/departments          -> danh sach tat ca
 * GET    /api/departments?id=1     -> chi tiet 1 department
 * POST   /api/departments          -> tao moi (body JSON: {"name": "..."})
 * PUT    /api/departments          -> cap nhat (body JSON: {"id":1,"name":"..."})
 * DELETE /api/departments?id=1     -> xoa
 */
@WebServlet("/api/departments")
public class DepartmentController extends HttpServlet {

    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final Validation validation = new Validation();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String idParam = request.getParameter("id");
        try {
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                Department d = departmentDAO.getById(id);
                if (d == null) {
                    response.getWriter().write(JsonUtil.error("Khong tim thay Department id=" + id));
                    return;
                }
                response.getWriter().write(JsonUtil.success("OK", d));
            } else {
                List<Department> list = departmentDAO.getAll();
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

        Department d = gson.fromJson(request.getReader(), Department.class);

        String err = validation.checkGeneralNameFormat(d == null ? null : d.getName(), "Ten department");
        if (err != null) {
            response.getWriter().write(JsonUtil.error(err));
            return;
        }

        try {
            if (departmentDAO.existsByName(d.getName().trim(), null)) {
                response.getWriter().write(JsonUtil.error("Department da ton tai"));
                return;
            }
            int newId = departmentDAO.insert(d);
            d.setId(newId);
            response.getWriter().write(JsonUtil.success("Them thanh cong", d));
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Department d = gson.fromJson(request.getReader(), Department.class);

        String err = validation.checkPositiveInteger(d == null ? null : String.valueOf(d.getId()), "id");
        if (err == null) {
            err = validation.checkGeneralNameFormat(d.getName(), "Ten department");
        }
        if (err != null) {
            response.getWriter().write(JsonUtil.error(err));
            return;
        }

        try {
            if (departmentDAO.existsByName(d.getName().trim(), d.getId())) {
                response.getWriter().write(JsonUtil.error("Ten Department da ton tai"));
                return;
            }
            boolean updated = departmentDAO.update(d);
            if (updated) {
                response.getWriter().write(JsonUtil.success("Cap nhat thanh cong", d));
            } else {
                response.getWriter().write(JsonUtil.error("Khong tim thay Department id=" + d.getId()));
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

        int id = Integer.parseInt(idParam);
        try {
            boolean deleted = departmentDAO.delete(id);
            if (deleted) {
                response.getWriter().write(JsonUtil.success("Xoa thanh cong", null));
            } else {
                response.getWriter().write(JsonUtil.error("Khong tim thay Department id=" + id));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            response.getWriter().write(JsonUtil.error("Khong the xoa: dang co Teacher hoac Course thuoc Department nay"));
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Loi database: " + e.getMessage()));
        }
    }
}
