package com.momo.theta.segments;

import com.momo.theta.api.ThetaSegment;
import com.momo.theta.config.SegmentConfig;
import com.momo.theta.constants.SegmentConstants;
import com.momo.theta.enums.DateFormatEnum;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 时间分区生成器
 */
public class DateSegmentGenerator implements ThetaSegment {

  /**
   * 日期格式，比如yyyy-MM-dd HH:mm:ss
   */
  private String pattern = DateFormatEnum.DATE_SHORT_DATE_ONLY.getCode();
  /**
   * 流水号片段长度
   */
  private int length;

  /**
   * 指定类型
   *
   * @return
   */
  @Override
  public String getType() {
    return "dateSegment";
  }


  @Override
  public void init(SegmentConfig segmentConfig) throws Exception {
    Map<String, Object> args = segmentConfig.getArgs();
    if (args == null) {
      return;
    }
    if (args.containsKey(SegmentConstants.PATTERN)) {
      this.pattern = (String) args.get("pattern");
    }
    if (args.containsKey(SegmentConstants.LENGTH)) {
      this.length = Integer.parseInt((String) args.get("length"));
    }

  }

  @Override
  public String getSequence() {
    DateFormat dateFormat = new SimpleDateFormat(pattern);
    Date currentDate = Calendar.getInstance().getTime();
    String segmentString = dateFormat.format(currentDate);
    if (length != 0 && (null == segmentString || segmentString.length() != length)) {
      throw new RuntimeException("the length of " + segmentString + " is illegal!");
    }
    return segmentString;
  }
}
