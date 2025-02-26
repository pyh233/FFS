package com.example.flyfishshop.service;

import com.example.flyfishshop.model.Good;
import com.github.pagehelper.Page;

import java.util.List;

public interface GoodService {
    public List<Good> getAllGoods(Good searchGood,Page<?> page);
    public int deleteGoodByIds(Integer[] ids);
    public boolean addGood(Good good);
    public Good getGoodById(Integer id);
    public boolean updateGood(Good good);
}
