package com.example.flyfishshop.service.impl;

import com.example.flyfishshop.dao.UserDao;
import com.example.flyfishshop.model.search.SearchUserModel;
import com.example.flyfishshop.model.User;
import com.example.flyfishshop.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@CacheConfig(cacheNames = "UserServiceImpl")
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    // 密码加密
    private final PasswordEncryptor pe = new StrongPasswordEncryptor();

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public List<User> list(SearchUserModel sum, Page<?> page) {
        try (Page<?> __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            return userDao.findAll(sum);
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    public int delete(Integer[] ids) {
        // 参数验证
        if (ids == null || ids.length == 0) {
            return 0;
        }
        // 安全删除
        // TODO:删除图片
        return userDao.deleteByIds(ids);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean insert(User user) {
        // 密码加密
        user.setPassword(pe.encryptPassword(user.getPassword()));

        user.setCreatedTime(LocalDateTime.now());
        user.setCreatedBy("admin");

        return userDao.insert(user) > 0;

    }

    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public User findById(Integer id) {
        return userDao.findById(id);
    }


    @Override
    @CacheEvict(allEntries = true)
    public boolean update(User user) {
        // 如果有密码传入(不是空字符串和null)
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(pe.encryptPassword(user.getPassword()));
        }

        user.setUpdatedBy("admin");
        user.setUpdatedTime(LocalDateTime.now());

        return userDao.update(user) > 0;
    }


    // 前端用户服务
    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public User Login(User user) {
        // 1.从数据库查询到该账户
        User userInDB = userDao.getUserByAccountForLogin(user.getAccount());
        if (userInDB == null) {
            return null;
        }
        // 2.若账户存在则检查密码
        boolean loginValid = pe.checkPassword(user.getPassword(), userInDB.getPassword());
        if (!loginValid) {
            return null;
        } else {
            return userInDB;
        }
    }

    @Override
    public boolean register(User user) {
        User userInDB = userDao.getUserByAccountForLogin(user.getAccount());
        if (userInDB != null) {
            return false;
        }
        user.setPassword(pe.encryptPassword(user.getPassword()));
        user.setCreatedBy(user.getAccount());
        user.setCreatedTime(LocalDateTime.now());
        return userDao.insert(user) > 0;
    }

    // 用户修改
    @Override
    @CacheEvict(allEntries = true)
    public boolean userUpdate(User user) {
        user.setUpdatedBy(user.getAccount());
        user.setUpdatedTime(LocalDateTime.now());

        return userDao.update(user) > 0;
    }


}
