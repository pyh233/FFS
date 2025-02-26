package com.example.flyfishshop.controller.front;

import com.example.flyfishshop.config.UserLoginInterceptor;
import com.example.flyfishshop.model.ItemInCart;
import com.example.flyfishshop.model.User;
import com.example.flyfishshop.service.ItemInCartService;
import com.example.flyfishshop.util.JsonResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class CartController {
    ItemInCartService itemInCartService;

    @Autowired
    public void setItemInCartService(ItemInCartService itemInCartService) {
        this.itemInCartService = itemInCartService;
    }

    // 添加到购物车请求
    @PostMapping(value = "/cart/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JsonResult> addToCart(ItemInCart itemInCart, HttpSession session) {
        // user一定存在，如果不存在就会被拦截器拦截进行登录登陆后一定存在
        User currentUser = (User) session.getAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY);
        itemInCart.setMemberId(currentUser.getId());
        boolean success = itemInCartService.addItemInCart(itemInCart);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("添加到购物车成功", null));
        } else {
            return ResponseEntity.badRequest().body(JsonResult.fail("添加到购物车失败"));
        }
    }

    // 查看购物车页面(初始化)
    @GetMapping("/cart")
    public String cart(Map<String, Object> map, HttpSession session) {
        User currentUser = (User) session.getAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY);
        List<ItemInCart> cartList = itemInCartService.getAllItemsByMebId(currentUser.getId());
        map.put("cartList", cartList);
        return "front/cart";
    }

    // 修改购物车中商品数量
    @PatchMapping(value = "/cart/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JsonResult> updateCart(ItemInCart itemInCart, HttpSession session) {
        // 使用添加商品时使用的更新语句需要传入额外数据
        User currentUser = (User) session.getAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY);
        itemInCart.setMemberId(currentUser.getId());
        boolean success = itemInCartService.updateItemInCart(itemInCart);
        if (success) {
            List<ItemInCart> cartList = itemInCartService.getAllItemsByMebId(currentUser.getId());
            return ResponseEntity.ok(JsonResult.success("修改成功", cartList));
        } else {
            return ResponseEntity.badRequest().body(JsonResult.fail("修改失败"));
        }
    }

    // 删除购物车中项
    @DeleteMapping(value = "/cart/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JsonResult> delete(ItemInCart itemInCart, HttpSession session, Map<String, Object> map) {
        User currentUser = (User) session.getAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY);
        itemInCart.setMemberId(currentUser.getId());
        // 可以使用userid和goodid一起删除 但是不如使用id直接删除简单
        boolean success = itemInCartService.removeItemInCart(itemInCart);
        if (success) {
            List<ItemInCart> cartList = itemInCartService.getAllItemsByMebId(currentUser.getId());
            return ResponseEntity.ok(JsonResult.success("删除成功", cartList));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

}
