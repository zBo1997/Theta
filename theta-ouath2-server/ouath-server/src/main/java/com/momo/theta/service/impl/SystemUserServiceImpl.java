package com.momo.theta.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.momo.theta.entity.UserInfo;
import com.momo.theta.mapper.UserInfoMapper;
import com.momo.theta.service.SystemUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SystemUserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements
        SystemUserService {

    @Override
    public UserInfo getUserByUserName(String userName) {
        ArrayList<String> list = new ArrayList<>();
        list.add("ADMIN");
        UserInfo user = new UserInfo();
        user.setUserId("1111");
        user.setPassword("123321");
        user.setUsername(userName);
        user.setPermissions(list);
        return user;
    }
}