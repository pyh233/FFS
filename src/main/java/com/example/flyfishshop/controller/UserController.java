package com.example.flyfishshop.controller;


import com.example.flyfishshop.config.AdminUserInterceptor;
import com.example.flyfishshop.model.Admin;
import com.example.flyfishshop.model.User;
import com.example.flyfishshop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/admin/user")
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute(AdminUserInterceptor.LOGIN_ADMIN_IDENTIFIED);
        model.addAttribute("admin", admin);
        model.addAttribute("contentPage","user/list");;
        return "framework/index";
    }
    @GetMapping("/add")
    public String add() {
        return "user/add";
    }
    @GetMapping("/edit")
    public String edit(@RequestParam("id") Integer id, Map<String, Object> map) {
        if (id == null) {
            map.put("error", "修改的用户编号不可为空");
            return "user/edit";
        }
        User user = userService.findById(id);
        if(user == null) {
            map.put("error","修改的用户不存在");
            return "user/edit";
        }

        map.put("user", user);
        return "user/edit";
    }
}
