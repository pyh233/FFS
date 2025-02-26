package com.example.flyfishshop.dao;

import com.example.flyfishshop.model.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryDao {
//    public List<Category> findAll();

    public Category findTreeById(Integer id);
    public Category findNodeById(Integer id);
    public int deleteByIds(Integer[] ids);
    public int insert(Category category);
    public int update(Category category);
}
