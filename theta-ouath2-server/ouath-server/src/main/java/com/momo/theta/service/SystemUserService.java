package com.momo.theta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.momo.theta.entity.UserInfo;

public interface SystemUserService extends IService<UserInfo> {
    UserInfo getUserByUserName(String userName);
}
