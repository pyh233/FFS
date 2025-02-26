package com.example.flyfishshop.service;

import com.example.flyfishshop.model.ItemInCart;

import java.util.List;

public interface ItemInCartService {
    //添加购物车项
    public boolean addItemInCart(ItemInCart itemInCart);

    // 查看我的个人购物车页面
    public List<ItemInCart> getAllItemsByMebId(Integer memberId);

    // 修改购物车项数量
    public boolean updateItemInCart(ItemInCart itemInCart);

    // 删除购物车项
    public boolean removeItemInCart(ItemInCart itemInCart);
}
