package controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dal.CategoryDAO;
import dal.PaymentMethodDAO;
import dal.TransactionDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import model.PaymentMethod;
import model.Transaction;
import util.JsonUtil;
import util.Validation;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/api/transactions")
public class TransactionController extends HttpServlet {
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final PaymentMethodDAO paymentMethodDAO = new PaymentMethodDAO();
    private final Validation validation = new Validation();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepare(response);
        try {
            String id = request.getParameter("id");
            if (id != null) {
                Transaction transaction = transactionDAO.getById(Long.parseLong(id));
                write(response, transaction == null ? JsonUtil.error("Không tìm thấy giao dịch")
                        : JsonUtil.success("Thành công", transaction));
                return;
            }
            List<Transaction> transactions = transactionDAO.search(
                    request.getParameter("search"), request.getParameter("type"),
                    parseOptionalInteger(request.getParameter("categoryId")),
                    parseOptionalInteger(request.getParameter("paymentMethodId")),
                    parseOptionalDate(request.getParameter("fromDate")),
                    parseOptionalDate(request.getParameter("toDate")));
            write(response, JsonUtil.success("Thành công", transactions));
        } catch (NumberFormatException exception) { write(response, JsonUtil.error("Tham số không hợp lệ")); }
        catch (SQLException exception) { write(response, JsonUtil.error("Lỗi database")); }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepare(response);
        Transaction transaction = parse(request, response);
        if (transaction == null) return;
        String error = validate(transaction, false);
        if (error != null) { write(response, JsonUtil.error(error)); return; }
        try {
            validateReferences(transaction);
            transaction.setId(transactionDAO.insert(transaction));
            write(response, JsonUtil.success("Thêm giao dịch thành công", transaction));
        } catch (IllegalArgumentException exception) { write(response, JsonUtil.error(exception.getMessage())); }
        catch (SQLException exception) { write(response, JsonUtil.error("Lỗi database")); }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepare(response);
        Transaction transaction = parse(request, response);
        if (transaction == null) return;
        String error = validation.checkPositiveInteger(String.valueOf(transaction.getId()), "id");
        if (error == null) error = validate(transaction, false);
        if (error != null) { write(response, JsonUtil.error(error)); return; }
        try {
            validateReferences(transaction);
            write(response, transactionDAO.update(transaction)
                    ? JsonUtil.success("Cập nhật giao dịch thành công", transaction)
                    : JsonUtil.error("Không tìm thấy giao dịch"));
        } catch (IllegalArgumentException exception) { write(response, JsonUtil.error(exception.getMessage())); }
        catch (SQLException exception) { write(response, JsonUtil.error("Lỗi database")); }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepare(response);
        String id = request.getParameter("id");
        String error = validation.checkPositiveInteger(id, "id");
        if (error != null) { write(response, JsonUtil.error(error)); return; }
        try {
            write(response, transactionDAO.delete(Long.parseLong(id))
                    ? JsonUtil.success("Xóa giao dịch thành công", null)
                    : JsonUtil.error("Không tìm thấy giao dịch"));
        } catch (SQLException exception) { write(response, JsonUtil.error("Lỗi database")); }
    }

    private String validate(Transaction transaction, boolean unused) {
        String error = validation.checkPositiveAmount(String.valueOf(transaction.getAmount()), "Số tiền");
        if (error == null) error = validation.checkRequired(transaction.getDescription(), "Mô tả");
        if (error == null) error = validation.checkTransactionType(transaction.getType());
        if (error == null) {
            if (transaction.getTransactionDate() == null || transaction.getTransactionDate().isBlank()) {
                transaction.setTransactionDate(LocalDate.now().toString());
            }
            error = validation.checkDateFormat(transaction.getTransactionDate(), "Ngày giao dịch");
        }
        if (error == null && transaction.getCategoryId() == null) error = "Danh mục không được để trống";
        if (error == null && "EXPENSE".equals(transaction.getType()) && transaction.getPaymentMethodId() == null) {
            error = "Phương thức thanh toán bắt buộc với khoản chi";
        }
        return error;
    }

    private void validateReferences(Transaction transaction) throws SQLException {
        Category category = categoryDAO.getById(transaction.getCategoryId());
        if (category == null) throw new IllegalArgumentException("Không tìm thấy danh mục");
        if (!transaction.getType().equals(category.getType())) {
            throw new IllegalArgumentException("Danh mục không đúng loại giao dịch");
        }
        if (transaction.getPaymentMethodId() != null
                && paymentMethodDAO.getById(transaction.getPaymentMethodId()) == null) {
            throw new IllegalArgumentException("Không tìm thấy phương thức thanh toán");
        }
    }

    private Transaction parse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try { return gson.fromJson(request.getReader(), Transaction.class); }
        catch (JsonSyntaxException exception) { write(response, JsonUtil.error("JSON không hợp lệ")); return null; }
    }

    private Integer parseOptionalInteger(String value) {
        return value == null || value.isBlank() ? null : Integer.valueOf(value);
    }

    private LocalDate parseOptionalDate(String value) {
        return value == null || value.isBlank() ? null : LocalDate.parse(value);
    }

    private void prepare(HttpServletResponse response) { response.setContentType("application/json;charset=UTF-8"); }
    private void write(HttpServletResponse response, String body) throws IOException { response.getWriter().write(body); }
}
