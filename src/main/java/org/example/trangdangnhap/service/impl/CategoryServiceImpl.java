package org.example.trangdangnhap.service.impl;

import org.example.trangdangnhap.dao.CategoryDAO;
import org.example.trangdangnhap.dao.impl.CategoryDAOImpl;
import org.example.trangdangnhap.model.Category;
import org.example.trangdangnhap.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryDAO categoryDAO = new CategoryDAOImpl();

    @Override
    public void insert(Category category) {
        if (category == null) return;
        categoryDAO.insert(category);
    }

    @Override
    public void edit(Category category) {
        if (category == null || category.getId() == null) return;
        // Có thể thêm validate tên/ảnh nếu cần
        categoryDAO.edit(category);
    }

    @Override
    public void delete(int id) {
        categoryDAO.delete(id);
    }

    @Override
    public Category get(int id) {
        return categoryDAO.get(id);
    }

    @Override
    public Category get(String name) {
        return categoryDAO.get(name);
    }

    @Override
    public List<Category> getAll() {
        return categoryDAO.getAll();
    }

    @Override
    public List<Category> search(String keyword) {
        return categoryDAO.search(keyword == null ? "" : keyword);
    }
}
