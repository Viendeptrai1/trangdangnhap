package org.example.trangdangnhap.dao;

import org.example.trangdangnhap.model.Category;

import java.util.List;

public interface CategoryDAO {
    void insert(Category category);

    void edit(Category category);

    void delete(int id);

    Category get(int id);

    Category get(String name);

    List<Category> getAll();

    List<Category> search(String keyword);
}
