package com.momo.theta.service;

import com.momo.theta.entity.UserInfo;

public interface SysUserService {
    UserInfo getUserByUserName(String userName);
}
