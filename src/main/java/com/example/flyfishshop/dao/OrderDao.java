package com.example.flyfishshop.dao;

import com.example.flyfishshop.model.ItemInOrder;
import com.example.flyfishshop.model.Order;
import com.example.flyfishshop.model.search.SearchOrderModel;
import org.apache.ibatis.annotations.Mapper;
import org.aspectj.weaver.ast.Or;

import java.util.List;

@Mapper
public interface OrderDao {
    // 订单
    int createOrder(Order order);
    Order getOrderById(Integer id);
    Order getOrderByOrderNo(String orderNo);
    int patchOrder(Order order);
    int patchPay(Order order);
    List<Order> getAllOrdersByUserId(Integer userId);
    Order getUserLatestOrderNotPay(Integer userId);
    // 订单项
    // 保存订单项
    int saveOrderItem(ItemInOrder itemInOrder);
    // 根据订单id获取订单项
    List<ItemInOrder> getOrderItemsByOrderId(Integer orderId);
    // 收货
    int finishOrder(Order order);
    // 申请退款
    int refundOrder(Order order);
    // =============================================================
    List<Order> getOrderList(SearchOrderModel som);
    int putOrder(Order order);
    // 接受退款
    int agreeRefund(Order order);
}
