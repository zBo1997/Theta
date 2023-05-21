package com.momo.theta.api;

import com.momo.theta.Result;
import com.momo.theta.form.UserSaveForm;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author ZhuBo
 * @Description 会员创建接口
 * @Date 2023/04/12
 * @ClassName MemberService
 */

public interface MemberServiceApi {


  /**
   * 创建用户
   *
   * @param userForm
   * @return
   */
  @PostMapping("api/member/doCreate")
  Result<?> create(@Validated @RequestBody UserSaveForm userForm);


}
