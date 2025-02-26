package com.example.flyfishshop.service;

import com.example.flyfishshop.model.Admin;
import com.github.pagehelper.Page;

import java.util.List;

public interface AdminService {
    public List<Admin> findAll(Admin searchAdmin, Page<?> page);
    public int delete(Integer[] ids);
    public boolean insert(Admin admin);
    public boolean update(Admin admin);
    public Admin findById(Integer id);

    public Admin login(Admin admin);
}
