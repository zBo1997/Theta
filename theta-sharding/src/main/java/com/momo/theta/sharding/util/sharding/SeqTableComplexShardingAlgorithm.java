package com.momo.theta.sharding.util.sharding;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class SeqTableComplexShardingAlgorithm implements ComplexKeysShardingAlgorithm {
    @Override
    public Collection<String> doSharding(Collection tableNames, ComplexKeysShardingValue shardingValues) {
        System.out.println("复合数据表collection:" + tableNames + ",shardingValues:" + shardingValues);
        //复合数据表collection:[rebate_order_item_0, rebate_order_item_1, rebate_order_item_2, rebate_order_item_3],
        // shardingValues:ComplexKeysShardingValue(logicTableName=REBATE_ORDER_ITEM, columnNameAndShardingValuesMap={PLAT_REBATE_ORDER_ITEM_ID=[202012010172088401SO010021490915_1]}, columnNameAndRangeValuesMap={})

        //复合数据表collection:[rebate_order_item_0, rebate_order_item_1, rebate_order_item_2, rebate_order_item_3],
        // shardingValues:ComplexKeysShardingValue(logicTableName=REBATE_ORDER_ITEM, columnNameAndShardingValuesMap={PLAT_REBATE_ORDER_ITEM_ID=[202012010172088401SO010021490915_1], PLAT_REBATE_ORDER_ID=[202012010172088401SO010021490915]}, columnNameAndRangeValuesMap={})
        Map<String, Collection> valuesMap = shardingValues.getColumnNameAndShardingValuesMap();
        String name = null;
        for (String key : valuesMap.keySet()) {
            Collection value = valuesMap.get(key);
            if (!CollectionUtils.isEmpty(value)) {
                String temp = (String) value.iterator().next();
                int idx = Integer.parseInt(temp.substring(22, 25));//截取部分
                name = (String) tableNames.toArray()[idx];
                break;
            }
        }
        if (name == null) {
            System.out.println("复合数据表=============空");
            name = (String) tableNames.toArray()[0];
        }
        System.out.println("复合数据表=============" + name);
        List<String> shardingSuffix = new ArrayList<>();
        shardingSuffix.add(name);
        return shardingSuffix;
    }

    public static void main(String[] args) {
        //01 库 0 表
        int idx = Integer.parseInt("202109010000307901RE010001290370".substring(22, 25));//截取部分
        System.out.println(idx);
    }
}
