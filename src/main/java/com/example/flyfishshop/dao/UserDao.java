package com.example.flyfishshop.dao;

import com.example.flyfishshop.model.search.SearchUserModel;
import com.example.flyfishshop.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {
    // 后台管理使用
    public List<User> findAll(SearchUserModel sum);
    public int deleteByIds(Integer[] ids);
    public int insert(User user);
    public int update(User user);
    public User findById(Integer id);

    // 前端登录以及修改密码
    public User getUserByAccountForLogin(String name);
}
