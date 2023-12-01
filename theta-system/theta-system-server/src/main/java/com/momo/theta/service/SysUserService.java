package com.momo.theta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.momo.theta.dto.UserAuthInfo;
import com.momo.theta.entity.SysUser;
import com.momo.theta.form.UserForm;

/**
 * 用户业务接口
 *
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户名获取认证信息
     *
     * @param username
     * @return 用户
     */
    UserAuthInfo getUserAuthInfo(String username);

    /**
     * 新增用户
     * @param userForm 表达数据
     * @return 是否添加成功
     */
    boolean saveUser(UserForm userForm);
}