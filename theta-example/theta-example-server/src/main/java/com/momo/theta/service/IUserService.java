package com.momo.theta.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.momo.theta.condition.UserCondition;
import com.momo.theta.dto.UserInfoDTO;
import com.momo.theta.entity.User;
import com.momo.theta.form.UserForm;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * User服务
 * </p>
 *
 * @author ZhuBo
 * @since 2021-09-07
 */
public interface IUserService extends IService<User> {

    IPage<User> query(UserCondition user);

    User getUser(User user);

    Long getCount();

    void createUser(UserForm userForm);

    UserInfoDTO update(UserForm userForm);

}
