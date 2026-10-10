package controller;

import com.google.gson.Gson;
import dal.RentalRequestDAO;
import dal.RoomDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.RentalRequest;
import model.User;
import util.JsonUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/requests/*")
public class RentalRequestController extends HttpServlet {

    private final RentalRequestDAO rentalRequestDAO = new RentalRequestDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final Gson gson = new Gson();

    private User getAuthenticatedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("user");
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        User user = getAuthenticatedUser(request);
        if (user == null) {
            response.getWriter().write(JsonUtil.error("Vui lòng đăng nhập"));
            return;
        }

        try {
            List<RentalRequest> requests;
            if ("LANDLORD".equals(user.getRole())) {
                requests = rentalRequestDAO.getByLandlord(user.getId());
            } else {
                requests = rentalRequestDAO.getByRenter(user.getId());
            }
            response.getWriter().write(JsonUtil.success("Thành công", requests));
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Lỗi database"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        User user = getAuthenticatedUser(request);
        if (user == null || !"RENTER".equals(user.getRole())) {
            response.getWriter().write(JsonUtil.error("Chỉ người thuê mới có thể gửi yêu cầu"));
            return;
        }

        try {
            RentalRequest reqData = gson.fromJson(request.getReader(), RentalRequest.class);
            if (reqData == null || reqData.getRoomId() <= 0) {
                response.getWriter().write(JsonUtil.error("Dữ liệu không hợp lệ"));
                return;
            }

            // Kiểm tra xem đã gửi yêu cầu cho phòng này chưa
            if (rentalRequestDAO.exists(reqData.getRoomId(), user.getId())) {
                response.getWriter().write(JsonUtil.error("Bạn đã gửi yêu cầu cho phòng này rồi"));
                return;
            }

            reqData.setRenterId(user.getId());
            int newId = rentalRequestDAO.insert(reqData);
            if (newId > 0) {
                reqData.setId(newId);
                reqData.setStatus("PENDING");
                response.getWriter().write(JsonUtil.success("Gửi yêu cầu thành công", reqData));
            } else {
                response.getWriter().write(JsonUtil.error("Không thể gửi yêu cầu"));
            }

        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Lỗi database"));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        User user = getAuthenticatedUser(request);
        if (user == null || !"LANDLORD".equals(user.getRole())) {
            response.getWriter().write(JsonUtil.error("Chỉ chủ nhà mới có quyền cập nhật"));
            return;
        }

        String path = request.getPathInfo();
        if (path == null || !path.equals("/status")) {
            // Có thể mở rộng nếu cần
            response.getWriter().write(JsonUtil.error("Đường dẫn không hợp lệ"));
            return;
        }

        try {
            RentalRequest reqData = gson.fromJson(request.getReader(), RentalRequest.class);
            if (reqData == null || reqData.getId() <= 0 || reqData.getStatus() == null) {
                response.getWriter().write(JsonUtil.error("Dữ liệu không hợp lệ"));
                return;
            }

            String newStatus = reqData.getStatus();
            if (!"APPROVED".equals(newStatus) && !"REJECTED".equals(newStatus)) {
                response.getWriter().write(JsonUtil.error("Trạng thái không hợp lệ"));
                return;
            }

            // Lấy thông tin yêu cầu hiện tại để xác minh
            RentalRequest existingReq = rentalRequestDAO.getById(reqData.getId());
            if (existingReq == null) {
                response.getWriter().write(JsonUtil.error("Không tìm thấy yêu cầu"));
                return;
            }
            
            // Lấy thông tin phòng để kiểm tra chủ nhà
            model.Room room = roomDAO.getById(existingReq.getRoomId());
            if (room == null || room.getLandlordId() != user.getId()) {
                response.getWriter().write(JsonUtil.error("Bạn không có quyền cập nhật yêu cầu này"));
                return;
            }

            boolean updated = rentalRequestDAO.updateStatus(reqData.getId(), newStatus);
            if (updated) {
                if ("APPROVED".equals(newStatus)) {
                    // Cập nhật trạng thái phòng thành NEGOTIATING
                    roomDAO.updateStatus(room.getId(), user.getId(), "NEGOTIATING");
                }
                response.getWriter().write(JsonUtil.success("Cập nhật trạng thái thành công", null));
            } else {
                response.getWriter().write(JsonUtil.error("Không thể cập nhật"));
            }

        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Lỗi database"));
        }
    }
}

