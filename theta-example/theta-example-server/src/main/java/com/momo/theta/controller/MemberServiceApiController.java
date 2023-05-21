package com.momo.theta.controller;


import cn.hutool.core.bean.BeanUtil;
import com.momo.theta.Result;
import com.momo.theta.api.MemberServiceApi;
import com.momo.theta.entity.ThetaMember;
import com.momo.theta.form.UserSaveForm;
import com.momo.theta.service.IThetaMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author zhubo
 * @since 2023-05-16
 */
@RestController
public class MemberServiceApiController implements MemberServiceApi {

  private IThetaMemberService thetaMemberService;

  @Autowired
  public void setThetaMemberService(IThetaMemberService thetaMemberService) {
    this.thetaMemberService = thetaMemberService;
  }

  /**
   * 创建会员信息
   *
   * @param userForm 用户信息
   * @return 创建后的用户信息
   */
  @Override
  public Result<?> create(@Validated @RequestBody UserSaveForm userForm) {
    ThetaMember thetaMember = BeanUtil.copyProperties(userForm, ThetaMember.class);
    thetaMemberService.saveMember(thetaMember);
    return Result.success(userForm);
  }


}
