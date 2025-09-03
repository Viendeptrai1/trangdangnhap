package org.example.trangdangnhap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.trangdangnhap.model.Category;
import org.example.trangdangnhap.model.User;
import org.example.trangdangnhap.service.CategoryService;
import org.example.trangdangnhap.service.impl.CategoryServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "categoryController", urlPatterns = {"/admin/category/list", "/admin/category/add", "/admin/category/edit", "/admin/category/delete"})
public class CategoryController extends HttpServlet {
    private final CategoryService categoryService = new CategoryServiceImpl();

    private User requireLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        return user;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        User user = requireLogin(req, resp);
        if (user == null) return;
        switch (path) {
            case "/admin/category/list":
                List<Category> cateList = categoryService.getAllByUserId(user.getId());
                req.setAttribute("cateList", cateList);
                req.getRequestDispatcher("/Views/admin/list-category.jsp").forward(req, resp);
                break;
            case "/admin/category/add":
                req.getRequestDispatcher("/Views/admin/add-category.jsp").forward(req, resp);
                break;
            case "/admin/category/edit":
                String id = req.getParameter("id");
                if (id == null) {
                    resp.sendRedirect(req.getContextPath() + "/admin/category/list");
                    return;
                }
                Category category = categoryService.get(Integer.parseInt(id), user.getId());
                if (category == null) {
                    resp.sendRedirect(req.getContextPath() + "/admin/category/list");
                    return;
                }
                req.setAttribute("category", category);
                req.getRequestDispatcher("/Views/admin/edit-category.jsp").forward(req, resp);
                break;
            case "/admin/category/delete":
                String delId = req.getParameter("id");
                if (delId != null) {
                    categoryService.delete(Integer.parseInt(delId), user.getId());
                }
                resp.sendRedirect(req.getContextPath() + "/admin/category/list");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        User user = requireLogin(req, resp);
        if (user == null) return;
        if ("/admin/category/add".equals(path)) {
            String name = req.getParameter("name");
            String icon = req.getParameter("icon");
            Category category = new Category(null, name, icon, user.getId());
            categoryService.insert(category);
            resp.sendRedirect(req.getContextPath() + "/admin/category/list");
        } else if ("/admin/category/edit".equals(path)) {
            String id = req.getParameter("id");
            String name = req.getParameter("name");
            String icon = req.getParameter("icon");
            Category category = new Category(Integer.parseInt(id), name, icon, user.getId());
            categoryService.edit(category);
            resp.sendRedirect(req.getContextPath() + "/admin/category/list");
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }
}
