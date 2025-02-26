package com.example.flyfishshop.controller;

import com.example.flyfishshop.config.AdminUserInterceptor;
import com.example.flyfishshop.model.Admin;
import com.example.flyfishshop.service.AdminService;
import com.example.flyfishshop.util.CommonAddGroup;
import com.example.flyfishshop.util.JsonResult;
import com.wf.captcha.SpecCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class IndexController {
    AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/index")
    public String main(Model model,HttpSession session) {
        // 将index原有的内容放在外部,首次访问的时候使用thymeleaf放入index页面中
        Admin admin = (Admin) session.getAttribute(AdminUserInterceptor.LOGIN_ADMIN_IDENTIFIED);
        model.addAttribute("admin", admin);
        model.addAttribute("contentPage", "framework/init-include");
        return "framework/index";
    }

    @GetMapping("/charts")
    public String charts() {
        return "framework/charts";
    }

    // 跳转登录
    @GetMapping("/login")
    public String toLogin() {
        return "admin/login";
    }

    // 登录请求
    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JsonResult> login(Admin admin,String captcha, HttpSession session) {
        // TODO: 验证码校验
        if(!captcha.equals(session.getAttribute("captcha"))) {
            return ResponseEntity.ok(JsonResult.fail("验证码不正确"));
        }
        Admin adminInDB = adminService.login(admin);
        if (adminInDB == null) {
            return ResponseEntity.ok(JsonResult.fail("登录失败，用户名或密码错误"));
        } else {
            session.setAttribute(AdminUserInterceptor.LOGIN_ADMIN_IDENTIFIED, adminInDB);
            return ResponseEntity.ok(JsonResult.success(" 登陆成功", adminInDB));
        }
    }

    // 跳转注册
    @GetMapping("/register")
    public String toRegister() {
        return "admin/register";
    }

    // 注册请求
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JsonResult> register(@Validated(CommonAddGroup.class) Admin admin) {
        return null;
    }

    // 验证码图片请求
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SpecCaptcha captcha = new SpecCaptcha(130,48, 4);
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        request.getSession().setAttribute("captcha", captcha.text().toLowerCase());// 已经放到请求与中了
        captcha.out(response.getOutputStream());
    }
    // 忘记密码跳转
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "admin/forgot-password";
    }
}
