package com.example.flyfishshop.service.impl;

import com.example.flyfishshop.dao.CartItemDao;
import com.example.flyfishshop.dao.GoodDao;
import com.example.flyfishshop.dao.OrderDao;
import com.example.flyfishshop.dao.UserDao;
import com.example.flyfishshop.model.*;
import com.example.flyfishshop.model.search.SearchOrderModel;
import com.example.flyfishshop.service.OrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@CacheConfig(cacheNames = "OrderServiceImpl")
public class OrderServiceImpl implements OrderService {
    private OrderDao orderDao;
    private CartItemDao cartItemDao;
    private GoodDao goodDao;

    @Autowired
    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Autowired
    public void setCartItemDao(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    @Autowired
    public void setGoodDao(GoodDao goodDao) {
        this.goodDao = goodDao;
    }

    // 创建订单，生成唯一订单号
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(allEntries = true)
    public boolean createOrder(Order order, Integer[] ids) {
        //  order已经有用户信息了
        Random r = new Random();
        order.setOrderNo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + r.nextInt(100000, 1000000));
        order.setState(OrderState.NOT_PAY);
        order.setCreatedTime(LocalDateTime.now());
        // TODO:存储默认地址信息

        // 创建订单,写操作
        int count = orderDao.createOrder(order);
        if (count == 0) {
            throw new RuntimeException("保存订单失败");
        }
        // 保存订单项,同时删除购物车项,写操作
        // ids中存储着User的ItemInCart的id
        for (Integer id : ids) {
            // 获取购物车上的项
            ItemInCart itemInCart = cartItemDao.getCartItemById(id);
            // 从购物车上获取商品
            Good good = goodDao.findGoodById(itemInCart.getGoodId());
            if (good == null) {
                throw new RuntimeException("要购买的商品不存在!");
            }
            // TODO:需要先将商品表中商品数量减少，再添加订单项，如果减少到0以下失败->事务回滚

            // 添加订单项,最重要的就是订单id
            ItemInOrder itemInOrder = new ItemInOrder();

            itemInOrder.setOrderId(order.getId());
            itemInOrder.setGoodId(itemInCart.getGoodId());
            itemInOrder.setPrice(BigDecimal.valueOf(good.getPrice()));
            itemInOrder.setCount(itemInCart.getQty());

            itemInOrder.setProductName(good.getName());
            itemInOrder.setProductPic(good.getPic());
            itemInOrder.setProductSummary(good.getSummary());
            // 订单项借用订单的dao
            this.orderDao.saveOrderItem(itemInOrder);
            // 删除购物车项
            this.cartItemDao.deleteCartItem(itemInCart);
        }
        return true;
    }


    // 根据id查询订单(带着订单项一起)
    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public Order getOrderById(Integer id) {
        Order order = orderDao.getOrderById(id);
        if (id == null || order == null) {
            return null;
        }
        order.setOrderItemList(orderDao.getOrderItemsByOrderId(order.getId()));
        return order;
    }

    // 获取订单
    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public Order getOrderByOrderNo(String orderNo) {
        Order order = orderDao.getOrderByOrderNo(orderNo);
        if (order == null || !StringUtils.hasText(orderNo)) {
            return null;
        }
        return order;
    }

    // 获取最近一笔未支付订单
    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public Order getNotPayedOrder(Integer userId) {
        return orderDao.getUserLatestOrderNotPay(userId);
    }

    // 完善订单地址信息和收货信息
    @Override
    @CacheEvict(allEntries = true)
    public boolean patchOrder(Order order) {
        return orderDao.patchOrder(order) > 0;
    }

    // 完善订单支付信息
    @Override
    @CacheEvict(allEntries = true)
    public boolean patchPay(Order order) {
        return orderDao.patchPay(order) > 0;
    }

    // 获取个人订单
    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public List<Order> getAllOrdersByUid(Integer uid) {
        List<Order> orderList = orderDao.getAllOrdersByUserId(uid);
        for (int i = 0; i < orderList.size(); i++) {
            orderList.get(i).setOrderItemList(orderDao.getOrderItemsByOrderId(orderList.get(i).getId()));
        }
        return orderList;
    }

    // 收货
    @Override
    @CacheEvict(allEntries = true)
    public boolean finishOrder(Integer orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setConfirmTime(LocalDateTime.now());
        order.setState(OrderState.RECEIVED);
        return orderDao.finishOrder(order) > 0;
    }

    // 申请退款
    @Override
    @CacheEvict(allEntries = true)
    public boolean applyRefund(Order order) {
        return orderDao.refundOrder(order) > 0;
    }

    // 获取订单列表
    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public List<Order> getAllOrders(SearchOrderModel searchOrderModel, Page<?> page) {
        try (Page<?> __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            return orderDao.getOrderList(searchOrderModel);
        }
    }

    // 发货
    @Override
    @CacheEvict(allEntries = true)
    public boolean putOrder(Integer orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setState(OrderState.SHIPPED);
        order.setPutTime(LocalDateTime.now());
        return orderDao.putOrder(order) > 0;
    }

    @Override
    @CacheEvict(allEntries = true)
    // TODO:接收退款应该还要返回支付宝余额
    @Transactional(rollbackFor = Exception.class)
    public boolean agreeRefund(Integer orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setState(OrderState.REFUNDED);
        order.setAgreeRefundTime(LocalDateTime.now());
        return orderDao.agreeRefund(order) > 0;
    }
}
