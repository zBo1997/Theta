package com.momo.theta.sharding.util.sharding;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

/**
 * 复合分片的工具类
 *
 * @author ZhuBo
 * @since 2021/8/31 11:17
 */
public class SeqDatabaseComplexShardingAlgorithm implements ComplexKeysShardingAlgorithm {


  /**
   * @param tableNames
   * @param shardingValues
   * @return
   */
  @Override
  public Collection<String> doSharding(Collection tableNames,
      ComplexKeysShardingValue shardingValues) {
    System.out.println("复合数据库collection:" + tableNames + ",shardingValues:" + shardingValues);
    Map<String, Collection> valuesMap = shardingValues.getColumnNameAndShardingValuesMap();
    String name = null;
    for (String key : valuesMap.keySet()) {
      Collection value = valuesMap.get(key);
      if (!CollectionUtils.isEmpty(value)) {
        String temp = (String) value.iterator().next();
        int idx = Integer.parseInt(temp.substring(20, 22));//截取部分
        name = (String) tableNames.toArray()[idx - 1];
        break;
      }
    }
    //否如果名称为空 默认为  1库
    if (name == null) {
      System.out.println("复合数据表=============空");
      name = (String) tableNames.toArray()[0];
    }
    System.out.println("复合数据表=============" + name);
    List<String> shardingSuffix = new ArrayList<>();
    shardingSuffix.add(name);
    return shardingSuffix;
  }

}
