/**
 *
 */
package com.momo.theta.segment.composite;

import com.momo.theta.segment.api.ThetaSegment;
import com.momo.theta.segment.config.SegmentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

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
