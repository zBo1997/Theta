package com.momo.theta.segment.auto;

import com.momo.theta.segment.api.ThetaSegment;
import com.momo.theta.segment.config.SegmentConfig;
import com.momo.theta.segment.generator.GenerateSegmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;


@Slf4j
@Configuration
@EnableConfigurationProperties(ThetaSegment.class)
@ConditionalOnProperty(name = "available", prefix = "theta.sequence", havingValue = "true")
public class SequenceAutoConfiguration implements ApplicationContextAware {

    public static BeanDefinitionRegistry registry;

    @Resource
    private ThetaSegmentConfig thetaSegmentConfig;

    private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    private ApplicationContext applicationContext;

    /**
     * 接受分页插件额外的属性
     *
     * @return 返回分页插件的配置属性
     */
    @Bean
    @ConfigurationProperties(prefix = "theta.sequence")
    public Properties pageHelperProperties() {
        return new Properties();
    }

    /**
     * 使用当前数据源进行配置，和进行事务管理器{@link DataSourceTransactionManager}
     * @param dataSource
     * @return
     */
    @Bean
    public Object generateSequenceBeans(DataSource dataSource) {
        //获取Spring的事物管理器
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        Properties properties = new Properties();
        properties.putAll(pageHelperProperties());
        log.info("加载流水序列号信息");
        if (thetaSegmentConfig == null || !thetaSegmentConfig.isAvailable()) {
            log.warn("segmentConfig is not found, if you want use segment component, you must make it, now we skip it for you");
            return new Object();
        }
        List<SegmentConfig> segmentConfigs = thetaSegmentConfig.getSequenceConfigs();
        if (segmentConfigs != null) {
            for (SegmentConfig segmentConfig : segmentConfigs) {
                Object sequenceImpl = GenerateSegmentService.getSegment(segmentConfig);
                ThetaSegment thetaSegment = registerSequenceBean(registry, segmentConfig.getId(), sequenceImpl.getClass());
                try {
                    segmentConfig.addInitArg("transactionManager", transactionManager);
                    thetaSegment.init(segmentConfig);
                } catch (Exception e) {
                    throw new RuntimeException("error occurs when create segment: " + segmentConfig, e);
                }
            }
        }
        return new Object();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 动态获取Segment的具体实例
     * @param registry
     * @param name
     * @param beanClass
     * @return
     */
    private ThetaSegment registerSequenceBean(BeanDefinitionRegistry registry, String name, Class<?> beanClass) {
        AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(beanClass);
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
        return (ThetaSegment) applicationContext.getBean(beanName);
    }

}

