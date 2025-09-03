package org.example.trangdangnhap.service;

import org.example.trangdangnhap.model.Category;

import java.util.List;

public interface CategoryService {
    void insert(Category category);
    void edit(Category category);
    void delete(int id, Long userId);
    Category get(int id, Long userId);
    Category get(String name, Long userId);
    List<Category> getAllByUserId(Long userId);
    List<Category> search(Long userId, String keyword);
}
