package controller;

import dal.TransactionDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.JsonUtil;
import java.io.IOException;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/dashboard")
public class DashboardController extends HttpServlet {
    private final TransactionDAO transactionDAO = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            String monthParam = request.getParameter("month");
            YearMonth month = monthParam == null || monthParam.isBlank()
                    ? YearMonth.now() : YearMonth.parse(monthParam);
            Map<String, Object> dashboard = new LinkedHashMap<>();
            dashboard.put("month", month.toString());
            dashboard.put("totals", transactionDAO.getTotals(month.atDay(1), month.atEndOfMonth()));
            dashboard.put("byCategory", transactionDAO.getTotalsByCategory(month.atDay(1), month.atEndOfMonth()));
            response.getWriter().write(JsonUtil.success("Thành công", dashboard));
        } catch (RuntimeException exception) {
            response.getWriter().write(JsonUtil.error("Tháng không hợp lệ, định dạng đúng là yyyy-MM"));
        } catch (SQLException exception) {
            response.getWriter().write(JsonUtil.error("Lỗi database"));
        }
    }
}
