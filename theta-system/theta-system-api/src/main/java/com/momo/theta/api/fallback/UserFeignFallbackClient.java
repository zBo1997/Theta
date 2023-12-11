package com.momo.theta.api.fallback;

import com.momo.theta.api.UserFeignClient;
import com.momo.theta.dto.UserAuthInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户调用回调
 */
@Component
@Slf4j
public class UserFeignFallbackClient implements FallbackFactory<UserFeignClient> {

    @Override
    public UserFeignClient create(Throwable cause) {
        return new UserFeignClient() {
            @Override
            public UserAuthInfo getUserAuthInfo(String username) {
                UserAuthInfo userAuthInfo = new UserAuthInfo();
                userAuthInfo.setUserId(1L);
                userAuthInfo.setUsername("admin");
                userAuthInfo.setStatus(1);
                userAuthInfo.setPassword("$2a$10$8/8PxGHA.30EeWg8x4/4BuWuCUJubFbGJXyUYRs7RaJEdVvEMRbWe");
                return userAuthInfo;
            }
        };
    }
}