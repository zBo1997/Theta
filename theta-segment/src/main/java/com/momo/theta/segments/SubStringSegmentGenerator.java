package com.momo.theta.segments;

import com.momo.theta.api.generator.VariableSegmentGenerator;
import com.momo.theta.config.SegmentConfig;
import java.util.Map;

public class SubStringSegmentGenerator implements VariableSegmentGenerator {

  private String stringField;
  private int start;
  private int end;
  /**
   * 是否左侧补零
   */
  private boolean leftZeroPadding = true;
  /**
   * 流水号片段长度
   */
  private int length = 0;

  @Override
  public String getStringSequence(Map<String, String> args) {
    String string = args.get(stringField);
    String originalSequence = string.substring(start, end);
    String segmentString = originalSequence;
    if (originalSequence.length() < length && leftZeroPadding) {
      int subSeqLength = length - originalSequence.length();
      StringBuilder zeroBuilder = new StringBuilder();
      for (int i = 0; i < subSeqLength; i++) {
        zeroBuilder.append("0");
      }
      segmentString = zeroBuilder.toString().concat(originalSequence);
    }
    return segmentString;
  }

  @Override
  public String getSequence() {
    throw new RuntimeException(
        "HashSegmentGenerator should use getStringSegment with variable args");
  }


  @Override
  public void init(SegmentConfig segmentConfig) throws Exception {
    Map<String, Object> args = segmentConfig.getArgs();
    if (args == null || !args.containsKey("stringField")) {
      throw new RuntimeException("subStringSegment segment need a stringField arg!");
    }
    this.stringField = (String) args.get("stringField");
    if (args.containsKey("length")) {
      this.length = Integer.parseInt((String) args.get("length"));
    }
    if (args.containsKey("leftZeroPadding")) {
      this.leftZeroPadding = Boolean.TRUE.toString()
          .equalsIgnoreCase((String) args.get("leftZeroPadding"));
    }
    if (args.containsKey("start")) {
      this.start = Integer.parseInt((String) args.get("start"));
    }
    if (args.containsKey("end")) {
      this.end = Integer.parseInt((String) args.get("end"));
    }
  }

  @Override
  public String getType() {
    return "subStringSegment";
  }

}
