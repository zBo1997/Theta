package com.momo.theta.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Java Web Token 的相关信息
 *
 * @author zhubo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserInfo implements Serializable {

  /**
   * 账号id
   */
  private Long userId;
  /**
   * 账号
   */
  private String account;
  /**
   * 姓名
   */
  private String name;


}
