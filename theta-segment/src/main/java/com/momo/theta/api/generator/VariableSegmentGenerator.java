package com.momo.theta.api.generator;

import com.momo.theta.api.ThetaSegment;

import java.util.Map;


public interface VariableSegmentGenerator extends ThetaSegment {

    String getStringSequence(Map<String, String> args);
}
