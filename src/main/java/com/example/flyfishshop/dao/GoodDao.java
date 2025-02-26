package com.example.flyfishshop.dao;

import com.example.flyfishshop.model.Good;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodDao {
    public List<Good> findAllGoods(@Param("sg") Good searchGood, Integer[] ids);
    public int deleteGood(Integer[] ids);
    public int addGood(Good good);
    public int updateGood(Good good);
    public Good findGoodById(Integer id);
}
