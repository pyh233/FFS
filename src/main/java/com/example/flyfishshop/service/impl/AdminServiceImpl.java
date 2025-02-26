package com.example.flyfishshop.service.impl;

import com.example.flyfishshop.dao.AdminDao;
import com.example.flyfishshop.model.Admin;
import com.example.flyfishshop.service.AdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private AdminDao adminDao;
    private final PasswordEncryptor pe = new StrongPasswordEncryptor();

    @Autowired
    public void setAdminDao(AdminDao adminDao) {
        this.adminDao = adminDao;
    }


    @Override
    public List<Admin> findAll(Admin searchAdmin, Page<?> page) {
        try (Page<?> __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            return adminDao.findAll(searchAdmin);
        }
    }

    @Override
    public int delete(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        // TODO:删除图片
        return adminDao.delete(ids);
    }

    @Override
    public boolean insert(Admin admin) {
        // TODO:查询是否用户名重复
        admin.setPassword(pe.encryptPassword(admin.getPassword()));
        admin.setCreatedBy("admin");
        admin.setCreateTime(LocalDateTime.now());
        return adminDao.insert(admin) > 0;
    }

    @Override
    public boolean update(Admin admin) {
        if(StringUtils.hasText(admin.getPassword())){
            admin.setPassword(pe.encryptPassword(admin.getPassword()));
        }
        admin.setLastModifiedBy("admin");
        admin.setLastModifyTime(LocalDateTime.now());
        return adminDao.update(admin) > 0;
    }

    @Override
    public Admin findById(Integer id) {
        return adminDao.findById(id);
    }

    @Override
    public Admin login(Admin admin) {
        Admin adminInDB = adminDao.login(admin);
        if(adminInDB == null){
            return null;
        }
        boolean valid = pe.checkPassword(admin.getPassword(), adminInDB.getPassword());
        if(valid){
            return adminInDB;
        }else {
            return null;
        }
    }
}
