package com.momo.theta.segment.api.generator;

import com.momo.theta.segment.api.ThetaSegment;

import java.util.Map;


public interface VariableSegmentGenerator extends ThetaSegment {

    String getStringSegment(Map<String, String> args);
}
