package com.momo.theta.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.momo.theta.dto.UserAuthInfo;
import com.momo.theta.entity.SysUser;

public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名获取认证信息
     *
     * @param username
     * @return
     */
    UserAuthInfo getUserAuthInfo(String username);

}