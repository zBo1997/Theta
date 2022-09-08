package com.momo.theta.sharding.config;

import org.apache.shardingsphere.shardingjdbc.spring.boot.sharding.SpringBootShardingRuleConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 分段号生成：落到哪个数据库的权重设置
 * 配置：
 * <p>
 * 20210908 0000000901 RE 01 002 1 954942
 * 1、默认数据库:order.gen.databaseDefaultValue
 * 2、权重配置:order.gen.databaseWeight
 * 设置规则：{ds1: 3,ds2: 5}
 * 会将ds1、ds2按照权重散落到大小为1000的list中
 * 生成订单时会根据请求流水号hash以1000取模
 * 从而获取到应该落到的数据库
 * 注意：1、配置的数据库名称即ds1必须是数据源配置的，否则此项无效；
 * 例如：配置的{ds1: 3,ds2: 5} 而数据库里配置的是 ds0、ds1 则有效配置为{ds2: 5} 所有数据都会落到ds2库
 * 2、当配置的数据按权重计算后存到list中不足1000是 会将走默认数据库
 * 例如：xxx.hashCode()%1000 = 999 此时list中只有990条数据 那么会映射到默认数据库
 */
@Component
public class DbWeightUtil {

    @Autowired
    private SpringBootShardingRuleConfigurationProperties shardingRuleConfig;

    /**
     * 列表初始化大小
     */
    private final static int MAXIMUM_CAPACITY = 2 << 9;

    /**
     * 列表最大值
     */
    private final int Ma = 1000;

    /**
     * 数据库前缀
     */
    private final static String DB_PREFIX = "ds";

    /**
     * 数据库默认值
     */
    private final static String DB_DEFAULT_VALUE = "01";

    /**
     * 初始化后权重集合
     */
    private ArrayList<String> weightList;

    /**
     * 数据源配置的名称
     */
    private Map<String, List<String>> sharingOrderDbs;

    /**
     * 初始化权重
     *
     * @param dataBaseInfo
     * @param tableInfo
     */
    public List<String> initDataBaseWeight(Map<String, String> dataBaseInfo, Map<String, String> tableInfo) {

        if (CollectionUtils.isEmpty(dataBaseInfo)) {
            return null;
        }
        //只有bean初始化后会调用可以不是线程安全
        weightList = new ArrayList<>(MAXIMUM_CAPACITY);
        //配置数据库名称
        Set<String> dbNames = dataBaseInfo.keySet();
        //权重求和
        int sum = dataBaseInfo.values().stream().mapToInt(Integer::valueOf).sum();

        int num = 0;
        //获取表的数据库权重
        for (Map.Entry<String, String> stringStringEntry : tableInfo.entrySet()) {
            sharingOrderDbs = getSharingOrderDbs(stringStringEntry.getValue());
        }
        for (Map.Entry<String, List<String>> stringListEntry : sharingOrderDbs.entrySet()) {
            int dbWeight = 0;
            //权重配置必须包含数据配置的数据库名称
            List<String> dbsInfo = stringListEntry.getValue();
            for (String dbs : dbsInfo) {
                if (dbNames.contains(dbs)) {
                    dbWeight = Integer.parseInt(dataBaseInfo.get(dbs));
                }
                String name = String.valueOf(++num);
                //根据权重计算分到集合中的数量
                int size = dbWeight * Ma / sum;
                //遍历放到集合
                for (int integer = 0; integer < size; integer++) {
                    weightList.add(name);
                }
            }
        }
        return weightList;
    }

    /**
     * 例如若您的的表明为order 那么请在config中设置好您的表名称
     * 配置数据的开始值 ds$->{1}.order_$->{0..2} start=1 end=2
     * 获取订单配置的数据库列表
     *
     * @return Map<String,List<String>> 其MapKey 为对应其表名
     */
    public static Map<String, List<String>> getSharingOrderDbs(String tableName) {
        //拿到数据库配置的数据源
        String test = "ds$->{1..100}.order_$->{0..255}";
        //String groovyString = shardingRuleConfig.getTables().get(tableName).getActualDataNodes();
        String[] orders = test.split("." + tableName);
        String substring = orders[0].substring(6, orders[0].length() - 1);
        String[] dss = substring.split("\\.");
        int start = Integer.parseInt(dss[0]);
        //如果只配置了一个数据库情况 end = start
        int end = dss.length >= 3 ? Integer.parseInt(dss[2]) : start;
        List<String> list = new ArrayList<String>();
        Map<String, List<String>> data = new HashMap<>();
        for (int i = start; i <= end; i++) {
            list.add(DB_PREFIX + i);
        }
        data.put(tableName, list);
        return data;
    }

    /**
     * 根据权重选择数据库
     *
     * @return
     */
    public Object getDbValue(String innerReqMsgId, String dbDefaultValue) {

        //取模后的数据0-999
        int index = innerReqMsgId.hashCode() % Ma;
        if (index < 0) {
            index = index * -1;
        }
        //集合未设置值调用处会使用默认配置的数据库  
        if (CollectionUtils.isEmpty(weightList)) {
            return this.getDbDefaultValue(dbDefaultValue);
        }
        //集合未满1000调用处会使用默认配置的数据库
        if (index > weightList.size()) {
            return this.getDbDefaultValue(dbDefaultValue);
        }
        return weightList.get(index);
    }

    /**
     * 获取默认值
     *
     * @param dbDefaultValue
     * @return
     */
    private String getDbDefaultValue(String dbDefaultValue) {
        return DB_DEFAULT_VALUE;
    }
}
