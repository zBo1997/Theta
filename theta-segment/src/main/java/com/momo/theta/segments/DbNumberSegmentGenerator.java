package com.momo.theta.segments;

import com.momo.theta.config.SegmentConfig;
import com.momo.theta.count.DbNumberSequenceGenerator;
import com.momo.theta.api.generator.SegmentGenerator;

public class DbNumberSegmentGenerator extends DbNumberSequenceGenerator implements SegmentGenerator {

    /**
     * 是否左侧补零
     */
    private boolean leftZeroPadding = true;

    @Override
    public String getSequence() {
        String originalSequence = super.getSequence();
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

    public void setLength(int length) {
        this.length = length;
    }

    public void setLeftZeroPadding(boolean leftZeroPadding) {
        this.leftZeroPadding = leftZeroPadding;
    }

    @Override
    public void init(SegmentConfig segmentConfig) throws Exception {
        super.init(segmentConfig);
    }

    @Override
    public String getType() {
        return "dbNumberSegment";
    }
}
