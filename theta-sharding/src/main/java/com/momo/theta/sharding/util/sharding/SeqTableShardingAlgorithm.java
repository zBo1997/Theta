package com.momo.theta.sharding.util.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 通过流水号进行分表标识识别，22-24位为分表标识，3位
 *
 * @author ZhuBo
 * @date 2018/12/26
 */
public class SeqTableShardingAlgorithm implements PreciseShardingAlgorithm<String> {
    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<String> shardingValue) {
        String seq = shardingValue.getValue();
        int idx = Integer.parseInt(seq.substring(22, 25));
        if (idx > tableNames.size() - 1) {
            return (String) tableNames.toArray()[0];
        }
        String name = (String) tableNames.toArray()[idx];
        return name;
    }

    public static void main(String[] args) {
        String seq = "2020120 1017208840 1 OR 02 166 1490915";
        int idx = Integer.parseInt(seq.substring(22, 25));
        SeqTableShardingAlgorithm seqTableShardingAlgorithm = new SeqTableShardingAlgorithm();
        List tableNames = seqTableShardingAlgorithm.test();
        if (idx > tableNames.size() - 1) {
            String name = (String) tableNames.toArray()[0];
            System.out.println("111==="+name);
        }
        String name = (String) tableNames.toArray()[idx];
        System.out.println("222==="+name);
    }

    private List test(){
        List<String> list = new ArrayList<>();
        for (int i = 0; i <256; i++) {
            list.add(i+"");
        }
        return list;
    }
}
