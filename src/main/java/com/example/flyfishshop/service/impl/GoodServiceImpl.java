package com.example.flyfishshop.service.impl;

import com.example.flyfishshop.dao.BrandDao;
import com.example.flyfishshop.dao.CategoryDao;
import com.example.flyfishshop.dao.GoodDao;
import com.example.flyfishshop.model.Good;
import com.example.flyfishshop.service.GoodService;
import com.example.flyfishshop.util.CategoryHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@CacheConfig(cacheNames = "GoodService")
public class GoodServiceImpl implements GoodService {
    GoodDao goodDao;
    // 使用缓存比较好
    BrandDao brandDao;
    CategoryDao categoryDao;

    @Autowired
    public void setGoodDao(GoodDao goodDao) {
        this.goodDao = goodDao;
    }

    @Autowired
    public void setBrand(BrandDao brandDao) {
        this.brandDao = brandDao;
    }

    @Autowired
    public void setCategory(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<Good> getAllGoods(Good searchGood, Page<?> page) {
        try (Page<?> __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            Integer[] ids = null;
            if (searchGood != null && searchGood.getId() != null) {
                ids = CategoryHelper.getCategoryTreeAllId(categoryDao.findTreeById(searchGood.getCategoryId()));
            }
            return goodDao.findAllGoods(searchGood, ids);
        }
    }

    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public Good getGoodById(Integer id) {
        //  参数验证已经在controller层完成
        Good good = goodDao.findGoodById(id);
        good.setBrand(brandDao.findBrandById(good.getBrandId()));
        good.setCategory(categoryDao.findNodeById(good.getCategoryId()));
        return good;
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteGoodByIds(Integer[] ids) {
        // 参数验证
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return goodDao.deleteGood(ids);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean addGood(Good good) {
        good.setCreatedBy("admin");
        good.setCreatedTime(LocalDateTime.now());
        // 参数验证已经使用注解完成
        return goodDao.addGood(good) > 0;
    }


    @Override
    @CacheEvict(allEntries = true)
    public boolean updateGood(Good good) {
        good.setUpdatedBy("admin");
        good.setUpdatedTime(LocalDateTime.now());
        // 参数验证已经使用注解完成
        return goodDao.updateGood(good) > 0;
    }

}
