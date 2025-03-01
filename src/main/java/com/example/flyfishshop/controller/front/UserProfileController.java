package com.example.flyfishshop.controller.front;

import com.example.flyfishshop.config.UserLoginInterceptor;
import com.example.flyfishshop.model.Order;
import com.example.flyfishshop.model.OrderState;
import com.example.flyfishshop.model.User;
import com.example.flyfishshop.model.UserAddress;
import com.example.flyfishshop.service.OrderService;
import com.example.flyfishshop.service.UserAddressService;
import com.example.flyfishshop.service.UserService;
import com.example.flyfishshop.util.validate.CommonAddGroup;
import com.example.flyfishshop.util.JsonResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class UserProfileController {
    private UserAddressService userAddressService;
    private OrderService orderService;
    private UserService userService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setUserAddressService(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    // 我的简介
    @GetMapping("/my/profile")
    public String myProfile(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY);
        model.addAttribute("currentUser", currentUser);
        return "front/profile";
    }
    // TODO:简介订单数量总览

    // 编辑简介页面
    @GetMapping("/my/profile/edit")
    public String myProfileEdit(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY);
        model.addAttribute("currentUser", currentUser);
        return "front/edit-profile";
    }

    // 编辑请求
    // NOTE:用户自己更新了用户信息，记得将session中的user更新一下
    @PutMapping(value = "/my/profile/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JsonResult> updateMyProfile(User user, HttpSession session) {
        User currentUser = (User) session.getAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY);
        if (StringUtils.hasText(user.getAvatar())) {
            currentUser.setAvatar(user.getAvatar());
        }
        if (StringUtils.hasText(user.getName())) {
            currentUser.setName(user.getName());
        }
        if (StringUtils.hasText(user.getSex())) {
            currentUser.setSex(user.getSex());
        }
        // 更新当前用户
        session.setAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY, currentUser);
        boolean success = userService.userUpdate(currentUser);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("修改成功,即将跳转", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改失败"));
        }
    }

    // 我的地址
    @GetMapping("/my/address")
    public String myAddress(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY);
        List<UserAddress> userAddressList = userAddressService.findUserAddressByUid(currentUser.getId());
        model.addAttribute("userAddressList", userAddressList);
        return "front/address-list";
    }

    // 添加地址页面
    @GetMapping("/my/address/add")
    public String myAddressAdd(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY);
        model.addAttribute("currentUser", currentUser);
        return "front/add-address";
    }

    // 添加地址请求
    @PostMapping("/my/address/add")
    @ResponseBody
    public ResponseEntity<JsonResult> addMyAddress(@Validated(CommonAddGroup.class) UserAddress userAddress, HttpSession session) {
        User currentUser = (User) session.getAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY);
        userAddress.setMemberId(currentUser.getId());
        boolean success = userAddressService.addUserAddress(userAddress);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("success", userAddressService.findUserAddressByUid(currentUser.getId())));
        } else {
            return ResponseEntity.ok(JsonResult.fail("添加地址失败"));
        }
    }

    // 删除地址请求
    @DeleteMapping("/my/address/delete")
    @ResponseBody
    public ResponseEntity<JsonResult> deleteMyAddress(Integer[] ids, HttpSession session) {
        System.out.println(Arrays.toString(ids));
        int count = userAddressService.deleteUserAddressByIds(ids);
        if (count == 0) {
            return ResponseEntity.ok(JsonResult.fail("删除失败!"));
        } else {
            return ResponseEntity.ok(JsonResult.success("成功删除" + count + "条地址", null));
        }
    }


    // 我的订单
    @GetMapping("/my/orders")
    public String myOrders(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY);
        List<Order> orderList = orderService.getAllOrdersByUid(currentUser.getId());
        model.addAttribute("orderList", orderList);
        return "front/order-list";
    }

    // 收货请求
    @PatchMapping("/order/finish")
    @ResponseBody
    public ResponseEntity<JsonResult> finishOrder(Integer orderId, HttpSession session) {
        if (orderId == null) {
            return ResponseEntity.badRequest().build();
        }
        boolean success = orderService.finishOrder(orderId);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("收货成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("fail"));
        }
    }

    // 申请退款页面
    @GetMapping("/order/refund")
    public String toApplyRefund(Integer orderId, Model model) {
        if (orderId == null) {
            model.addAttribute("error", "请选择要退款的订单");
            return "front/edit-refund";
        }
        Order order = orderService.getOrderById(orderId);
        if (order == null || order.getPayTime() == null) {
            model.addAttribute("error", "您还未支付/订单未生成，请稍后再试");
            return "front/edit-refund";
        }
        model.addAttribute("refundOrder", order);
        return "front/edit-refund";
    }

    // 退款请求
    @PutMapping(value = "/order/refund", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JsonResult> refundOrder(Integer orderId, String refundCause, HttpSession session) {
        if (orderId == null) {
            return ResponseEntity.badRequest().build();
        }
        Order order = new Order();
        order.setId(orderId);
        order.setState(OrderState.REFUNDING);
        order.setRefundCause(refundCause);
        order.setApplyRefundTime(LocalDateTime.now());
        boolean success = orderService.applyRefund(order);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("success", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("fail"));
        }
    }
}
