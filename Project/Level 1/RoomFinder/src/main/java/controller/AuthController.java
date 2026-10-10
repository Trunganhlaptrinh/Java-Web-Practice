package controller;

import com.google.gson.Gson;
import dal.UserDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.JsonUtil;
import util.PasswordUtil;
import util.Validation;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet(urlPatterns = {"/api/auth/register", "/api/auth/login", "/api/auth/logout", "/api/auth/me"})
public class AuthController extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final Validation validation = new Validation();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        String path = request.getServletPath();

        if ("/api/auth/me".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                user.setPassword(null); // Không trả về password
                response.getWriter().write(JsonUtil.success("Đã đăng nhập", user));
            } else {
                response.getWriter().write(JsonUtil.error("Chưa đăng nhập"));
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        String path = request.getServletPath();

        if ("/api/auth/register".equals(path)) {
            handleRegister(request, response);
        } else if ("/api/auth/login".equals(path)) {
            handleLogin(request, response);
        } else if ("/api/auth/logout".equals(path)) {
            handleLogout(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User reqUser = gson.fromJson(request.getReader(), User.class);
        if (reqUser == null) return;

        String err = validation.checkRequired(reqUser.getUsername(), "Tài khoản");
        if (err == null) err = validation.checkRequired(reqUser.getPassword(), "Mật khẩu");
        if (err == null) err = validation.checkRequired(reqUser.getFullName(), "Họ tên");
        if (err == null) err = validation.checkRequired(reqUser.getPhone(), "Số điện thoại");
        if (err == null) err = validation.checkRole(reqUser.getRole());

        if (err != null) {
            response.getWriter().write(JsonUtil.error(err));
            return;
        }

        try {
            reqUser.setPassword(PasswordUtil.hashPassword(reqUser.getPassword()));
            int newId = userDAO.insert(reqUser);
            reqUser.setId(newId);
            reqUser.setPassword(null); // Ẩn mật khẩu khi trả về
            response.getWriter().write(JsonUtil.success("Đăng ký thành công", reqUser));
        } catch (SQLIntegrityConstraintViolationException e) {
            response.getWriter().write(JsonUtil.error("Tên đăng nhập đã tồn tại"));
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Lỗi database"));
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User reqUser = gson.fromJson(request.getReader(), User.class);
        if (reqUser == null || reqUser.getUsername() == null || reqUser.getPassword() == null) {
            response.getWriter().write(JsonUtil.error("Vui lòng nhập tài khoản và mật khẩu"));
            return;
        }

        try {
            User dbUser = userDAO.getByUsername(reqUser.getUsername().trim());
            if (dbUser != null && dbUser.getPassword().equals(PasswordUtil.hashPassword(reqUser.getPassword()))) {
                // Tạo session
                HttpSession session = request.getSession();
                session.setAttribute("user", dbUser);
                dbUser.setPassword(null);
                response.getWriter().write(JsonUtil.success("Đăng nhập thành công", dbUser));
            } else {
                response.getWriter().write(JsonUtil.error("Tài khoản hoặc mật khẩu không đúng"));
            }
        } catch (SQLException e) {
            response.getWriter().write(JsonUtil.error("Lỗi database"));
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.getWriter().write(JsonUtil.success("Đăng xuất thành công", null));
    }
}

