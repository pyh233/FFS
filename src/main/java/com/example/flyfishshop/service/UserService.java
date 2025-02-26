package com.example.flyfishshop.service;

import com.example.flyfishshop.model.search.SearchUserModel;
import com.example.flyfishshop.model.User;
import com.github.pagehelper.Page;

import java.util.List;

public interface UserService {
    public List<User> list(SearchUserModel sum, Page<?> page);
    public int delete(Integer[] ids);
    public boolean insert(User user);
    public User findById(Integer id);
    public boolean update(User user);

    public User Login(User user);
    public boolean register(User user);
    public boolean userUpdate(User user);
}
