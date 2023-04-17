package com.momo.theta.config;


import com.momo.theta.DefaultRedisCache;
import com.momo.theta.properties.RedissonProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Slf4j
@AllArgsConstructor
@EnableConfigurationProperties(RedissonProperties.class)
@ConditionalOnProperty(prefix = "spring.redis.redisson", name = "enable", havingValue = "true")
public class RedissonConfig {

    private RedisProperties redisProperties;

    /**
     * 集群模式redis redisson 初始化配置
     *
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnProperty(prefix = "spring.redis", name = "multi-nodes", havingValue = "true")
    public RedissonClient redissonClusterConfiguration() {
        List<String> nodes = redisProperties.getCluster().getNodes();
        String[] clusterNodes = new String[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            clusterNodes[i] = RedisLettuceConfig.ACCESS_PROTOCOL + nodes.get(i);
        }
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers();
        clusterServersConfig.setCheckSlotsCoverage(false);
        clusterServersConfig.addNodeAddress(clusterNodes);
        if (StringUtils.hasText(redisProperties.getPassword())) {
            clusterServersConfig.setPassword(redisProperties.getPassword());
        }
        return Redisson.create(config);

    }

    /**
     * 单节点redis redisson 初始化配置
     *
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnProperty(prefix = "spring.redis", name = "multi-nodes", havingValue = "false", matchIfMissing = true)
    public RedissonClient redissonConfiguration() {
        Config config = new Config();
        String host = redisProperties.getHost();
        if (!host.startsWith(RedisLettuceConfig.ACCESS_PROTOCOL)) {
            host = RedisLettuceConfig.ACCESS_PROTOCOL + host;
        }
        host = host + ":" + redisProperties.getPort();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(host);
        if (StringUtils.hasText(redisProperties.getPassword())) {
            singleServerConfig.setPassword(redisProperties.getPassword());
        }
        return Redisson.create(config);

    }


    /**
     * 分布式锁
     *
     * @param redisTemplate redis
     * @return 分布式锁
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.redis", name = "multi-nodes", havingValue = "true")
    public DefaultRedisCache defaultRedisCache(RedisTemplate<String, Object> redisTemplate, RedissonClient redissonClient) {
        return new DefaultRedisCache(redisTemplate, redissonClient, true);
    }

    /**
     * 分布式锁
     *
     * @param redisTemplate redis
     * @return 分布式锁
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.redis", name = "multi-nodes", havingValue = "false", matchIfMissing = true)
    public DefaultRedisCache defaultClusterRedisCache(RedisTemplate<String, Object> redisTemplate, RedissonClient redissonClient) {
        return new DefaultRedisCache(redisTemplate, redissonClient, false);
    }
}
