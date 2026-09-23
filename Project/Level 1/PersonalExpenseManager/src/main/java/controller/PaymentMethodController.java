package controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dal.PaymentMethodDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.PaymentMethod;
import util.JsonUtil;
import util.Validation;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/payment-methods")
public class PaymentMethodController extends HttpServlet {
    private final PaymentMethodDAO paymentMethodDAO = new PaymentMethodDAO();
    private final Validation validation = new Validation();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepare(response);
        try {
            String id = request.getParameter("id");
            if (id != null) {
                PaymentMethod paymentMethod = paymentMethodDAO.getById(Integer.parseInt(id));
                write(response, paymentMethod == null ? JsonUtil.error("Không tìm thấy phương thức thanh toán")
                        : JsonUtil.success("Thành công", paymentMethod));
            } else {
                List<PaymentMethod> methods = paymentMethodDAO.getAll();
                write(response, JsonUtil.success("Thành công", methods));
            }
        } catch (NumberFormatException exception) { write(response, JsonUtil.error("id không hợp lệ")); }
        catch (SQLException exception) { write(response, JsonUtil.error("Lỗi database")); }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepare(response);
        PaymentMethod method = parse(request, response);
        if (method == null) return;
        String error = validation.checkGeneralNameFormat(method.getName(), "Tên phương thức thanh toán");
        if (error != null) { write(response, JsonUtil.error(error)); return; }
        try {
            if (paymentMethodDAO.existsByName(method.getName().trim(), null)) {
                write(response, JsonUtil.error("Phương thức thanh toán đã tồn tại")); return;
            }
            method.setName(method.getName().trim());
            method.setId(paymentMethodDAO.insert(method));
            write(response, JsonUtil.success("Thêm phương thức thành công", method));
        } catch (SQLException exception) { write(response, JsonUtil.error("Lỗi database")); }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepare(response);
        PaymentMethod method = parse(request, response);
        if (method == null) return;
        String error = validation.checkPositiveInteger(String.valueOf(method.getId()), "id");
        if (error == null) error = validation.checkGeneralNameFormat(method.getName(), "Tên phương thức thanh toán");
        if (error != null) { write(response, JsonUtil.error(error)); return; }
        try {
            if (paymentMethodDAO.existsByName(method.getName().trim(), method.getId())) {
                write(response, JsonUtil.error("Phương thức thanh toán đã tồn tại")); return;
            }
            method.setName(method.getName().trim());
            write(response, paymentMethodDAO.update(method)
                    ? JsonUtil.success("Cập nhật phương thức thành công", method)
                    : JsonUtil.error("Không tìm thấy phương thức thanh toán"));
        } catch (SQLException exception) { write(response, JsonUtil.error("Lỗi database")); }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepare(response);
        String id = request.getParameter("id");
        String error = validation.checkPositiveInteger(id, "id");
        if (error != null) { write(response, JsonUtil.error(error)); return; }
        try {
            write(response, paymentMethodDAO.delete(Integer.parseInt(id))
                    ? JsonUtil.success("Xóa phương thức thành công", null)
                    : JsonUtil.error("Không tìm thấy phương thức thanh toán"));
        } catch (SQLException exception) { write(response, JsonUtil.error("Lỗi database")); }
    }

    private PaymentMethod parse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try { return gson.fromJson(request.getReader(), PaymentMethod.class); }
        catch (JsonSyntaxException exception) { write(response, JsonUtil.error("JSON không hợp lệ")); return null; }
    }

    private void prepare(HttpServletResponse response) { response.setContentType("application/json;charset=UTF-8"); }
    private void write(HttpServletResponse response, String body) throws IOException { response.getWriter().write(body); }
}
