package com.momo.theta.feign;

import com.momo.theta.api.UserFeignClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "theta-system")
public interface SystemUserFeignApi extends UserFeignClient {


}
