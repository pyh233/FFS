package com.example.flyfishshop.service;

import com.example.flyfishshop.model.Address;

import java.util.List;

public interface AddressService {

    List<Address> findAllProvince();
    List<Address> findAllCity(Integer provinceId);
    List<Address> findAllArea(Integer cityId);
    List<Address> findAllStreet(Integer areaId);

}
