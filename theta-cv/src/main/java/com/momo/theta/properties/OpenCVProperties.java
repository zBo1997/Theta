package com.momo.theta.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "opencv")
public class OpenCVProperties {

  /**
   * 模型加载线程数量
   */
  private Integer plateDetectionThread = 1;

  /**
   * 模型加载线程数量
   */
  private Integer plateRecognitionThread = 1;

}
