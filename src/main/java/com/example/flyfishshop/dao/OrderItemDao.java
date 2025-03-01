package com.example.flyfishshop.dao;

import com.example.flyfishshop.model.ItemInOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemDao {
    int saveOrderItem(ItemInOrder itemInOrder);

    List<ItemInOrder> getOrderItemsByOrderId(Integer orderId);
}
