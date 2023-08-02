package com.momo.theta.limiter.service.impl;

import com.momo.theta.constants.Constants;
import com.momo.theta.exception.NotSupportException;
import com.momo.theta.exception.TimeoutException;
import com.momo.theta.limiter.enums.LimiterMode;
import com.momo.theta.limiter.service.LimiterService;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

/**
 * 限流器
 *
 * @author zhubo
 */
@Service
public class LimiterServiceImpl implements LimiterService {

  @Resource
  private RedissonClient redissonClient;

  @Override
  public void limitNormal(LimiterMode mode, String key, long rateCount, long rateIntervalSecond) {
    limitTimeOutException(mode, key, rateCount, rateIntervalSecond, -1);
  }

  @Override
  public void limitTimeOutException(LimiterMode mode, String key, long rateCount,
      long rateIntervalSecond, long timeoutMs) {

    if (null == redissonClient) {
      throw new NotSupportException(getClass().getSimpleName(), "limitTimeOutException",
          "This method is not supported , please inject RedissonClient");
    }
    String limiterKey = mode.name() + "_" + key + "_" + rateCount + "_" + rateIntervalSecond;

    RRateLimiter rateLimiter = redissonClient.getRateLimiter(limiterKey);

    if (RateType.OVERALL.name().equals(mode.name())) {
      rateLimiter.trySetRate(RateType.OVERALL, rateCount, rateIntervalSecond,
          RateIntervalUnit.SECONDS);
    } else {
      rateLimiter.trySetRate(RateType.PER_CLIENT, rateCount, rateIntervalSecond,
          RateIntervalUnit.SECONDS);
    }

    if (timeoutMs <= 0) {
      rateLimiter.acquire();
      return;
    }

    boolean result = rateLimiter.tryAcquire(timeoutMs, TimeUnit.MILLISECONDS);
    if (!result) {
      throw new TimeoutException(
          Constants.PROJECT_PREFIX, "limiter",
          "limiter try acquire key:[" + key + "] timeout [" + timeoutMs + "]");
    }
  }
}
