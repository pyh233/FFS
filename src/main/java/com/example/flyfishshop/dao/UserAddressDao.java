package com.example.flyfishshop.dao;

import com.example.flyfishshop.model.UserAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAddressDao {
    List<UserAddress> findUserAddressByUid(Integer id);
    int deleteUserAddressByIds(Integer[] ids);
    int insertUserAddress(UserAddress userAddress);
    int updateUserAddressById(UserAddress userAddress);
}
