package com.momo.theta.base;

import com.momo.theta.model.ImageMat;
import com.momo.theta.model.PlateInfo;
import java.util.List;
import java.util.Map;

public interface PlateDetection {


  /**
   * 获取车牌信息
   *
   * @param image   图像信息
   * @param scoreTh 车牌分数阈值
   * @param iouTh   车牌iou阈值
   * @param params  参数信息
   * @return
   */
  List<PlateInfo> inference(ImageMat image, float scoreTh, float iouTh, Map<String, Object> params);


}
