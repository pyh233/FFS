package com.example.flyfishshop.dao;

import com.example.flyfishshop.model.ItemInCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 购物车中项目
 */
@Mapper
public interface CartItemDao {

    // 添加到购物车
    public int addCartItem(ItemInCart itemInCart);
    // 通过用户id和商品id检查购物车中是否存在该商品
    public ItemInCart getCartItemByMidAndGid(ItemInCart itemInCart);
    // 如果存在就修改商品信息而不是添加
    public int updateCartItem(ItemInCart itemInCart);

    // 查看我的购物车列表
    public List<ItemInCart> getCartItemsByMebId(Integer memberId);
    // 根据id删除购物车列表项
    public int deleteCartItem(ItemInCart itemInCart);

    // 根据id获取购物车中项
    public ItemInCart getCartItemById(Integer id);
}
