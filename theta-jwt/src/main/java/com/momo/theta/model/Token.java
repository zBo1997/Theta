package com.momo.theta.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhubo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token implements Serializable {

  private static final long serialVersionUID = 8482946147272782305L;

  /**
   * token
   */
  private String token;

  /**
   * 有效期
   */
  private Long expire;

  /**
   * Token到期的时间
   */
  private LocalDateTime expiration;

}
