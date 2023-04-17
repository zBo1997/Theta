package com.momo.theta.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
@AllArgsConstructor
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(prefix = "spring.redis", name = "driver", havingValue = "jedis")
public class RedisJedisConfig {

    private RedisProperties redisProperties;

    @Bean
    @ConditionalOnProperty(prefix = "spring.redis", name = "multi-nodes", havingValue = "true")
    public JedisConnectionFactory redisClusterConnectionFactory() {
        log.info("加载 cluster redis >>>>>> driver:{},nodes:{}", "jedis",redisProperties.getCluster().getNodes());
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        RedisClusterConfiguration configuration = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
        configuration.setPassword(redisProperties.getPassword());
        configuration.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());
        return new JedisConnectionFactory(configuration, jedisPoolConfig);
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.redis", name = "multi-nodes", havingValue = "false", matchIfMissing = true)
    public JedisConnectionFactory redisStandaloneConnectionFactory() {
        log.info("加载 standalone redis >>>>>> driver:{},host:{} port:{} ", "jedis",redisProperties.getHost(), redisProperties.getPort());
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPassword(redisProperties.getPassword());
        configuration.setPort(redisProperties.getPort());
        return new JedisConnectionFactory(configuration);
    }


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



