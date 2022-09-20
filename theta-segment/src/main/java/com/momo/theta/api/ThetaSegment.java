package com.momo.theta.api;


import com.momo.theta.config.SegmentConfig;

public interface ThetaSegment extends StrategyService {

    void init(SegmentConfig sequenceConfig) throws Exception;

    String getSequence();

}
