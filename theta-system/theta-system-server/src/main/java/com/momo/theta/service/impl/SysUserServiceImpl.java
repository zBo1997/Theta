package com.momo.theta.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.momo.theta.dto.UserAuthInfo;
import com.momo.theta.entity.SysUser;
import com.momo.theta.form.UserForm;
import com.momo.theta.mapper.SysUserMapper;
import com.momo.theta.service.SysUserService;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户业务接口
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserAuthInfo getUserAuthInfo(String username) {
        return this.baseMapper.getUserAuthInfo(username);
    }

    @Override
    public boolean saveUser(UserForm userForm) {
        String username = userForm.getUsername();

        long count = this.count(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));

        if (count >= 1) {
            log.info("此用户已经被注册过：{}", JSONObject.toJSON(userForm));
        }
        SysUser sysUser = BeanUtil.copyProperties(userForm, SysUser.class);
        // 设置默认加密密码
        String defaultEncryptPwd = passwordEncoder.encode("123456");
        sysUser.setPassword(defaultEncryptPwd);

        // 新增用户
        return this.save(sysUser);
    }
}