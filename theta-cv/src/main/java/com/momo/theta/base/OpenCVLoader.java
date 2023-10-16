package com.momo.theta.base;

import cn.hutool.core.io.IoUtil;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class OpenCVLoader {

  //静态加载动态链接库
  static {
    nu.pattern.OpenCV.loadShared();
  }

  public byte[] getModelPath(String modelPath) {
    InputStream inputStream = OpenCVLoader.class.getResourceAsStream(modelPath);
    log.info("加载模型路径：{}", modelPath);
    return IoUtil.readBytes(inputStream);
  }

}
