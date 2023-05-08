package com.momo.theta.config;

import com.momo.theta.factory.SequenceAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThetaSequenceAutoConfig implements BeanDefinitionRegistryPostProcessor {

  private Logger logger = LoggerFactory.getLogger(ThetaSequenceAutoConfig.class);

  /**
   * BFPP的核心，可以修改beanDefinition的值，在bean具体实例化前，修改一次bean的定义信息
   *
   * @param beanFactory
   * @throws BeansException
   */
  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
    logger.info("Invoke Method postProcessBeanFactory");
    // 这里可以设置属性，例如
        /*BeanDefinition bd = beanFactory.getBeanDefinition("thetaConfig");
        MutablePropertyValues mpv =  bd.getPropertyValues();
        mpv.addPropertyValue("driverClassName", "com.mysql.jdbc.Driver");
        mpv.addPropertyValue("url", "jdbc:mysql://localhost:3306/test");
        mpv.addPropertyValue("username", "root");
        mpv.addPropertyValue("password", "123456");*/
  }

  /**
   * 根据目前当前环境设置默认的 beanDefinitionRegistry
   *
   * @param registry
   * @throws BeansException
   */
  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
      throws BeansException {
    SequenceAutoConfiguration.registry = registry;
  }
}
