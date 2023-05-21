package com.momo.theta.feign;

import com.momo.theta.api.MemberServiceApi;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * @author zhubo
 */
@FeignClient(name = "theta-example-server")
public interface MemberFeignApi extends MemberServiceApi {

}