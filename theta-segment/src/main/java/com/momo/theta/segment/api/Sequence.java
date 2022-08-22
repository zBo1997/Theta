package com.momo.theta.segment.api;

import com.momo.theta.core.config.SpringContextHolder;
import com.momo.theta.segment.composite.CompositeStringSequenceGenerator;
import com.momo.theta.segment.config.SegmentConfig;
import com.momo.theta.segment.segments.DbNumberSegmentGenerator;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 最终互获取Sequence的对象，通过此对象可获取对应的值
 */
public class Sequence implements ThetaSegment {
    private final String id;
    private ThetaSegment sequence;
    private Map<String, DbNumberSegmentGenerator> dbNumberSegmentGeneratorMap = new ConcurrentHashMap<>();
    private String subTableName;
    private String subStep = "100";
    private String subLength = "6";
    private String subCurrentSequenceKey;
    private String subMaximumSequenceKey;
    private String subMaxSequenceValue;
    private String subTransactionManager;
    private String subIdKey;

    private int parentSegmentNum;

    public Sequence(String id) {
        this.id = id;
    }

    public Sequence(String id, int parentSegmentNum) {
        this.id = id;
        this.parentSegmentNum = parentSegmentNum;
    }

    @Override
    public String getSequence() {
        checkInit();
        return sequence.getSequence();
    }

    public String getSequence(Map<String, String> args) {
        checkInit();
        if (!(sequence instanceof CompositeStringSequenceGenerator)) {
            throw new RuntimeException("VariableSequenceSegmentGenerator should be an instance of  CompositeStringSequenceGenerator");
        }
        CompositeStringSequenceGenerator compositeStringSequenceGenerator = (CompositeStringSequenceGenerator) sequence;
        return compositeStringSequenceGenerator.getStringSequence(args);
    }

    /**
     * 以父流水生成流水号
     *
     * @param parentSeq
     * @return
     */
    public String getStringSequenceByParentSegment(String parentSeq) {
        checkInit();
        StringBuilder result = new StringBuilder();
        if (!(sequence instanceof CompositeStringSequenceGenerator)) {
            throw new RuntimeException("VariableSequenceSegmentGenerator should be an instance of  CompositeStringSequenceGenerator");
        }
        CompositeStringSequenceGenerator compositeStringSequenceGenerator = (CompositeStringSequenceGenerator) sequence;
        String subSeq = compositeStringSequenceGenerator.getStringSequenceByParentSegment(parentSegmentNum);
        int p = parentSeq.length() - subSeq.length();
        result.append(parentSeq.substring(0, p));
        result.append(subSeq);
        return result.toString();
    }

    /**
     * 以父流水生成流水号
     *
     * @param parentSeq
     * @return
     */
    public String getStringSequenceByParentSegment(String parentSeq, Map<String, String> args) {
        checkInit();
        StringBuilder result = new StringBuilder();
        if (!(sequence instanceof CompositeStringSequenceGenerator)) {
            throw new RuntimeException("VariableSequenceSegmentGenerator should be an instance of  CompositeStringSequenceGenerator");
        }
        CompositeStringSequenceGenerator compositeStringSequenceGenerator = (CompositeStringSequenceGenerator) sequence;
        int parentIndex = parentSegmentNum;
        if (StringUtils.isEmpty(parentSeq)) {
            parentIndex = 0;
        }
        String subSeq = compositeStringSequenceGenerator.getStringSequenceByParentSegment(parentIndex, args);
        if (StringUtils.isEmpty(parentSeq)) {
            return subSeq;
        }
        int p = parentSeq.length() - subSeq.length();
        result.append(parentSeq.substring(0, p));
        result.append(subSeq);
        return result.toString();
    }

    /**
     * 初始化检查
     */
    public void checkInit() {
        if (sequence == null) {
            synchronized (this) {
                if (sequence == null) {
                    sequence = SpringContextHolder.getBean(id, ThetaSegment.class);
                }
            }
        }
    }


    @Override
    public String getType() {
        return "";
    }

    @Override
    public void init(SegmentConfig segmentConfig) throws Exception {

    }

    public String getSubTableName() {
        return subTableName;
    }

    public void setSubTableName(String subTableName) {
        this.subTableName = subTableName;
    }

    public String getSubStep() {
        return subStep;
    }

    public void setSubStep(String subStep) {
        this.subStep = subStep;
    }

    public String getSubLength() {
        return subLength;
    }

    public void setSubLength(String subLength) {
        this.subLength = subLength;
    }

    public String getSubCurrentSequenceKey() {
        return subCurrentSequenceKey;
    }

    public void setSubCurrentSequenceKey(String subCurrentSequenceKey) {
        this.subCurrentSequenceKey = subCurrentSequenceKey;
    }

    public String getSubMaximumSequenceKey() {
        return subMaximumSequenceKey;
    }

    public void setSubMaximumSequenceKey(String subMaximumSequenceKey) {
        this.subMaximumSequenceKey = subMaximumSequenceKey;
    }

    public String getSubMaxSequenceValue() {
        return subMaxSequenceValue;
    }

    public void setSubMaxSequenceValue(String subMaxSequenceValue) {
        this.subMaxSequenceValue = subMaxSequenceValue;
    }

    public String getSubTransactionManager() {
        return subTransactionManager;
    }

    public void setSubTransactionManager(String subTransactionManager) {
        this.subTransactionManager = subTransactionManager;
    }

    public String getSubIdKey() {
        return subIdKey;
    }

    public void setSubIdKey(String subIdKey) {
        this.subIdKey = subIdKey;
    }
}
