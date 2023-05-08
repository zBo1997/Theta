/**
 *
 */
package com.momo.theta.composite;

import com.momo.theta.api.ThetaSegment;
import com.momo.theta.config.SegmentConfig;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用UUID生成序列号
 * <p> 使用UUID生成序列号 </p>
 */
public class JvmUUIDStringSequenceGenerator implements ThetaSegment {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public String getSequence() {
    return UUID.randomUUID().toString();
  }

  @Override
  public String getType() {
    return "uuid";
  }

  @Override
  public void init(SegmentConfig segmentConfig) {
    logger.info("create uuid sequence");
  }
}
