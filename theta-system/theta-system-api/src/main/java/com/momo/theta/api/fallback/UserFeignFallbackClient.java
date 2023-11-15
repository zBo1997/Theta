package com.momo.theta.api.fallback;


import com.momo.theta.api.UserFeignClient;
import com.momo.theta.dto.UserAuthInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户调用回调
 *
 */
@Component
@Slf4j
public class UserFeignFallbackClient implements UserFeignClient {

    @Override
    public UserAuthInfo getUserAuthInfo(String username) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return new UserAuthInfo();
    }
}