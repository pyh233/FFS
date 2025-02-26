package com.example.flyfishshop.service.impl;

import com.example.flyfishshop.dao.UserAddressDao;
import com.example.flyfishshop.model.UserAddress;
import com.example.flyfishshop.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    UserAddressDao userAddressDao;

    @Autowired
    public void setUserAddressDao(UserAddressDao userAddressDao) {
        this.userAddressDao = userAddressDao;
    }

    @Override
    public List<UserAddress> findUserAddressByUid(Integer id) {
        return userAddressDao.findUserAddressByUid(id);
    }

    @Override
    public boolean addUserAddress(UserAddress userAddress) {
        //  默认是不设置默认值的
        userAddress.setIsDefault(false);
        return userAddressDao.insertUserAddress(userAddress)>0;
    }

    @Override
    public int deleteUserAddressByIds(Integer[] ids) {
        if(ids == null || ids.length == 0) {
            return 0;
        }
        return userAddressDao.deleteUserAddressByIds(ids);
    }
}
