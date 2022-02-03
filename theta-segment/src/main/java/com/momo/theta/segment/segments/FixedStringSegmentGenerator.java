package com.momo.theta.segment.segments;

import com.momo.theta.commom.constants.SegmentConstants;
import com.momo.theta.segment.api.ThetaSegment;
import com.momo.theta.segment.config.SegmentConfig;

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
