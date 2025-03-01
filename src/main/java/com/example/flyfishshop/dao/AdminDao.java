package com.example.flyfishshop.dao;

import com.example.flyfishshop.model.Admin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminDao {
    public List<Admin> findAll(Admin searchAdmin);
    public int delete(Integer[] ids);
    public int insert(Admin admin);
    public int update(Admin admin);

    public Admin findById(Integer id);
    public Admin findByAccount(String account);
}
