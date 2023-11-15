package com.momo.theta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.momo.theta.dto.UserAuthInfo;
import com.momo.theta.entity.SysUser;

/**
 * 用户业务接口
 *
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户名获取认证信息
     *
     * @param username
     * @return
     */
    UserAuthInfo getUserAuthInfo(String username);
}