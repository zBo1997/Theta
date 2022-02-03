package com.momo.theta.segment.composite;


import com.momo.theta.segment.api.ThetaSegment;
import com.momo.theta.segment.api.generator.VariableSegmentGenerator;
import com.momo.theta.segment.config.SegmentConfig;
import com.momo.theta.segment.generator.GenerateSegmentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompositeStringSequenceGenerator implements ThetaSegment {

    private List<ThetaSegment> ThetaSegments = new ArrayList<>();

    @Override
    public String getSequence() {
        StringBuilder sequenceBuilder = new StringBuilder();
        for (ThetaSegment ThetaSegment : ThetaSegments) {
            String stringSegment = ThetaSegment.getSequence();
            sequenceBuilder.append(stringSegment);
        }
        return sequenceBuilder.toString();
    }

    public String getStringSequence(Map<String, String> argsMap) {
        StringBuilder sequenceBuilder = new StringBuilder();
        for (ThetaSegment ThetaSegment : ThetaSegments) {
            String stringSegment = null;
            if (ThetaSegment instanceof VariableSegmentGenerator) {
                stringSegment = ((VariableSegmentGenerator) ThetaSegment).getStringSegment(argsMap);
            } else {
                stringSegment = ThetaSegment.getSequence();
            }
            sequenceBuilder.append(stringSegment);
        }
        return sequenceBuilder.toString();
    }

    public String getStringSequenceByParentSegment(int parentSegmentNum, Map<String, String> argsMap) {
        StringBuilder sequenceBuilder = new StringBuilder();
        int i = 0;
        for (ThetaSegment ThetaSegment : ThetaSegments) {
            if (++i <= parentSegmentNum) {
                continue;
            }
            String stringSegment = null;
            if (ThetaSegment instanceof VariableSegmentGenerator) {
                stringSegment = ((VariableSegmentGenerator) ThetaSegment).getStringSegment(argsMap);
            } else {
                stringSegment = ThetaSegment.getSequence();
            }
            sequenceBuilder.append(stringSegment);
        }
        return sequenceBuilder.toString();
    }

    public String getStringSequenceByParentSegment(int parentSegmentNum) {
        StringBuilder sequenceBuilder = new StringBuilder();
        int i = 0;
        for (ThetaSegment ThetaSegment : ThetaSegments) {
            if (++i <= parentSegmentNum) {
                continue;
            }
            String stringSegment = ThetaSegment.getSequence();
            sequenceBuilder.append(stringSegment);
        }
        return sequenceBuilder.toString();
    }

    public void setThetaSegments(List<ThetaSegment> ThetaSegments) {
        this.ThetaSegments = ThetaSegments;
    }

    @Override
    public String getType() {
        return "compositeString";
    }

    /**
     * 初始化组合配置，全放在一个有序的线性表中
     *
     * @param segmentConfig
     * @throws Exception
     */
    @Override
    public void init(SegmentConfig segmentConfig) throws Exception {
        if (segmentConfig.getSegmentConfigs() == null || segmentConfig.getSegmentConfigs().length == 0) {
            throw new RuntimeException("segmentGenerators is need");
        }
        SegmentConfig[] segmentConfigs = segmentConfig.getSegmentConfigs();
        for (SegmentConfig sc : segmentConfigs) {
            ThetaSegment segmentGenerator = (ThetaSegment) GenerateSegmentService.getSegment(sc);
            segmentConfig.getInitArgs().putAll(sc.getInitArgs());
            segmentGenerator.init(sc);
            this.ThetaSegments.add(segmentGenerator);
        }
    }
}
