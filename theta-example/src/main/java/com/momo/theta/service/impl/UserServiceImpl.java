package com.momo.theta.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.momo.theta.condition.UserCondition;
import com.momo.theta.entity.User;
import com.momo.theta.mapper.UserMapper;
import com.momo.theta.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * User业务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Override
    public IPage<User> query(UserCondition user) {
        log.info("分页入参:{}", JSONObject.toJSONString(user));
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Page<User> page = new Page<>(user.getPageNo(), user.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public User getUser(User user) {
        return null;
    }

    @Override
    public Long getCount() {
        return this.count();
    }
}
