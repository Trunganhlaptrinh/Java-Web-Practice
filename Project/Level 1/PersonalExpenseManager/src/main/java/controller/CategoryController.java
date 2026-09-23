package controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dal.CategoryDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import util.JsonUtil;
import util.Validation;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@WebServlet("/api/categories")
public class CategoryController extends HttpServlet {
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final Validation validation = new Validation();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepare(response);
        try {
            String id = request.getParameter("id");
            if (id != null) {
                Category category = categoryDAO.getById(Integer.parseInt(id));
                write(response, category == null ? JsonUtil.error("Không tìm thấy danh mục")
                        : JsonUtil.success("Thành công", category));
            } else {
                List<Category> categories = categoryDAO.getAll(request.getParameter("type"));
                write(response, JsonUtil.success("Thành công", categories));
            }
        } catch (NumberFormatException exception) {
            write(response, JsonUtil.error("id không hợp lệ"));
        } catch (SQLException exception) {
            write(response, JsonUtil.error("Lỗi database"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepare(response);
        Category category = parse(request, response);
        if (category == null) return;
        String error = validate(category, false);
        if (error != null) { write(response, JsonUtil.error(error)); return; }
        try {
            if (categoryDAO.existsByNameAndType(category.getName().trim(), category.getType(), null)) {
                write(response, JsonUtil.error("Danh mục đã tồn tại trong loại giao dịch này")); return;
            }
            category.setName(category.getName().trim());
            category.setId(categoryDAO.insert(category));
            write(response, JsonUtil.success("Thêm danh mục thành công", category));
        } catch (SQLException exception) { write(response, JsonUtil.error("Lỗi database")); }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepare(response);
        Category category = parse(request, response);
        if (category == null) return;
        String error = validation.checkPositiveInteger(String.valueOf(category.getId()), "id");
        if (error == null) error = validate(category, false);
        if (error != null) { write(response, JsonUtil.error(error)); return; }
        try {
            if (categoryDAO.existsByNameAndType(category.getName().trim(), category.getType(), category.getId())) {
                write(response, JsonUtil.error("Danh mục đã tồn tại trong loại giao dịch này")); return;
            }
            category.setName(category.getName().trim());
            write(response, categoryDAO.update(category)
                    ? JsonUtil.success("Cập nhật danh mục thành công", category)
                    : JsonUtil.error("Không tìm thấy danh mục"));
        } catch (SQLException exception) { write(response, JsonUtil.error("Lỗi database")); }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepare(response);
        String id = request.getParameter("id");
        String error = validation.checkPositiveInteger(id, "id");
        if (error != null) { write(response, JsonUtil.error(error)); return; }
        try {
            write(response, categoryDAO.delete(Integer.parseInt(id))
                    ? JsonUtil.success("Xóa danh mục thành công", null)
                    : JsonUtil.error("Không tìm thấy danh mục"));
        } catch (SQLIntegrityConstraintViolationException exception) {
            write(response, JsonUtil.error("Không thể xóa danh mục"));
        } catch (SQLException exception) { write(response, JsonUtil.error("Lỗi database")); }
    }

    private String validate(Category category, boolean unused) {
        String error = validation.checkGeneralNameFormat(category.getName(), "Tên danh mục");
        if (error == null) error = validation.checkTransactionType(category.getType());
        return error;
    }

    private Category parse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try { return gson.fromJson(request.getReader(), Category.class); }
        catch (JsonSyntaxException exception) { write(response, JsonUtil.error("JSON không hợp lệ")); return null; }
    }

    private void prepare(HttpServletResponse response) { response.setContentType("application/json;charset=UTF-8"); }
    private void write(HttpServletResponse response, String body) throws IOException { response.getWriter().write(body); }
}
