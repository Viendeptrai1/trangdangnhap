package org.example.trangdangnhap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.trangdangnhap.model.User;
import org.example.trangdangnhap.service.UserService;
import org.example.trangdangnhap.service.impl.UserServiceImpl;

import java.io.IOException;

@WebServlet(name = "userController", urlPatterns = {"/login", "/register", "/logout"})
public class UserController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/login":
                req.getRequestDispatcher("/Views/login.jsp").forward(req, resp);
                break;
            case "/register":
                req.getRequestDispatcher("/Views/register.jsp").forward(req, resp);
                break;
            case "/logout":
                HttpSession session = req.getSession(false);
                if (session != null) session.invalidate();
                resp.sendRedirect(req.getContextPath() + "/login");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/login".equals(path)) {
            doLogin(req, resp);
        } else if ("/register".equals(path)) {
            doRegister(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private void doRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        User user = new User(username, password, email);
        boolean ok = userService.register(user);
        if (ok) {
            req.setAttribute("message", "Đăng ký thành công. Mời đăng nhập.");
            req.getRequestDispatcher("/Views/login.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", "Đăng ký thất bại. Vui lòng kiểm tra thông tin.");
            req.getRequestDispatcher("/Views/register.jsp").forward(req, resp);
        }
    }

    private void doLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userService.login(username, password);
        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", user);
            resp.sendRedirect(req.getContextPath() + "/Views/home.jsp");
        } else {
            req.setAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
            req.getRequestDispatcher("/Views/login.jsp").forward(req, resp);
        }
    }
}


