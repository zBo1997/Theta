package com.momo.theta.sharding.config;

import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 封库分表基础信息
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "sharding.config")
@Slf4j
public class ShardingGenConfig implements InitializingBean {

  /**
   * #默认数据库 ds1/ds2
   */
  private String databaseDefaultValue;

  /**
   * #配置数据库权重 比例=各自配置/总和
   */
  private Map<String, String> databaseWeight;

  /**
   * 分库分表名称
   */
  private Map<String, String> tableInfo;


  @Autowired
  private DbWeightUtil dbWeightUtil;

  @Override
  public void afterPropertiesSet() {
    log.info("刷新配置:{}", this);
    try {
      dbWeightUtil.initDataBaseWeight(databaseWeight, tableInfo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

}
