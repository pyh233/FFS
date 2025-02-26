package com.example.flyfishshop.service.impl;

import com.example.flyfishshop.dao.AddressDao;
import com.example.flyfishshop.model.Address;
import com.example.flyfishshop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "AddressService")
public class AddressServiceImpl implements AddressService {
    AddressDao addressDao;

    @Autowired
    public void setAddressDao(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public List<Address> findAllProvince() {
        return addressDao.findAllProvince();
    }

    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public List<Address> findAllCity(Integer provinceId) {
        return addressDao.getAllCity(provinceId);
    }

    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public List<Address> findAllArea(Integer cityId) {
        return addressDao.getAllArea(cityId);
    }

    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public List<Address> findAllStreet(Integer areaId) {
        return addressDao.getAllStreet(areaId);
    }
}
