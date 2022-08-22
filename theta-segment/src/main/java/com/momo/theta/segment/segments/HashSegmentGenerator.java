package com.momo.theta.segment.segments;

import com.momo.theta.segment.api.generator.VariableSegmentGenerator;
import com.momo.theta.segment.config.SegmentConfig;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 哈希生成
 */
public class HashSegmentGenerator implements VariableSegmentGenerator {

    /**
     *  用来hash的字段域
     */
    private String hashField;

    /**
     * 用来hash的mod
     */
    private String modField;

    /**
     * 默认数据库字段
     */
    private String defaultValueField;

    /**
     * 数据库或表开始位置
     */
    private String startField;

    /**
     * 默认参数
     */
    private int defaultMod;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 是否左侧补零
     */
    private boolean leftZeroPadding = true;
    /**
     * 流水号片段长度
     */
    private int length = 0;


    @Override
    public String getStringSequence(Map<String, String> args) {
        String originalSequence = null;
        if(!StringUtils.isEmpty(defaultValueField)){
            Object defaultValue = args.get(defaultValueField);
            if(defaultValue != null){
                originalSequence =  defaultValue.toString();
            }
        }else if (defaultValue != null) {
            originalSequence = defaultValue;
        } else {
            int hash;
            if (args == null) {
                hash = ThreadLocalRandom.current().nextInt();
            } else {
                Object hashObject = args.get(hashField);
                if (hashObject == null) {
                    hash = ThreadLocalRandom.current().nextInt();
                } else {
                    hash = hashObject.hashCode();
                }
            }
            if (hash < 0) {
                hash = Math.abs(hash);
            }
            int mod = defaultMod;
            if (modField != null && args != null) {
                mod = Integer.parseInt(String.valueOf(args.get(modField)));
            }
            int startValue = 0;
            if(!StringUtils.isEmpty(startField)){
                Object startValueObj = args.get(startField);
                if(startValueObj != null){
                    startValue = Integer.parseInt(String.valueOf(startValueObj));
                }
            }
            originalSequence = Long.toString(hash % mod + startValue);
        }
        String segmentString = originalSequence;
        if (originalSequence.length() < length && leftZeroPadding) {
            int subSeqLength = length - originalSequence.length();
            StringBuilder zeroBuilder = new StringBuilder();
            for (int i = 0; i < subSeqLength; i++) {
                zeroBuilder.append("0");
            }
            segmentString = zeroBuilder.toString().concat(originalSequence);
        }
        return segmentString;
    }

    @Override
    public String getSequence() {
        throw new RuntimeException("HashSegmentGenerator should use getStringSegment with variable args");
    }


    @Override
    public void init(SegmentConfig sequenceConfig) throws Exception {
        Map<String, Object> args = sequenceConfig.getArgs();
        //长度
        if (args.containsKey("length")) {
            this.length = Integer.parseInt((String) args.get("length"));
        }
        //是否左边0补齐
        if (args.containsKey("leftZeroPadding")) {
            this.leftZeroPadding = Boolean.TRUE.toString().equalsIgnoreCase((String) args.get("leftZeroPadding"));
        }
        //默认Hash字段
        if (args.containsKey("defaultValueField")) {
            this.defaultValueField = (String) args.get("defaultValueField");
        }
        //默认值，如果指定了默认值 后续的参数不再设置
        if (args.containsKey("defaultValue")) {
            this.defaultValue = (String) args.get("defaultValue");
            return;
        }

        if (args == null || !args.containsKey("hashField")) {
            throw new RuntimeException("has segment need a hashField arg!");
        }

        if (!args.containsKey("defaultMod") && !args.containsKey("modField")) {
            throw new RuntimeException("has segment need a modField or defaultMod arg!");
        }

        this.hashField = (String) args.get("hashField");

        if (args.containsKey("modField")) {
            this.modField = (String) args.get("modField");
        }
        if (args.containsKey("defaultMod")) {
            this.defaultMod = Integer.parseInt(String.valueOf(args.get("defaultMod")));
        }
        if (args.containsKey("startField")) {
            this.startField = (String) args.get("startField");
        }
    }

    @Override
    public String getType() {
        return "hashSegment";
    }

}
