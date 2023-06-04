package com.momo.theta.extract;

import com.momo.theta.model.ExtParam;
import com.momo.theta.model.ImageMat;
import com.momo.theta.model.PlateImage;
import java.util.Map;

public interface PlateExtractor {

  /**
   * 车牌特征提取
   *
   * @param image
   * @param extParam
   * @param params
   * @return
   */
  public PlateImage extract(ImageMat image, ExtParam extParam, Map<String, Object> params);

}
