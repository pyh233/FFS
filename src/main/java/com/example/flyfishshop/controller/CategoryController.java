package com.example.flyfishshop.controller;

import com.example.flyfishshop.config.AdminUserInterceptor;
import com.example.flyfishshop.model.Admin;
import com.example.flyfishshop.model.Category;
import com.example.flyfishshop.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {
    CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping("/list")
    public String list(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute(AdminUserInterceptor.LOGIN_ADMIN_IDENTIFIED);
        model.addAttribute("admin", admin);
        model.addAttribute("contentPage","category/list");
        return "framework/index";
    }

    @RequestMapping("/add")
    public String add(Integer id, Map<String,Object> model) {
        if(id == null) {
            id = 1;
        }
        Category parentCategory = categoryService.findNodeById(id);
        model.put("parentCategory", parentCategory);
        return "category/add";
    }
    @RequestMapping("/edit")
    public String edit(Integer id, Map<String,Object> model) {
        if(id == null) {
            model.put("error", "请选择修改的种类");
            return "category/edit";
        }
        Category editCategory = categoryService.findCategoryTreeById(id);
        if(editCategory == null) {
            model.put("error","修改的种类不存在请稍后再试");
            return "category/edit";
        }
        model.put("editCategory", editCategory);
        model.put("parentCategory", editCategory.getParent());
        return "category/edit";
    }
}
