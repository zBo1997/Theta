package com.momo.theta.base;

public abstract class OpenCVLoader {

  //静态加载动态链接库
  static {
    nu.pattern.OpenCV.loadShared();
  }

}
