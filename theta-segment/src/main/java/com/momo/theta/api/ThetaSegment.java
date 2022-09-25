package com.momo.theta.api;


import com.momo.theta.config.SegmentConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface ThetaSegment extends StrategyService {

    Map<String, ThetaSegment> segmentMap = new ConcurrentHashMap<>();

    void init(SegmentConfig sequenceConfig) throws Exception;

    String getSequence();

}
