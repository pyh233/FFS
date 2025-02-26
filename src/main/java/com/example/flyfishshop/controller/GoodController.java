package com.example.flyfishshop.controller;

import com.example.flyfishshop.config.AdminUserInterceptor;
import com.example.flyfishshop.dao.GoodDao;
import com.example.flyfishshop.model.Admin;
import com.example.flyfishshop.model.Good;
import com.example.flyfishshop.service.GoodService;
import com.example.flyfishshop.util.JsonResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/admin/good")
public class GoodController {
    GoodService goodService;

    @Autowired
    public void setGoodService(GoodService goodService) {
        this.goodService = goodService;
    }

    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute(AdminUserInterceptor.LOGIN_ADMIN_IDENTIFIED);
        model.addAttribute("admin", admin);
        model.addAttribute("contentPage","good/list");
        return "framework/index";
    }

    @GetMapping("/add")
    public String add() {
        return "good/add";
    }

    @GetMapping("/edit")
    public String edit(Integer id, Map<String, Object> model) {
        if (id == null) {
            model.put("error", "请选择查询商品id");
            return "good/edit";
        }
        Good good = goodService.getGoodById(id);
        if (good == null) {
            model.put("error","查询的用户不存在");
            return "good/edit";
        }
        model.put("good", good);
        return "good/edit";
    }
    @GetMapping("/show")
    public String show(Integer id, Map<String, Object> model) {
        if (id == null) {
            model.put("error","请选择查看的商品");
            return "good/show";
        }
        Good good = goodService.getGoodById(id);
        if (good == null) {
            model.put("error","查看的商品不存在");
            return "good/show";

        }
        model.put("good", good);
        return "good/show";
    }
}
