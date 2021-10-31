package com.imooc.service;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {
//  根据用户id查询收货地址列表
    public List<UserAddress> queryAll(String userId);
//  新增地址
    void addAddress(AddressBO addressBO);
//  修改地址
    void updateAddress(AddressBO addressBO);
//  删除地址
    void delete(String userId, String addressId);
//  设置默认地址
    void setDefaultAddress(String userId, String addressId);

//  根据用户名id和地址id查询地址
    UserAddress queryUserAddress(String userId,String addressId);
}
