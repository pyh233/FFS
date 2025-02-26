package com.example.flyfishshop.controller;

import com.example.flyfishshop.config.AdminUserInterceptor;
import com.example.flyfishshop.model.Admin;
import com.example.flyfishshop.model.Order;
import com.example.flyfishshop.service.OrderService;
import com.example.flyfishshop.util.JsonResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;

@Controller
@RequestMapping("/admin/order")
public class OrderController {
    OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
    // orderList页面
    @RequestMapping("/list")
    public String list(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute(AdminUserInterceptor.LOGIN_ADMIN_IDENTIFIED);
        model.addAttribute("admin", admin);
        model.addAttribute("contentPage", "order/list");
        return "framework/index";
    }
    // 发货
    @PatchMapping(value = "/put", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JsonResult> delivery(Integer orderId) {
        boolean success = orderService.putOrder(orderId);
        if(success) {
            return ResponseEntity.ok(JsonResult.success("发货成功",null));
        }else {
            return ResponseEntity.ok(JsonResult.fail("发货失败"));
        }
    }
    // 同意退款
    @PatchMapping(value = "/refund/agree",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JsonResult> refundAgree(Integer orderId) {
        boolean success = orderService.agreeRefund(orderId);
        if(success) {
            return ResponseEntity.ok(JsonResult.success("接收买家退款成功",null));
        }else {
            return ResponseEntity.ok(JsonResult.fail("接收买家退款失败"));
        }
    }
    // 跳转编辑
    @GetMapping("/edit")
    public String edit(Integer orderId, Model model) {
        if(orderId == null) {
            model.addAttribute("error", "请选择修改的商品");
            return "order/edit";
        }
        Order order = orderService.getOrderById(orderId);
        if(order == null) {
            model.addAttribute("error","要修改的订单已不存在");
            return "order/edit";
        }
        model.addAttribute("editOrder", order);
        return "order/edit";
    }
}
