package com.momo.theta.service.impl;

import com.momo.theta.entity.UserInfo;
import com.momo.theta.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Override
    public UserInfo getUserByUserName(String userName) {
        List<String> list = new ArrayList<>();
        list.add("ADMIN");
        UserInfo user = new UserInfo();
        user.setUserId("1111");
        user.setPassword("123321");
        user.setUserName(userName);
        user.setPermissions(list);
        return user;
    }
}
