package com.momo.theta.factory;

import com.momo.theta.config.SpringContextHolder;
import com.momo.theta.api.StrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;


public class ThetaServiceFactory {

    private final static Logger logger = LoggerFactory.getLogger(ThetaServiceFactory.class);

    /**
     * 根据类型获取Bean
     * @param type 类型
     * @param classType classType类型
     * @param <T> 指定的泛型
     * @return
     */
    public static <T> T getService(String type, Class<T> classType) {
        logger.debug("获取{}扩展bean", type);
        if (type == null || classType == null) {
            return null;
        }
        T service = null;
        try {
            Map<String, T> services = SpringContextHolder.getApplicationContext().getBeansOfType(classType);
            if (services != null) {
                for (T s : services.values()) {
                    if (s instanceof StrategyService) {
                        StrategyService extendService = (StrategyService) s;
                        if (type.equals(extendService.getType())) {
                            return s;
                        }
                    }
                }
            }
            logger.debug("get service bean by spring:" + type + " for " + classType);
        } catch (Exception e) {
        }
        if (classType.isInterface()) {
            ServiceLoader serviceLoader = ServiceLoader.load(classType);
            Iterator<T> it = serviceLoader.iterator();
            while (it.hasNext()) {
                service = it.next();
                if (service instanceof StrategyService) {
                    String serviceType = ((StrategyService) service).getType();
                    if (serviceType != null && serviceType.equalsIgnoreCase(type)) {
                        logger.debug("get service bean by spi:" + type + " for " + classType);
                        return service;
                    }
                }
            }
        }
        try {
            service = SpringContextHolder.getApplicationContext().getBean(type, classType);
            logger.debug("get service bean by spring:" + type + " for " + classType);
            return service;
        } catch (Exception e) {
        }
        try {
            service = (T) Class.forName(type).newInstance();
            logger.debug("get service bean by newInstance:" + type + " for " + classType);
            return service;
        } catch (Exception e) {
        }
        throw new RuntimeException("cannot find a service bean for '" + type + "' for " + classType);
    }
}
