package com.momo.theta.segments;

import com.momo.theta.api.ThetaSegment;
import com.momo.theta.config.SegmentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RandomStringSegmentGenerator implements ThetaSegment {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 序列哈片段长度
     */
    private int length = 10;

    @Override
    public String getSequence() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long minValue = getMinValue(length);
        long maxValue = getMaxValue(length);
        long segmentValue = random.nextLong(minValue, maxValue);
        String segmentString = String.valueOf(segmentValue);
        if (segmentString.length() != length) {
            throw new RuntimeException("the length of " + segmentString + " is illegal!");
        }
        return segmentString;
    }

    public void setLength(int length) {
        this.length = length;
    }

    /**
     * 获取指定位数的数据最小值
     *
     * @param length
     * @return
     */
    private long getMinValue(int length) {
        long minValue = 1;
        for (int i = 1; i < length; i++) {
            minValue *= 10;
        }
        return minValue;
    }

    /**
     * 获取指定位数的数据最大值
     *
     * @param length
     * @return
     */
    private long getMaxValue(int length) {
        long maxValue = 0;
        long sumValue = 1;
        for (int i = 0; i < length; i++) {
            sumValue *= 10;
            maxValue = sumValue - 1;
        }
        return maxValue;
    }

    @Override
    public void init(SegmentConfig segmentConfig) throws Exception {
        Map<String, Object> args = segmentConfig.getArgs();
        if (args.containsKey("length")) {
            this.length = Integer.parseInt((String) args.get("length"));
        } else {
            logger.info("length is not configured, use default value " + this.length);
        }
    }


    @Override
    public String getType() {
        return "randomStringSegment";
    }
}
