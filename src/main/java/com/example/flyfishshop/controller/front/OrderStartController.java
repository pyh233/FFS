package com.example.flyfishshop.controller.front;

import com.example.flyfishshop.config.UserLoginInterceptor;
import com.example.flyfishshop.model.Order;
import com.example.flyfishshop.model.User;
import com.example.flyfishshop.service.OrderService;
import com.example.flyfishshop.service.UserAddressService;
import com.example.flyfishshop.util.JsonResult;
import com.example.flyfishshop.util.validate.OrderDataPatch;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderStartController {
    OrderService orderService;
    UserAddressService userAddressService;

    @Autowired
    public void setUserAddressService(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    // 创建订单(需要检查是否存在未支付订单，否则不可以新建订单)
    @PostMapping(value = "/order/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JsonResult> createOrder(Integer[] ids, HttpSession session, Model model) {
        if (ids == null || ids.length == 0) {
            return ResponseEntity.ok(JsonResult.fail("请选择一件商品进行购买!"));
        }
        User currentUser = (User) session.getAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY);
        Order order = new Order();
        order.setMemberId(currentUser.getId());
        boolean success = orderService.createOrder(order, ids);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("成功创建订单", order.getId()));
        } else {
            return ResponseEntity.ok(JsonResult.fail("创建订单失败"));
        }
    }

    // 创建订单成功后返回订单信息页面
    @GetMapping("/order/checkout")
    public String checkout(Integer id, Model model) {
        Order order = orderService.getOrderById(id);
        // 如果没有此商品或者已经支付完成就没有这个订单页面了
        if (order == null || order.getPayTime() != null) {
            return "front/404";
        }
        // 基础订单
        model.addAttribute("order", order);
        // 用户的所有收获信息 NOTE:通过api接口异步访问，可以减轻服务器压力?
//        model.addAttribute("addressList", userAddressService.findUserAddressByUid(order.getMemberId()));
        return "front/checkout";
    }

    // 完善订单，添加订单信息,收货人。。。准备发起支付宝请求
    @PatchMapping("/order/patch")
    @ResponseBody
    public ResponseEntity<JsonResult> updateOrder(@Validated(OrderDataPatch.class) Order order, Integer streetId, HttpSession session) {
        boolean success = orderService.patchOrder(order);
        if (success) {
            // ok请求成功，修改成功
            return ResponseEntity.ok(JsonResult.success("success", order));
        } else {
            // ok请求成功,修改失败
            return ResponseEntity.ok(JsonResult.fail("fail"));
        }
    }
}
