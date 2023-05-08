package com.momo.theta.sharding.util.sharding;


import java.util.Collection;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

/**
 * 按orderDay,yyyyMMdd,每月31日分表
 *
 * @author ZhuBo
 * @date 2018/12/24
 */
public class OrderShardingByDayAlgorithm implements PreciseShardingAlgorithm<String> {

  @Override
  public String doSharding(Collection<String> tableNames,
      PreciseShardingValue<String> shardingValue) {
    int idx = Integer.parseInt(shardingValue.getValue().substring(6));
    for (Object tableName : tableNames.toArray()) {
      String name = (String) tableName;
      if (name.endsWith(String.valueOf("_" + idx))) {
        return name;
      }
    }
    return (String) tableNames.toArray()[0];
  }

}
