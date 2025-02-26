package com.example.flyfishshop.dao;

import com.example.flyfishshop.model.Brand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BrandDao {
    public List<Brand> findBrandsToShow(Brand brand);
    public List<Brand> findAllBrands();
    public Brand findBrandById(Integer id);
    public int deleteBrandByIds(Integer[] ids);
    public int addBrand(Brand brand);
    public int updateBrand(Brand brand);
}
