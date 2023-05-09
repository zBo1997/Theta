package com.momo.theta.sharding.util.sharding;

import java.util.Collection;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

/**
 * 添加分库策略, 根据商户号分库
 *
 * @author sunquanhu@scenetec.com
 * @date 2018/9/19
 */
public class PreciseModuloDatabaseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

  @Override
  public String doSharding(Collection<String> databaseNames,
      PreciseShardingValue<String> shardingValue) {
    String name = databaseNames.iterator().next();
    return name;
  }

}
