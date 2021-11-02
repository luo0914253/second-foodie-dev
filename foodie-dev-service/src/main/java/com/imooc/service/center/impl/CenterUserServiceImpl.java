package com.imooc.service.center.impl;

import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.service.center.CenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CenterUserServiceImpl implements CenterUserService {

    @Autowired
    private UsersMapper usersMapper;
    /**
     * 根据用户id查询用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public Users queryUserInfo(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        users.setPassword(null);
        return users;
    }

    /**
     * 修改用户信息
     *
     * @param userId
     * @param centerUserBO
     * @return
     */
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users users = new Users();
        BeanUtils.copyProperties(centerUserBO,users);
        users.setId(userId);
        users.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(users);
        return queryUserInfo(userId);
    }
}
