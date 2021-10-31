package com.imooc.service.impl;

import com.imooc.enums.YseOrNo;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper addressMapper;

    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress address = new UserAddress();
        address.setUserId(userId);
        List<UserAddress> list = addressMapper.select(address);
        return list;
    }

    @Override
    public void addAddress(AddressBO addressBO) {
        Integer isDefault = 0;
        List<UserAddress> userAddressList = queryAll(addressBO.getUserId());
        if (CollectionUtils.isEmpty(userAddressList)) {
            isDefault = 1;
        }
        String id = Sid.next();
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,userAddress);
        userAddress.setId(id);
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        addressMapper.insert(userAddress);
    }

    @Override
    public void updateAddress(AddressBO addressBO) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,userAddress);
        userAddress.setId(addressBO.getAddressId());
        userAddress.setUpdatedTime(new Date());
        addressMapper.updateByPrimaryKeySelective(userAddress);
    }

    @Override
    public void delete(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setId(addressId);
        addressMapper.delete(userAddress);
    }

    @Override
    public void setDefaultAddress(String userId, String addressId) {
//      查找默认地址，设为不默认
        List<UserAddress> userAddressList = queryAll(userId);
        userAddressList.forEach(userAddress -> {
            if (userAddress.getIsDefault() == 1){
                userAddress.setIsDefault(0);
                addressMapper.updateByPrimaryKey(userAddress);
            }
        });
//      根据id修改为默认地址
        UserAddress userAddress = new UserAddress();
        userAddress.setIsDefault(YseOrNo.YES.type);
        userAddress.setUserId(userId);
        userAddress.setId(addressId);
        userAddress.setUpdatedTime(new Date());
        addressMapper.updateByPrimaryKeySelective(userAddress);
    }

    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setId(addressId);
        return addressMapper.selectOne(userAddress);
    }
}
