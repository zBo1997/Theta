package com.momo.theta.biz;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.momo.theta.Cache;
import com.momo.theta.condition.UserCondition;
import com.momo.theta.dto.UserInfoDTO;
import com.momo.theta.entity.User;
import com.momo.theta.form.UserForm;
import com.momo.theta.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserBusiness {

  @Autowired
  private IUserService userInfoService;

  @Autowired
  private Cache cache;

  public UserBusiness(IUserService userInfoService, Cache cache) {
    this.userInfoService = userInfoService;
    this.cache = cache;
  }

  public UserInfoDTO update(UserForm userForm) {
    return cache.acquireForRedissonCallable("USER_UPDATE_LOCK", 1000L, () -> {
      UserInfoDTO userInfoDTO = new UserInfoDTO();
      LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
      wrapper.set(User::getUserName, userForm.getUserName());
      wrapper.eq(User::getId, "1");
      userInfoService.update(wrapper);
      userInfoDTO.setUserName(userForm.getUserName());
      return userInfoDTO;
    }, 6000L);

  }

  public void createUser(UserForm userForm) {
    userInfoService.createUser(userForm);

  }

  public User getById(String userId) {
    return userInfoService.getById(userId);
  }

  public IPage<User> query(UserCondition userCondition) {
    return userInfoService.query(userCondition);
  }
}
