package com.momo.theta.segments;

import com.momo.theta.api.ThetaSegment;
import com.momo.theta.config.SegmentConfig;
import com.momo.theta.constants.SegmentConstants;
import java.util.Map;

public class FixedStringSegmentGenerator implements ThetaSegment {

  /**
   * 固定字符串
   */
  private String segmentString;

  public void setSegmentString(String segmentString) {
    this.segmentString = segmentString;
  }


  @Override
  public String getSequence() {
    return this.segmentString;
  }


  @Override
  public void init(SegmentConfig segmentConfig) throws Exception {
    Map<String, Object> args = segmentConfig.getArgs();
    if (args.containsKey(SegmentConstants.SEGMENT_STRING)) {
      this.segmentString = (String) args.get("segmentString");
    }

  }

  @Override
  public String getType() {
    return "fixedStringSegment";
  }
}
