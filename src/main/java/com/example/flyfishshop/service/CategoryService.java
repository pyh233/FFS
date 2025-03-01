package com.example.flyfishshop.service;

import com.example.flyfishshop.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    Category findCategoryTreeById(Integer id);
    Category findNodeById(Integer id);
    int deleteCategoryByIds(Integer[] ids);
    boolean addCategory(Category category);
    boolean updateCategory(Category updateCategory);
}
