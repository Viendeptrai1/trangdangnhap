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
        if (category == null || category.getId() == null || category.getUserId() == null) return;
        categoryDAO.edit(category);
    }

    @Override
    public void delete(int id, Long userId) {
        if (userId == null) return;
        if (categoryDAO instanceof CategoryDAOImpl) {
            ((CategoryDAOImpl) categoryDAO).delete(id, userId);
        }
    }

    @Override
    public Category get(int id, Long userId) {
        if (userId == null) return null;
        if (categoryDAO instanceof CategoryDAOImpl) {
            return ((CategoryDAOImpl) categoryDAO).get(id, userId);
        }
        return null;
    }

    @Override
    public Category get(String name, Long userId) {
        if (userId == null) return null;
        if (categoryDAO instanceof CategoryDAOImpl) {
            return ((CategoryDAOImpl) categoryDAO).get(name, userId);
        }
        return null;
    }

    @Override
    public List<Category> getAllByUserId(Long userId) {
        if (userId == null) return List.of();
        return categoryDAO.getAllByUserId(userId);
    }

    @Override
    public List<Category> search(Long userId, String keyword) {
        if (userId == null) return List.of();
        if (categoryDAO instanceof CategoryDAOImpl) {
            return ((CategoryDAOImpl) categoryDAO).search(userId, keyword == null ? "" : keyword);
        }
        return List.of();
    }
}
