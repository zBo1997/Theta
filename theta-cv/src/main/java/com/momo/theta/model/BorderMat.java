package com.momo.theta.model;

import org.opencv.core.Mat;

public class BorderMat {

  /**
   * 图片数据
   */
  public Mat mat;
  /**
   * 图片的缩放比率
   **/
  public float scale;
  /**
   * 往上补充的像素宽度
   **/
  public int top;
  /**
   * 往下补充的像素宽度
   **/
  public int bottom;
  /**
   * 往左补充的像素宽度
   **/
  public int left;
  /**
   * 往右补充的像素宽度
   **/
  public int right;

  public BorderMat(Mat mat, float scale, int top, int bottom, int left, int right) {
    this.mat = mat;
    this.scale = scale;
    this.top = top;
    this.bottom = bottom;
    this.left = left;
    this.right = right;
  }

  /**
   * 释放资源
   */
  public void release() {
    if (this.mat != null) {
      try {
        this.mat.release();
        this.mat = null;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
