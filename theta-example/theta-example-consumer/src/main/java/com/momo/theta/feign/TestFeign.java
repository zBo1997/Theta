package com.momo.theta.feign;

import com.momo.theta.api.TestService;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * @author zhubo
 */
@FeignClient(name = "theta-example-server")
public interface TestFeign extends TestService {

}