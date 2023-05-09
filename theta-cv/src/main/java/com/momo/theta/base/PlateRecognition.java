package com.momo.theta.base;

import com.momo.theta.domain.ImageMat;
import com.momo.theta.domain.PlateInfo.ParseInfo;
import java.util.Map;

public interface PlateRecognition {

  ParseInfo inference(ImageMat image, Boolean single, Map<String, Object> params);
}
