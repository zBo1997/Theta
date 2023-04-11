package com.momo.theta.factory;

import com.momo.theta.Cache;
import com.momo.theta.DefaultRedisCache;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;


@Slf4j
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(prefix = "spring.redis", name = "switch", havingValue = "true")
public class RedisAutoConfiguration implements ApplicationContextAware {

    public static BeanDefinitionRegistry registry;

    private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    private ApplicationContext applicationContext;

    public RedisAutoConfiguration() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 初始化默认的redis配置
     */
    @PostConstruct
    public void initCacheService() {
        this.registerCacheBean(registry, null, DefaultRedisCache.class);
    }


    /**
     * 自动装配DefaultRedisCache
     *
     * @param registry
     * @param name
     * @param beanClass
     * @return
     */
    private Cache registerCacheBean(BeanDefinitionRegistry registry, String name, Class<?> beanClass) {
        AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(beanClass);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        // 由于会出现两个redisTemplate
        propertyValues.add("redisTemplate",applicationContext.getBean("redisTemplate",RedisTemplate.class));
        propertyValues.add("redissonClient",applicationContext.getBean(RedissonClient.class));
        abd.setPropertyValues(propertyValues);
        //Bean定义信息的元数据解析器
        ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(abd);
        //获取Scope的注解内容
        abd.setScope(scopeMetadata.getScopeName());
        // 可以自动生成name
        String beanName = (name != null ? name : this.beanNameGenerator.generateBeanName(abd, registry));
        //检查是否有特殊注解：@Lazy、@Primary、@DependsOn......等注解
        AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
        //创建BeanDefinitionHolder的持有者，并且注册对应的bean定义信息
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
        return (Cache) applicationContext.getBean(beanName);
    }

}

