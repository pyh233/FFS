package com.example.flyfishshop.dao;

import com.example.flyfishshop.model.Address;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressDao {
    public List<Address> findAllProvince();
    public List<Address> getAllCity(Integer provinceId);
    public List<Address> getAllArea(Integer cityId);
    public List<Address> getAllStreet(Integer areaId);

    public Address findById(Integer id);
}
