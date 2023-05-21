package com.momo.theta.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.momo.theta.entity.ThetaMember;
import com.momo.theta.mapper.ThetaMemberMapper;
import com.momo.theta.service.IThetaMemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-05-16
 */
@Service
public class ThetaMemberServiceImpl extends ServiceImpl<ThetaMemberMapper, ThetaMember> implements
    IThetaMemberService {

  private final static String DEFAULT_PASS_WORD = "123456";

  @Override
  public void saveMember(ThetaMember thetaMemberInfo) {
    thetaMemberInfo.setSalt(RandomUtil.randomString(20));
    if (StringUtils.isBlank(thetaMemberInfo.getPassword())) {
      thetaMemberInfo.setPassword(DEFAULT_PASS_WORD);
    }
    thetaMemberInfo.setPassword(
        SecureUtil.sha256(thetaMemberInfo.getPassword() + thetaMemberInfo.getSalt()));
    thetaMemberInfo.setPasswordErrorNum(0);
    this.save(thetaMemberInfo);
  }
}
