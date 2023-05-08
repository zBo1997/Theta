package com.momo.theta.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlateImage implements Serializable {

  /**
   * 图像数据
   **/
  public String imageBase64;
  /**
   * 车牌解析数据
   **/
  public List<PlateInfo> PlateInfos;

  /**
   * 构建函数
   *
   * @param imageBase64 图像数据
   * @param PlateInfos  车牌解析数据
   * @return
   */
  private PlateImage(String imageBase64, List<PlateInfo> PlateInfos) {
    this.imageBase64 = imageBase64;
    this.PlateInfos = PlateInfos;
  }

  /**
   * 构建对象
   *
   * @param imageBase64 图像数据
   * @param PlateInfos  车牌解析数据
   * @return
   */
  public static PlateImage build(String imageBase64, List<PlateInfo> PlateInfos) {
    if (PlateInfos == null) {
      PlateInfos = new ArrayList<>();
    }
    return new PlateImage(imageBase64, PlateInfos);
  }

  /**
   * 图像数据
   *
   * @return
   */
  public String imageBase64() {
    return this.imageBase64;
  }

  /**
   * 获取图像数据
   *
   * @return
   */
  public ImageMat imageMat() {
    return ImageMat.fromBase64(this.imageBase64);
  }

  /**
   * 获取车牌解析数据
   *
   * @return
   */
  public List<PlateInfo> PlateInfos() {
    return this.PlateInfos;
  }

}
