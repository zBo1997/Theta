package com.momo.theta.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(prefix = "spring.redis", name = "driver", havingValue = "lettuce", matchIfMissing = true)
public class RedisLettuceConfig {

    public static final String ACCESS_PROTOCOL = "redis://";

    private RedisProperties redisProperties;

    private GenericObjectPoolConfig<?> genericObjectPoolConfig(RedisProperties.Pool properties) {
        GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(properties.getMaxActive());
        config.setMaxIdle(properties.getMaxIdle());
        config.setMinIdle(properties.getMinIdle());
        if (properties.getTimeBetweenEvictionRuns() != null) {
            config.setTimeBetweenEvictionRuns(properties.getTimeBetweenEvictionRuns());
        }
        if (properties.getMaxWait() != null) {
            config.setMaxWait(properties.getMaxWait());
        }
        return config;
    }

    /**
     * redis cluster模式配置，redisConnectionFactory 初始化
     *
     * @return
     */
    @Bean(value = "redisConnectionFactory", destroyMethod = "destroy")
    @ConditionalOnProperty(prefix = "spring.redis", name = "multi-nodes", havingValue = "true")
    public LettuceConnectionFactory lettuceClusterConnFactory() {
        log.info("loading cluster redis driver:{},nodes:{}", "lettuce", redisProperties.getCluster().getNodes());
        //开启 自适应集群拓扑刷新和周期拓扑刷新
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                // 开启全部自适应刷新
                .enableAllAdaptiveRefreshTriggers()
                // 自适应刷新超时时间(默认30秒)
                .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(3))
                // 开周期刷新
                .enablePeriodicRefresh(Duration.ofSeconds(3))
                .build();

        ClientOptions clientOptions = ClusterClientOptions.builder()
                .topologyRefreshOptions(clusterTopologyRefreshOptions)
                .build();

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig(redisProperties.getLettuce().getPool()))
                .readFrom(ReadFrom.MASTER_PREFERRED)
                .clientOptions(clientOptions)
                .commandTimeout(redisProperties.getTimeout())
                .build();

        Set<RedisNode> nodes = new HashSet<>();
        List<String> clusterNodes = redisProperties.getCluster().getNodes();
        clusterNodes.forEach(address -> nodes.add(new RedisNode(address.split(":")[0].trim(), Integer.valueOf(address.split(":")[1]))));
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
        clusterConfiguration.setClusterNodes(nodes);
        clusterConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        clusterConfiguration.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(clusterConfiguration, clientConfig);
        lettuceConnectionFactory.setShareNativeConnection(false);
        lettuceConnectionFactory.resetConnection();
        return lettuceConnectionFactory;
    }

    /**
     * redis 单节点模式配置，redisConnectionFactory 初始化
     *
     * @return
     */
    @Bean(value = "redisConnectionFactory", destroyMethod = "destroy")
    @ConditionalOnProperty(prefix = "spring.redis", name = "multi-nodes", havingValue = "false", matchIfMissing = true)
    public LettuceConnectionFactory redisStandaloneConnectionFactory() {
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
        String host = redisProperties.getHost().replaceAll(RedisLettuceConfig.ACCESS_PROTOCOL, "");
        standaloneConfiguration.setHostName(host);
        standaloneConfiguration.setPort(redisProperties.getPort());
        standaloneConfiguration.setPassword(redisProperties.getPassword());
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig(redisProperties.getLettuce().getPool()))
                .build();

        return new LettuceConnectionFactory(standaloneConfiguration, clientConfig);
    }

    /**
     * redis 序列化方式初始化配置
     *
     * @param factory
     * @param <T>
     * @return
     */
    @Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;

    }


}
