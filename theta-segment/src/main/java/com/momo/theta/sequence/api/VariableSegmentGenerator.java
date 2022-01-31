package com.momo.theta.sequence.api;

import java.util.Map;


public interface VariableSegmentGenerator extends ThetaSegment {

    String getStringSegment(Map<String, String> args);
}
