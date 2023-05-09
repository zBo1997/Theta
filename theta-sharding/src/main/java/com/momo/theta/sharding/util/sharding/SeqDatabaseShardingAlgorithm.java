package com.momo.theta.sharding.util.sharding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

/**
 * 通过流水号进行分库标识识别,20-21位为分库标识，两位
 *
 * @author ZhuBo
 * @date 2018/12/26
 */
public class SeqDatabaseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

  public static void main(String[] args) {
    String seq = "202004260000046120OR020011456204";
    List databaseNames = new ArrayList<>();
    databaseNames.add("ds1");
    databaseNames.add("ds2");
    String name = "";
    int idx = Integer.parseInt(seq.substring(20, 22));
    if (idx > databaseNames.size()) {
      name = (String) databaseNames.toArray()[0];
    } else {
      name = (String) databaseNames.toArray()[idx - 1];
    }
    System.out.println(name);
  }

  @Override
  public String doSharding(Collection<String> databaseNames,
      PreciseShardingValue<String> shardingValue) {
    String seq = shardingValue.getValue();
    int idx = Integer.parseInt(seq.substring(20, 22));
    if (idx > databaseNames.size()) {
      return (String) databaseNames.toArray()[0];
    }
    String name = (String) databaseNames.toArray()[idx - 1];
    return name;
  }
}
