package com.momo.theta.feign;

import com.momo.theta.api.OcrService;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * @author zhubo
 */
@FeignClient(name = "theta-ocr-server")
public interface OcrFeign extends OcrService {


}