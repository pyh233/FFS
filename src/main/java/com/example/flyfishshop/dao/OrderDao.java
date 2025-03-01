package com.example.flyfishshop.dao;

import com.example.flyfishshop.model.ItemInOrder;
import com.example.flyfishshop.model.Order;
import com.example.flyfishshop.model.search.SearchOrderModel;
import org.apache.ibatis.annotations.Mapper;
import org.aspectj.weaver.ast.Or;

import java.util.List;

@Mapper
public interface OrderDao {
    // 创建订单，添加收货人信息，添加支付信息，添加收货信息，添加退款信息
    int createOrder(Order order);
    int patchOrder(Order order);
    int patchPay(Order order);
    int finishOrder(Order order);
    int refundOrder(Order order);
    // 获取用户所有订单，最近未支付订单
    List<Order> getAllOrdersByUserId(Integer userId);
    Order getUserLatestOrderNotPay(Integer userId);
    Order getOrderById(Integer id);
    Order getOrderByOrderNo(String orderNo);

    // =============================================================
    List<Order> getOrderList(SearchOrderModel som);
    // 发货
    int putOrder(Order order);
    // 接受退款
    int agreeRefund(Order order);
}
