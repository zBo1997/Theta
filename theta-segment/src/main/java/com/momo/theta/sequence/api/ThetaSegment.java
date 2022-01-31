package com.momo.theta.sequence.api;


import com.momo.theta.core.api.StrategyService;
import com.momo.theta.sequence.config.SegmentConfig;

public interface ThetaSegment extends StrategyService {

    void init(SegmentConfig sequenceConfig) throws Exception;

    String getSequence();

}
