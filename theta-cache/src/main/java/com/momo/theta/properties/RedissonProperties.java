package com.momo.theta.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.redis.redisson")
public class RedissonProperties {

  private boolean enable;
}
