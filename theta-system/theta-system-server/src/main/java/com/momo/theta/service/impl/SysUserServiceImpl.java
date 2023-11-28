package com.momo.theta.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.momo.theta.dto.UserAuthInfo;
import com.momo.theta.entity.SysUser;
import com.momo.theta.mapper.SysUserMapper;
import com.momo.theta.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * 用户业务接口
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public UserAuthInfo getUserAuthInfo(String username) {
        return this.baseMapper.getUserAuthInfo(username);
    }
}