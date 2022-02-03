package com.momo.theta.segment.api;


import com.momo.theta.core.api.StrategyService;
import com.momo.theta.segment.config.SegmentConfig;

public interface ThetaSegment extends StrategyService {

    void init(SegmentConfig sequenceConfig) throws Exception;

    String getSequence();

}
