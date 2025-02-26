package com.example.flyfishshop.controller.front;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.flyfishshop.config.UserLoginInterceptor;
import com.example.flyfishshop.model.*;
import com.example.flyfishshop.model.search.FrontGoodSearchModel;
import com.example.flyfishshop.service.*;
import com.example.flyfishshop.util.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class FrontController {
    // user登录使用
    private UserService userService;
    // 商品列表和商品详情页
    private GoodService goodService;
    // 订单服务
    private OrderService orderService;

    @Autowired
    public void setGoodService(GoodService goodService) {
        this.goodService = goodService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    // 主页面
    @GetMapping("/index")
    public String index(Model model) {
        List<Good> goodList = goodService.getAllGoods(null, new Page<>(1, 8));
        model.addAttribute("goods", goodList);
        return "front/index";
    }

    // 登陆页面
    @GetMapping("/login")
    public String toLogin() {
        return "front/login";
    }

    // 登录请求
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JsonResult> login(@Validated({CommonLoginGroup.class}) User user, HttpSession session) {
        //  TODO:验证码验证

        User userInDB = userService.Login(user);
        if (userInDB != null) {
            session.setAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY, userInDB);
            return ResponseEntity.ok(JsonResult.success("登陆成功前端跳转首页", null));
        } else {
            // 这个401是登录功能发送的401 与拦截器发送的401不同
            // 这个401不需要前台ajax做处理，只需要判断不ok则输出错误信息就可以了
            return ResponseEntity.status(401).body(JsonResult.fail("登录失败，用户名或密码错误!"));
        }
    }

    // 登出请求
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request) {
        session.removeAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY);
        return "redirect:" + request.getContextPath() + "/index";
    }

    // 注册页面
    @GetMapping("/register")
    public String toRegister() {
        return "front/register";
    }

    // 注册请求
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JsonResult> register(@Validated(CommonRegisterGroup.class) User user, HttpSession session) {
        boolean success = userService.register(user);
        if(success){
            session.setAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY, user);
            return ResponseEntity.ok(JsonResult.success("创建用户成功!即将跳转", null));
        }else {
            return ResponseEntity.ok(JsonResult.fail("用户已存在"));
        }
    }

    // 修改密码页面
    @GetMapping("/forgot-pw")
    public String toForgetPw() {
        return "front/forgot-password";
    }

    // 商品详情页
    @GetMapping("/good-show")
    public String findGoodsByCategoryId(Integer gid, Model model) {
        // 1.查询细节商品
        Good good = goodService.getGoodById(gid);
        // 2.如果有细节图片
        if (StringUtils.hasText(good.getOtherPics())) {
            List<String> pics = Arrays.asList(good.getOtherPics().split(","));
            model.addAttribute("pics", pics);
        }
        // 3.储存商品
        model.addAttribute("good", good);
        return "front/good-show";
    }

    // 根据关键词和类别查询商品列表初始页面
    @GetMapping("/shop-list")
    public String goods(FrontGoodSearchModel fgsm, Model model) {
        fgsm.setName(fgsm.getKeyword());
        System.out.println(fgsm);
        Page<?> page = new Page<>(1, 8);
        List<Good> goodList = goodService.getAllGoods(fgsm, page);


        PageInfo<?> pi = new PageInfo<>(goodList);
        model.addAttribute("fgsm", fgsm);
        model.addAttribute("goodList", goodList);
        model.addAttribute("pageInfo", pi);
        Integer[] pages = new Integer[]{1, 2, 3};
        model.addAttribute("pages", pages);
        return "front/shop-list";
    }
}
