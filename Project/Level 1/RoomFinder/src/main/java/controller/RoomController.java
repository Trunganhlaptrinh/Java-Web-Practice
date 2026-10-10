package controller;

import com.google.gson.Gson;
import dal.RoomDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.Room;
import model.User;
import util.JsonUtil;
import util.Validation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = {"/api/rooms", "/api/rooms/status"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class RoomController extends HttpServlet {

    private final RoomDAO roomDAO = new RoomDAO();
    private final Validation validation = new Validation();
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

        String idParam = request.getParameter("id");
        try {
            if (idParam != null && !idParam.trim().isEmpty()) {
                Room room = roomDAO.getById(Integer.parseInt(idParam));
                if (room == null) {
                    response.getWriter().write(JsonUtil.error("Không tìm thấy phòng"));
                } else {
                    response.getWriter().write(JsonUtil.success("Thành công", room));
                }
            } else {
                String search = request.getParameter("search");
                String landlordIdParam = request.getParameter("landlordId");
                String status = request.getParameter("status");

                Integer landlordId = (landlordIdParam != null && !landlordIdParam.trim().isEmpty()) 
                                     ? Integer.parseInt(landlordIdParam) : null;
                List<Room> rooms = roomDAO.getAll(search, landlordId, status);
                response.getWriter().write(JsonUtil.success("Thành công", rooms));
            }
        } catch (NumberFormatException e) {
            response.getWriter().write(JsonUtil.error("ID không hợp lệ"));
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Lỗi database"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        User user = getAuthenticatedUser(request);
        if (user == null || !"LANDLORD".equals(user.getRole())) {
            response.getWriter().write(JsonUtil.error("Chỉ chủ nhà mới được đăng phòng"));
            return;
        }

        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String areaStr = request.getParameter("area");
            String address = request.getParameter("address");
            String imageUrl = request.getParameter("imageUrl");

            String err = validation.checkRequired(title, "Tiêu đề");
            if (err == null) err = validation.checkPositiveLong(priceStr, "Giá phòng");
            if (err == null) err = validation.checkPositiveFloat(areaStr, "Diện tích");
            if (err == null) err = validation.checkRequired(address, "Địa chỉ");
            if (err != null) {
                response.getWriter().write(JsonUtil.error(err));
                return;
            }

            Part filePart = request.getPart("imageFile");
            if (filePart != null && filePart.getSize() > 0) {
                String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();

                String fileName = UUID.randomUUID().toString() + "_" + getFileName(filePart);
                File file = new File(uploadPath + File.separator + fileName);
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                imageUrl = "uploads/" + fileName;
            }

            Room room = new Room();
            room.setLandlordId(user.getId());
            room.setTitle(title.trim());
            room.setDescription(description != null ? description.trim() : "");
            room.setPrice(Long.parseLong(priceStr.trim()));
            room.setArea(Float.parseFloat(areaStr.trim()));
            room.setAddress(address.trim());
            room.setImageUrl(imageUrl);

            int newId = roomDAO.insert(room);
            room.setId(newId);
            room.setStatus("AVAILABLE");
            response.getWriter().write(JsonUtil.success("Đăng phòng thành công", room));
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

        String path = request.getServletPath();
        try {
            if ("/api/rooms/status".equals(path)) {
                Room reqRoom = gson.fromJson(request.getReader(), Room.class);
                if (reqRoom == null || reqRoom.getStatus() == null) {
                    response.getWriter().write(JsonUtil.error("Dữ liệu không hợp lệ"));
                    return;
                }
                boolean updated = roomDAO.updateStatus(reqRoom.getId(), user.getId(), reqRoom.getStatus());
                if (updated) {
                    response.getWriter().write(JsonUtil.success("Cập nhật trạng thái thành công", null));
                } else {
                    response.getWriter().write(JsonUtil.error("Không tìm thấy phòng"));
                }
            } else {
                Room room = gson.fromJson(request.getReader(), Room.class);
                String err = validation.checkPositiveInteger(String.valueOf(room.getId()), "ID");
                if (err == null) err = validation.checkRequired(room.getTitle(), "Tiêu đề");
                if (err == null) err = validation.checkPositiveLong(String.valueOf(room.getPrice()), "Giá phòng");
                if (err == null) err = validation.checkPositiveFloat(String.valueOf(room.getArea()), "Diện tích");
                if (err == null) err = validation.checkRequired(room.getAddress(), "Địa chỉ");
                
                if (err != null) {
                    response.getWriter().write(JsonUtil.error(err));
                    return;
                }
                room.setLandlordId(user.getId());
                boolean updated = roomDAO.update(room);
                if (updated) {
                    response.getWriter().write(JsonUtil.success("Cập nhật phòng thành công", room));
                } else {
                    response.getWriter().write(JsonUtil.error("Không tìm thấy phòng"));
                }
            }
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Lỗi database"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        User user = getAuthenticatedUser(request);
        if (user == null || !"LANDLORD".equals(user.getRole())) {
            response.getWriter().write(JsonUtil.error("Không có quyền"));
            return;
        }

        String idParam = request.getParameter("id");
        try {
            boolean deleted = roomDAO.delete(Integer.parseInt(idParam), user.getId());
            if (deleted) {
                response.getWriter().write(JsonUtil.success("Đã xóa phòng", null));
            } else {
                response.getWriter().write(JsonUtil.error("Không tìm thấy phòng hoặc không có quyền"));
            }
        } catch (NumberFormatException e) {
            response.getWriter().write(JsonUtil.error("ID không hợp lệ"));
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Lỗi database"));
        }
    }

    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "unknown";
    }
}
