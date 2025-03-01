package com.example.flyfishshop.service.impl;

import com.example.flyfishshop.dao.BrandDao;
import com.example.flyfishshop.model.Brand;
import com.example.flyfishshop.service.BrandService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@CacheConfig(cacheNames = "BrandService")
public class BrandServiceImpl implements BrandService {
    BrandDao brandDao;

    @Autowired
    public void setBrandDao(BrandDao brandDao) {
        this.brandDao = brandDao;
    }

    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public List<Brand> getAllBrands(Brand brand,Page<?> page) {
        try (Page<?> __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            return brandDao.findAllBrands(brand);
        }
    }

    // 根据id获取brand，brand自己肯定用得到，其次，可能商品也要用，可以通过spring获取，否则级联查询浪费资源.
    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public Brand getBrandById(Integer id) {
        return brandDao.findBrandById(id);
    }

    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public List<Brand> getAllBrands() {
        return brandDao.findAllBrands(new Brand());
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveBrand(Brand brand) {
        return brandDao.addBrand(brand)>0;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateBrand(Brand brand) {
        return brandDao.updateBrand(brand)>0;
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteBrandByIds(Integer[] ids) {
        if(ids.length==0 || ids==null){
            return 0;
        }
        return brandDao.deleteBrandByIds(ids);
    }
}
