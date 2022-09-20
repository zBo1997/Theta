package com.momo.theta.composite;


import com.momo.theta.api.ThetaSegment;
import com.momo.theta.api.generator.VariableSegmentGenerator;
import com.momo.theta.config.SegmentConfig;
import com.momo.theta.generator.GenerateSegmentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompositeStringSequenceGenerator implements ThetaSegment {

    private List<ThetaSegment> ThetaSegments = new ArrayList<>();

    /**
     * 通过所有已经注册了Segment使用StringBuilder来生成对应德Sequence
     * @return
     */
    @Override
    public String getSequence() {
        StringBuilder sequenceBuilder = new StringBuilder();
        for (ThetaSegment ThetaSegment : ThetaSegments) {
            String stringSegment = ThetaSegment.getSequence();
            sequenceBuilder.append(stringSegment);
        }
        return sequenceBuilder.toString();
    }

    /**
     * 支持多个可变参数的组合
     * @param argsMap 多个可变参数
     * @return
     */
    public String getStringSequence(Map<String, String> argsMap) {
        StringBuilder sequenceBuilder = new StringBuilder();
        for (ThetaSegment ThetaSegment : ThetaSegments) {
            String stringSegment = null;
            if (ThetaSegment instanceof VariableSegmentGenerator) {
                stringSegment = ((VariableSegmentGenerator) ThetaSegment).getStringSequence(argsMap);
            } else {
                stringSegment = ThetaSegment.getSequence();
            }
            sequenceBuilder.append(stringSegment);
        }
        return sequenceBuilder.toString();
    }

    /**
     * 基于父SegmentNum和可变参数生成序列号
     * @param parentSegmentNum 父Segment
     * @param argsMap 可变参数 可以通过 <p>#{code1}#{code2}#{code3}</p> 的这种方式进行组合
     * @return
     */
    public String getStringSequenceByParentSegment(int parentSegmentNum, Map<String, String> argsMap) {
        StringBuilder sequenceBuilder = new StringBuilder();
        int i = 0;
        for (ThetaSegment ThetaSegment : ThetaSegments) {
            if (++i <= parentSegmentNum) {
                continue;
            }
            String stringSegment = null;
            if (ThetaSegment instanceof VariableSegmentGenerator) {
                stringSegment = ((VariableSegmentGenerator) ThetaSegment).getStringSequence(argsMap);
            } else {
                stringSegment = ThetaSegment.getSequence();
            }
            sequenceBuilder.append(stringSegment);
        }
        return sequenceBuilder.toString();
    }

    /**
     * 根据父Segment生成序列号
     * @param parentSegmentNum 父Segment
     * @return
     */
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

    /**
     * 设置所有的生成Segment
     * @param ThetaSegments
     */
    public void setThetaSegments(List<ThetaSegment> ThetaSegments) {
        this.ThetaSegments = ThetaSegments;
    }

    /**
     * 当前的类型
     * @return
     */
    @Override
    public String getType() {
        return "compositeString";
    }

    /**
     * 初始化组合配置，全放在一个有序的线性表中
     *
     * @param segmentConfig segment配置
     * @throws Exception 异常信息
     */
    @Override
    public void init(SegmentConfig segmentConfig) throws Exception {
        if (segmentConfig.getSegmentConfigs() == null || segmentConfig.getSegmentConfigs().length == 0) {
            throw new RuntimeException("segmentGenerators is need");
        }
        SegmentConfig[] segmentConfigs = segmentConfig.getSegmentConfigs();
        for (SegmentConfig sc : segmentConfigs) {
            ThetaSegment segmentGenerator = (ThetaSegment) GenerateSegmentService.getSegment(sc);
            sc.getInitArgs().putAll(segmentConfig.getInitArgs());
            segmentGenerator.init(sc);
            this.ThetaSegments.add(segmentGenerator);
        }
    }
}
