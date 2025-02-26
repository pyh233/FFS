package com.example.flyfishshop.service;

import com.example.flyfishshop.model.UserAddress;

import java.util.List;

public interface UserAddressService {
    List<UserAddress> findUserAddressByUid(Integer id);

    boolean addUserAddress(UserAddress userAddress);
    int deleteUserAddressByIds(Integer[] ids);
}
