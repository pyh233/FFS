package com.example.flyfishshop.service.impl;

import com.example.flyfishshop.dao.CartItemDao;
import com.example.flyfishshop.model.ItemInCart;
import com.example.flyfishshop.service.ItemInCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public
class ItemInCartServiceImpl implements ItemInCartService {
    CartItemDao cartItemDao;

    @Autowired
    public void setCartItemDao(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    // 往购物车添加商品
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addItemInCart(ItemInCart itemInCart) {
        ItemInCart itemInCart1DB = cartItemDao.getCartItemByMidAndGid(itemInCart);
        // 如果购物车中没有东西就会插入
        if(itemInCart1DB == null) {
            return cartItemDao.addCartItem(itemInCart)>0;
        }else {// 如果有东西就会添加进去
            itemInCart.setQty(itemInCart.getQty() + itemInCart1DB.getQty());
            return cartItemDao.updateCartItem(itemInCart)>0;
        }
    }

    @Override
    public List<ItemInCart> getAllItemsByMebId(Integer memberId) {
        return cartItemDao.getCartItemsByMebId(memberId);
    }

    @Override
    public boolean updateItemInCart(ItemInCart itemInCart) {
        return cartItemDao.updateCartItem(itemInCart)>0;
    }

    @Override
    public boolean removeItemInCart(ItemInCart itemInCart) {
        return cartItemDao.deleteCartItem(itemInCart)>0;
    }

}
