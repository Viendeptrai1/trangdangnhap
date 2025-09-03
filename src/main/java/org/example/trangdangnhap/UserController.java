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

@WebServlet(name = "userController", urlPatterns = {"/login", "/register", "/logout", "/home", "/forgot-password", "/change-password"})
public class UserController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/login":
                // Auto-login bằng cookie nếu có
                if (req.getSession(false) != null && req.getSession(false).getAttribute("currentUser") != null) {
                    resp.sendRedirect(req.getContextPath() + "/home");
                    return;
                }
                User remembered = getUserFromRememberCookie(req);
                if (remembered != null) {
                    req.getSession(true).setAttribute("currentUser", remembered);
                    resp.sendRedirect(req.getContextPath() + "/home");
                    return;
                }
                req.getRequestDispatcher("/Views/login.jsp").forward(req, resp);
                break;
            case "/register":
                req.getRequestDispatcher("/Views/register.jsp").forward(req, resp);
                break;
            case "/logout":
                HttpSession session = req.getSession(false);
                if (session != null) session.invalidate();
                clearRememberCookie(req, resp);
                resp.sendRedirect(req.getContextPath() + "/login");
                break;
            case "/home":
                if (req.getSession(false) == null || req.getSession(false).getAttribute("currentUser") == null) {
                    resp.sendRedirect(req.getContextPath() + "/login");
                    return;
                }
                req.getRequestDispatcher("/Views/home.jsp").forward(req, resp);
                break;
            case "/forgot-password":
                req.getRequestDispatcher("/Views/forgot-password.jsp").forward(req, resp);
                break;
            case "/change-password":
                if (req.getSession(false) == null || req.getSession(false).getAttribute("currentUser") == null) {
                    resp.sendRedirect(req.getContextPath() + "/login");
                    return;
                }
                req.getRequestDispatcher("/Views/change-password.jsp").forward(req, resp);
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
        } else if ("/forgot-password".equals(path)) {
            doForgotPassword(req, resp);
        } else if ("/change-password".equals(path)) {
            doChangePassword(req, resp);
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
        boolean remember = "true".equals(req.getParameter("remember"));

        User user = userService.login(username, password);
        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", user);
            if (remember) {
                setRememberCookie(resp, user);
            }
            // Điều hướng theo vai trò nếu có
            if (user.getRoleId() != null) {
                switch (user.getRoleId()) {
                    case 1: // admin
                        resp.sendRedirect(req.getContextPath() + "/home");
                        return;
                    case 2: // manager
                        resp.sendRedirect(req.getContextPath() + "/home");
                        return;
                    default:
                        resp.sendRedirect(req.getContextPath() + "/home");
                        return;
                }
            }
            resp.sendRedirect(req.getContextPath() + "/home");
        } else {
            req.setAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
            req.getRequestDispatcher("/Views/login.jsp").forward(req, resp);
        }
    }

    private void setRememberCookie(HttpServletResponse resp, User user) {
        // Lưu username băm nhẹ (demo). Sản phẩm thực tế dùng token an toàn.
        jakarta.servlet.http.Cookie c = new jakarta.servlet.http.Cookie("remember_username", user.getUsername());
        c.setMaxAge(7 * 24 * 60 * 60);
        c.setPath("/");
        resp.addCookie(c);
    }

    private void clearRememberCookie(HttpServletRequest req, HttpServletResponse resp) {
        jakarta.servlet.http.Cookie[] cookies = req.getCookies();
        if (cookies == null) return;
        for (jakarta.servlet.http.Cookie c : cookies) {
            if ("remember_username".equals(c.getName())) {
                c.setMaxAge(0);
                c.setPath("/");
                resp.addCookie(c);
            }
        }
    }

    private User getUserFromRememberCookie(HttpServletRequest req) {
        jakarta.servlet.http.Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        String username = null;
        for (jakarta.servlet.http.Cookie c : cookies) {
            if ("remember_username".equals(c.getName())) {
                username = c.getValue();
                break;
            }
        }
        if (username == null) return null;
        // Không có mật khẩu ở cookie, nên không thể login thực sự.
        // Ở đây có thể mở rộng: tạo API lấy user by username. Đơn giản: tạo user tối thiểu.
        User minimal = new User();
        minimal.setUsername(username);
        minimal.setEmail("");
        return minimal;
    }

    private void doForgotPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String email = req.getParameter("email");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (email == null || email.trim().isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập email");
            req.getRequestDispatcher("/Views/forgot-password.jsp").forward(req, resp);
            return;
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập mật khẩu mới");
            req.getRequestDispatcher("/Views/forgot-password.jsp").forward(req, resp);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("error", "Mật khẩu xác nhận không khớp");
            req.getRequestDispatcher("/Views/forgot-password.jsp").forward(req, resp);
            return;
        }

        boolean success = userService.forgotPassword(email, newPassword);
        if (success) {
            req.setAttribute("message", "Đổi mật khẩu thành công. Mời đăng nhập với mật khẩu mới.");
            req.getRequestDispatcher("/Views/login.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", "Email không tồn tại hoặc có lỗi xảy ra");
            req.getRequestDispatcher("/Views/forgot-password.jsp").forward(req, resp);
        }
    }

    private void doChangePassword(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập mật khẩu hiện tại");
            req.getRequestDispatcher("/Views/change-password.jsp").forward(req, resp);
            return;
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập mật khẩu mới");
            req.getRequestDispatcher("/Views/change-password.jsp").forward(req, resp);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("error", "Mật khẩu xác nhận không khớp");
            req.getRequestDispatcher("/Views/change-password.jsp").forward(req, resp);
            return;
        }

        User currentUser = (User) req.getSession().getAttribute("currentUser");
        boolean success = userService.changePassword(currentUser.getId(), currentPassword, newPassword);
        if (success) {
            req.setAttribute("message", "Đổi mật khẩu thành công");
            req.getRequestDispatcher("/Views/change-password.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", "Mật khẩu hiện tại không đúng hoặc có lỗi xảy ra");
            req.getRequestDispatcher("/Views/change-password.jsp").forward(req, resp);
        }
    }
}


