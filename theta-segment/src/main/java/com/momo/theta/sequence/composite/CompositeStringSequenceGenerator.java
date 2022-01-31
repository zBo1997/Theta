//package com.momo.theta.sequence.composite;
//
//
//import com.momo.theta.sequence.api.ThetaSegment;
//import com.momo.theta.sequence.config.SegmentConfig;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class CompositeStringSequenceGenerator implements ThetaSegment {
//
//    private List<ThetaSegment> ThetaSegments = new ArrayList<>();
//
//    @Override
//    public String getSequence() {
//        StringBuilder sequenceBuilder = new StringBuilder();
//        for (ThetaSegment ThetaSegment : ThetaSegments) {
//            String stringSegment = ThetaSegment.getSequence();
//            sequenceBuilder.append(stringSegment);
//        }
//        return sequenceBuilder.toString();
//    }
//
//    public String getStringSequence(Map<String, String> argsMap) {
//        StringBuilder sequenceBuilder = new StringBuilder();
//        for (ThetaSegment ThetaSegment : ThetaSegments) {
//            String stringSegment = null;
//            if (ThetaSegment instanceof VariableThetaSegment) {
//                stringSegment = ((VariableThetaSegment) ThetaSegment).getStringSegment(argsMap);
//            } else {
//                stringSegment = ThetaSegment.getSequence();
//            }
//            sequenceBuilder.append(stringSegment);
//        }
//        return sequenceBuilder.toString();
//    }
//
//    public String getStringSequenceByParentSegment(int parentSegmentNum, Map<String, String> argsMap) {
//        StringBuilder sequenceBuilder = new StringBuilder();
//        int i = 0;
//        for (ThetaSegment ThetaSegment : ThetaSegments) {
//            if (++i <= parentSegmentNum) {
//                continue;
//            }
//            String stringSegment = null;
//            if (ThetaSegment instanceof VariableThetaSegment) {
//                stringSegment = ((VariableThetaSegment) ThetaSegment).getStringSegment(argsMap);
//            } else {
//                stringSegment = ThetaSegment.getSequence();
//            }
//            sequenceBuilder.append(stringSegment);
//        }
//        return sequenceBuilder.toString();
//    }
//
//    public String getStringSequenceByParentSegment(int parentSegmentNum) {
//        StringBuilder sequenceBuilder = new StringBuilder();
//        int i = 0;
//        for (ThetaSegment ThetaSegment : ThetaSegments) {
//            if (++i <= parentSegmentNum) {
//                continue;
//            }
//            String stringSegment = ThetaSegment.getSequence();
//            sequenceBuilder.append(stringSegment);
//        }
//        return sequenceBuilder.toString();
//    }
//
//    public void setThetaSegments(List<ThetaSegment> ThetaSegments) {
//        this.ThetaSegments = ThetaSegments;
//    }
//
//    @Override
//    public String getType() {
//        return "compositeString";
//    }
//
//    @Override
//    public void init(SegmentConfig segmentConfig) throws Exception {
//        if (segmentConfig.getSegmentConfigs() == null || segmentConfig.getSegmentConfigs().length == 0) {
//            throw new ZeusFrameworkException("segmentGenerators is need");
//        }
//        SegmentConfig[] segmentGenerators = segmentConfig.getSegmentConfigs();
//        for (SegmentConfig segmentConfig : segmentGenerators) {
//            SegmentConfig segmentGenerator = (ThetaSegment) GenerateSequenceService.getSequence(segmentConfig);
//            segmentConfig.getInitArgs().putAll(segmentConfig.getInitArgs());
//            segmentGenerator.init(segmentConfig);
//            this.segmentGenerators.add(segmentGenerator);
//        }
//    }
//}
