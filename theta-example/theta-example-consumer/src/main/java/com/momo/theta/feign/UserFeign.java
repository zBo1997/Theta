package com.momo.theta.feign;

import com.momo.theta.api.UserInfoService;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * @author zhubo
 */
@FeignClient(name = "theta-example-server")
public interface UserFeign extends UserInfoService {

}