package com.momo.theta.auto;

import com.momo.theta.factory.RedisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheAutoConfig implements BeanDefinitionRegistryPostProcessor {
    private Logger logger = LoggerFactory.getLogger(CacheAutoConfig.class);

    /**
     * BFPP的核心，可以修改beanDefinition的值，在bean具体实例化前，修改一次bean的定义信息
     *
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        logger.info("Invoke Method postProcessBeanFactory");
    }

    /**
     * 根据目前当前环境设置默认的 beanDefinitionRegistry
     *
     * @param registry
     * @throws BeansException
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        RedisAutoConfiguration.registry = registry;
    }
}
