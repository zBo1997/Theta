package com.momo.theta.base;

import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;

@Slf4j
public abstract class OpenCVLoader {

  //静态加载动态链接库
  static {
    nu.pattern.OpenCV.loadShared();
  }

  public byte[] getModelPath(String modelPath) {
    try {
      InputStream inputStream = OpenCVLoader.class.getResourceAsStream(modelPath);
      log.info("加载模型路径：{}", modelPath);
      return IOUtils.toByteArray(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
