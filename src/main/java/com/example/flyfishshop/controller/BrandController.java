package com.example.flyfishshop.controller;

import com.example.flyfishshop.config.AdminUserInterceptor;
import com.example.flyfishshop.model.Admin;
import com.example.flyfishshop.model.Brand;
import com.example.flyfishshop.service.BrandService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/brand")
public class BrandController {
    BrandService brandService;

    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute(AdminUserInterceptor.LOGIN_ADMIN_IDENTIFIED);
        model.addAttribute("admin", admin);
        model.addAttribute("contentPage", "brand/list");
        return "framework/index";
    }

    @GetMapping("/add")
    public String add() {
        return "brand/add";
    }

    @GetMapping("/edit")
    public String edit(Integer id, Model model) {
        if (id == null) {
            model.addAttribute("error", "id为空");
            return "brand/edit";
        }
        Brand brand = brandService.getBrandById(id);
        if(brand == null) {
            model.addAttribute("error","查询的品牌不存在");
            return "brand/edit";
        }
        model.addAttribute("brand", brand);
        return "brand/edit";
    }
}
