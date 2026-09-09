/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Trung Anh
 */
// bản chất là đang giao tiếp với server -> tomcat
// Nó nói với Tomcat:
// Nếu có request tới /login, hãy giao request này cho LoginServlet.java
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})

// extends HttpServlet là để kế thừa lại --> trong serlet có doget và dopost để chơi như ở dưới
public class LoginServlet extends HttpServlet {

    // Khi browser gửi HTTP GET tới Servlet này, Tomcat sẽ gọi tới doGet
    // ví dụ khi enter http://localhost:8080/LoginServlet/login 
    // thì thực chất là: GET /ServletPractice/hello HTTP/1.1 giống burp suite chưa hẹ hẹ
    // khi Tomcat nhận: GET --> /login --> LoginServlet.java --> gọi vào doGet
    // chỉ mới gọi và biết đường đi chưa chạy nhé
    
    
    
    // HttpServletResponse response thì ta biết là request chứa những thứ client gửi lên server.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
        //processRequest(request, response);
    }

    // hiển thị thì do get còn sử lí thì do post
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username.equals("admin") && password.equals("123")) {
            // cái này test
            //request.setAttribute("message", "xin chao" + username);
            //request.getRequestDispatcher("home.jsp").forward(request, response);

            response.sendRedirect("home");
        } else {
            response.sendRedirect("login?error=true");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
