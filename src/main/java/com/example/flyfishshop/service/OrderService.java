package com.example.flyfishshop.service;

import com.example.flyfishshop.model.Order;
import com.example.flyfishshop.model.search.SearchOrderModel;
import com.github.pagehelper.Page;

import java.util.List;

public interface OrderService {
    public boolean createOrder(Order order,Integer[] ids);

    public Order getOrderById(Integer id);
    public Order getOrderByOrderNo(String orderNo);
    public Order getNotPayedOrder(Integer userId);
    public boolean patchOrder(Order order);
    public boolean patchPay(Order order);
    //我的订单列表
    List<Order> getAllOrdersByUid(Integer uid);
    // 收货
    boolean finishOrder(Integer orderId);
    // 退款
    boolean applyRefund(Order order);
    // =====================================
    // 后台获取所有订单
    List<Order> getAllOrders(SearchOrderModel searchOrderModel, Page<?> page);
    // 后台发货
    boolean putOrder(Integer orderId);
    // 后台同意退款
    boolean agreeRefund(Integer orderId);
}
