package com.momo.theta.factory;

import com.momo.theta.config.SegmentConfig;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置文件相相关实现 tip： theta: sequence: available: true sequenceConfigs: - id: commonSequence type:
 * compositeString segmentConfigs: - id: date type: dateSegment args: {pattern: 'yyyyMMdd'} - id: db
 * type: dbNumberSegment args: {id: 'PAYMENT_A', maxSequenceValue: '9999999999', length: '10'} - id:
 * random type: randomStringSegment args: {length: '6'} #8（8位时间yyyyMMdd）10(顺序号，数字范围百亿)
 * 3（类型）2（分库号）3（分表号）4（应用节点号）6（顺序号，百万）。 - id: paymentOrderSequence type: compositeString
 * segmentConfigs: - id: date type: dateSegment args: {pattern: 'yyyyMMdd'} - id: db type:
 * dbNumberSegment args: {id: 'paymentOrderSequence', maxSequenceValue: '9999999999', length: '10'}
 * - id: type type: fixedStringSegment args: {segmentString: 'P0'} - id: databaseIndex type:
 * hashSegment args: {length: '2', defaultMod: '2', hashField: 'isvId', defaultValueField:
 * 'databaseDefaultValue'} - id: tableIndex type: hashSegment #type: subStringSegment args: {length:
 * '3', defaultMod: '128', hashField: 'innerReqMsgId', startField: 'tableStart'} #args: {length:
 * '3', stringField: 'timestamp', start: '6', end: '8'} - id: nodeNo type: fixedStringSegment args:
 * {propertyName: 'nodeNo', length: '1', segmentString: '1'} - id: random type: randomStringSegment
 * args: {length: '6'} - id: settleOrderIdentitySequence type: compositeString segmentConfigs: - id:
 * date type: dateSegment args: {pattern: 'yyyyMMdd'} - id: variableSegment type:
 * variableSequenceSegment args: {pattern: '#{settleBatch}#{userId}#{batchDate}', upperCase:
 * 'true'}
 */
@ConfigurationProperties(prefix = "theta.sequence")
public class ThetaSegmentConfig {

  /**
   * 默认打开sequence可
   */
  private boolean available = true;

  /**
   * 初始化配置文件为ArrayList
   */
  private List<SegmentConfig> segmentConfigs = new ArrayList<>();

  public List<SegmentConfig> getSequenceConfigs() {
    return segmentConfigs;
  }

  public void setSequenceConfigs(List<SegmentConfig> segmentConfigs) {
    this.segmentConfigs = segmentConfigs;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }
}

