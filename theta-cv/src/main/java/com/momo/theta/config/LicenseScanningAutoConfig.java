package com.momo.theta.config;

import com.momo.theta.base.PlateDetection;
import com.momo.theta.base.PlateRecognition;
import com.momo.theta.extract.PlateExtractor;
import com.momo.theta.extract.PlateExtractorImpl;
import com.momo.theta.models.TorchPlateDetection;
import com.momo.theta.models.TorchPlateRecognition;
import com.momo.theta.properties.OpenCVProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

/**
 * 车牌扫描配置类
 *
 * @author zhubo
 */
@Slf4j
@AllArgsConstructor
@EnableCaching
@EnableConfigurationProperties(OpenCVProperties.class)
@ConditionalOnProperty(prefix = "opencv.license", name = "enable", havingValue = "true")
public class LicenseScanningAutoConfig {


  private OpenCVProperties openCvProperties;


  /**
   * 获取车牌检查模型
   */
  @Bean(name = "visualPlateDetection")
  public PlateDetection getPlateDetection() {
    return new TorchPlateDetection("theta-cv/src/main/resources/models/plate_detect.onnx",
        openCvProperties.getPlateDetectionThread());
  }

  /**
   * 车牌特征提取服务
   */
  @Bean(name = "visualPlateRecognition")
  public PlateRecognition getPlateRecognition() {
    return new TorchPlateRecognition("theta-cv/src/main/resources/models/plate_rec_color.onnx",
        openCvProperties.getPlateRecognitionThread());
  }


  /**
   * 构建特征提取器
   *
   * @param plateDetection   车牌检测模型
   * @param plateRecognition 车牌识别模型
   */
  @Bean(name = "visualPlateExtractor")
  public PlateExtractor getPlateExtractor(
      @Qualifier("visualPlateDetection") PlateDetection plateDetection,
      @Qualifier("visualPlateRecognition") PlateRecognition plateRecognition
  ) {
    return new PlateExtractorImpl(plateDetection, plateRecognition);
  }

  public static String function(String fileName) {
    return LicenseScanningAutoConfig.class.getClassLoader().getResource(fileName).getFile().substring(1);
  }
}
