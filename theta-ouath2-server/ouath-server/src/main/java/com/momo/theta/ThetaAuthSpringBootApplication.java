package com.momo.theta;


import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableDiscoveryClient
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableCaching
@SpringCloudApplication
@EnableFeignClients(AppConstant.BASE_PACKAGES)
@EnableAsync
public class ThetaAuthSpringBootApplication {


}
