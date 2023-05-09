package com.momo.theta.sharding.util.sharding;


import java.util.Collection;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

/**
 * hash分表
 *
 * @author ZhuBo
 * @date 2018/12/26
 */
public class HashTableShardingAlgorithm implements PreciseShardingAlgorithm<String> {

  public static void main(String[] args) {
    int hashCode = "M00008024620211108145754882576967680002009".hashCode();
    int idx = hashCode % 128;
    if (idx < 0) {
      idx = idx * -1;
    }

    System.out.println(idx);
  }

  @Override
  public String doSharding(Collection<String> tableNames,
      PreciseShardingValue<String> shardingValue) {
    int hashCode = shardingValue.getValue().hashCode();
    int idx = hashCode % tableNames.size();
    if (idx < 0) {
      idx = idx * -1;
    }
    String name = (String) tableNames.toArray()[idx];
    return name;
  }
}
