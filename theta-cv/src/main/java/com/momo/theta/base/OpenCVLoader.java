package com.momo.theta.base;

import java.net.URL;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class OpenCVLoader {

  //静态加载动态链接库
  static {
    nu.pattern.OpenCV.loadShared();
  }

  public String getModelPath(String modelPath) {
    URL resource = OpenCVLoader.class.getClassLoader().getResource("");
    if (resource == null) {
      log.error("加载模型路径：{}，未找到模型路径：{}", modelPath, resource.getPath() + modelPath);
      return "";
    }
    log.info("加载模型路径：{}，获取模型路径为：{}", modelPath, resource.getPath() + modelPath);
    return resource.getPath() + modelPath;
  }

}
