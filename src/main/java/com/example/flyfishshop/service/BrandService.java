package com.example.flyfishshop.service;

import com.example.flyfishshop.model.Brand;
import com.github.pagehelper.Page;

import java.util.List;

public interface BrandService {
    public List<Brand> getAllBrands(Brand brand,Page<?> page);
    public boolean saveBrand(Brand brand);
    public boolean updateBrand(Brand brand);
    public int deleteBrandByIds(Integer[] ids);
    
    public Brand getBrandById(Integer id);
    public List<Brand> getAllBrands();
}
