package com.momo.theta.config;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OrtSession.SessionOptions;
import cn.hutool.core.io.FileUtil;
import com.momo.theta.base.OpenCVLoader;
import com.momo.theta.base.PlateDetection;
import com.momo.theta.base.PlateRecognition;
import com.momo.theta.extract.PlateExtractor;
import com.momo.theta.extract.PlateExtractorImpl;
import com.momo.theta.models.TorchPlateDetection;
import com.momo.theta.models.TorchPlateRecognition;
import com.momo.theta.properties.OpenCVProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;


@Slf4j
@AllArgsConstructor
@EnableCaching
@EnableConfigurationProperties(OpenCVProperties.class)
@ConditionalOnProperty(prefix = "opencv.license", name = "enable", havingValue = "true")
public class LicenseScanningAutoConfig extends OpenCVLoader {


  private OpenCVProperties openCvProperties;

  @Bean(name = "currentEnvironment")
  public OrtEnvironment getCurrentEnvironment() {
    return OrtEnvironment.getEnvironment();
  }

  @Bean(name = "currentSessionOptions")
  public SessionOptions getCurrentSessionOptions() {
    return new OrtSession.SessionOptions();
  }

  @Bean(name = "visualPlateDetection")
  public PlateDetection getPlateDetection(
      @Qualifier("currentEnvironment") OrtEnvironment currentEnvironment,
      @Qualifier("currentSessionOptions") SessionOptions sessionOptions) {
    return new TorchPlateDetection(super.getModelPath(openCvProperties.getDetectionModelPath()),
        openCvProperties.getPlateDetectionThread(), currentEnvironment,
        sessionOptions);
  }

  @Bean(name = "visualPlateRecognition")
  public PlateRecognition getPlateRecognition(
      @Qualifier("currentEnvironment") OrtEnvironment currentEnvironment,
      @Qualifier("currentSessionOptions") SessionOptions sessionOptions) {
    return new TorchPlateRecognition(super.getModelPath(openCvProperties.getRecognitionModelPath()),
        openCvProperties.getPlateRecognitionThread(), currentEnvironment,
        sessionOptions);
  }


  @Bean(name = "visualPlateExtractor")
  public PlateExtractor getPlateExtractor(
      @Qualifier("visualPlateDetection") PlateDetection plateDetection,
      @Qualifier("visualPlateRecognition") PlateRecognition plateRecognition
  ) {
    return new PlateExtractorImpl(plateDetection, plateRecognition);
  }

}
