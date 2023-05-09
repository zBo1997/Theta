package com.momo.theta.service;

import com.momo.theta.domain.PlateInfoRepVo;
import com.momo.theta.domain.PlateInfoReqVo;
import java.util.List;

public interface PlateService {

  /**
   * 识别车牌信息
   *
   * @param plateInfoReq
   * @return
   */
  public List<PlateInfoRepVo> recognition(PlateInfoReqVo plateInfoReq);

}
