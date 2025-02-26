package com.example.flyfishshop.controller;

import com.example.flyfishshop.config.AdminUserInterceptor;
import com.example.flyfishshop.model.Admin;
import com.example.flyfishshop.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/admin")
public class AdminController {
    AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/list")
    public String list(Model model,HttpSession session) {
        Admin admin = (Admin) session.getAttribute(AdminUserInterceptor.LOGIN_ADMIN_IDENTIFIED);
        model.addAttribute("admin", admin);
        model.addAttribute("contentPage", "admin/list");
        return "framework/index";
    }

    @GetMapping("/add")
    public String add() {
        return "admin/add";
    }

    @GetMapping("/edit")
    public String edit(Integer id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择查询的id");
            return "admin/edit";
        }
        Admin editAdmin = adminService.findById(id);
        if (editAdmin == null) {
            model.addAttribute("error","要修改的admin不存在");
            return "admin/edit";
        }
        model.addAttribute("editAdmin", editAdmin);
        return "admin/edit";
    }
}
