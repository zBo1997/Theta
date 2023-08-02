package com.momo.theta.limiter.service;

import com.momo.theta.limiter.enums.LimiterMode;


/**
 * 限流接口
 *
 * @author zhubo
 */
public interface LimiterService {

  /**
   * 阻塞试限流
   *
   * @param mode               限制模式  单机 or 集群
   * @param key                限制key
   * @param rateCount          限制数量
   * @param rateIntervalSecond 限流时间间隔  秒
   */
  void limitNormal(LimiterMode mode, String key, long rateCount, long rateIntervalSecond);

  /**
   * 超时式限流
   *
   * @param mode               限制模式  单机 or 集群
   * @param key                限制key
   * @param rateCount          限制数量
   * @param rateIntervalSecond 限流时间间隔  秒
   * @param timeoutMs          超时时间  毫秒
   */
  void limitTimeOutException(LimiterMode mode, String key, long rateCount, long rateIntervalSecond,
      long timeoutMs);
}
