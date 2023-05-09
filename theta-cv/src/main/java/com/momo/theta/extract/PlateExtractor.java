package com.momo.theta.extract;

import com.momo.theta.domain.ExtParam;
import com.momo.theta.domain.ImageMat;
import com.momo.theta.domain.PlateImage;
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
