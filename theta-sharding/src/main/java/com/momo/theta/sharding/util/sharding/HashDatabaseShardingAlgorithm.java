package com.momo.theta.sharding.util.sharding;


import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * hash分表
 *
 * @author ZhuBo
 * @date 2018/12/26
 */
public class HashDatabaseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    @Override
    public String doSharding(Collection<String> databaseNames, PreciseShardingValue<String> shardingValue) {
        int hashCode = shardingValue.getValue().hashCode();
        int idx = hashCode % databaseNames.size();
        if (idx < 0) {
            idx = idx * -1;
        }
        String name = (String) databaseNames.toArray()[idx];
        return name;
    }

}
