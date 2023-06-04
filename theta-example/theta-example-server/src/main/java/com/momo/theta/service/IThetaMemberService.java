package com.momo.theta.service;

import com.momo.theta.entity.ThetaMember;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author author
 * @since 2023-05-16
 */
public interface IThetaMemberService extends IService<ThetaMember> {

  /**
   * 创建一个用户的示例
   * @param thetaMemberInfo 用户信息
   */
  void saveMember(ThetaMember thetaMemberInfo);

}
