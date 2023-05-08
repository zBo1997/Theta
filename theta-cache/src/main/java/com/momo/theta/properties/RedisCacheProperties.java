package com.momo.theta.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.redis.cache")
public class RedisCacheProperties {

  private String prefix = "";
  private long ttl = 30L * 24 * 60;

}
