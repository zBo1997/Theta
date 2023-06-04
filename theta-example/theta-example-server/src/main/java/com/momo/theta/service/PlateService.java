package com.momo.theta.service;

import com.momo.theta.model.PlateInfoRepVO;
import com.momo.theta.model.PlateInfoReqVO;
import java.util.List;

public interface PlateService {

  /**
   * 识别车牌信息
   *
   * @param plateInfoReq
   * @return
   */
  public List<PlateInfoRepVO> recognition(PlateInfoReqVO plateInfoReq);

}
