/**
 *
 */
package com.momo.theta.segment.composite;

import com.momo.theta.core.constants.BasicConstants;
import com.momo.theta.core.util.MapToBean;
import com.momo.theta.segment.api.ThetaSegment;
import com.momo.theta.segment.api.generator.NumberSequenceGenerator;
import com.momo.theta.segment.config.SegmentConfig;
import com.momo.theta.segment.generator.GenerateSegmentService;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 使用数据库默认序列进行序列号生成，
 * <p> 默认字符串序列号生成器（支持集群部署） </p>
 */
public class DefaultStringSequenceGenerator implements ThetaSegment {

    /**
     * 数字序列号生成器
     */
    private NumberSequenceGenerator numberSequenceGenerator;
    /**
     * 是否左侧零补齐
     */
    private boolean leftZeroPadding = true;
    /**
     * 序列号长度（此处作为属性主要目的是左侧零补齐，请保证配置时数字序列号的位数不会超过此处设置的值）
     */
    private int length = 0;
    /**
     * 前缀
     */
    private String prefix = BasicConstants.EMPTY_STRING;

    @Override
    public String getSequence() {
        String originalSequence = numberSequenceGenerator.getSequence();
        StringBuilder builder = new StringBuilder();
        builder.append(prefix);
        if (leftZeroPadding) {
            builder.append(getLeftZeroPadding(originalSequence));
        }
        builder.append(originalSequence);
        return builder.toString();
    }

    /**
     * 获取左侧零补齐
     *
     * @param originalSequence
     * @return
     */
    private String getLeftZeroPadding(String originalSequence) {

        int paddingLength = length - originalSequence.length();
        if (paddingLength >= 0) {
            return paddingTable[paddingLength];
        }
        return BasicConstants.EMPTY_STRING;
    }

    /**
     * 位数不够按照0进行补全
     */
    private final String[] paddingTable = {"", "0", "00", "000", "0000", "00000", "000000", "0000000", "00000000",
            "000000000", "0000000000", "00000000000", "000000000000", "0000000000000", "00000000000000",
            "000000000000000", "0000000000000000", "00000000000000000", "000000000000000000", "0000000000000000000",
            "00000000000000000000"};

    public NumberSequenceGenerator getNumberSequenceGenerator() {

        return numberSequenceGenerator;
    }

    public void setNumberSequenceGenerator(NumberSequenceGenerator numberSequenceGenerator) {
        this.numberSequenceGenerator = numberSequenceGenerator;
    }

    public boolean isLeftZeroPadding() {

        return leftZeroPadding;
    }

    public void setLeftZeroPadding(boolean leftZeroPadding) {

        this.leftZeroPadding = leftZeroPadding;
    }

    public int getLength() {

        return length;
    }

    public void setLength(int length) {
        //断言判断判断是否存在长度
        Assert.isTrue(length > 0);
        this.length = length;
    }

    public String getPrefix() {

        return prefix;
    }

    public void setPrefix(String prefix) {
        //断言判断，是否含有前缀
        Assert.hasText(prefix);
        this.prefix = prefix;
    }

    @Override
    public String getType() {
        return "defaultString";
    }

    /**
     * 初始化当付Segment的配置信息
     * @param segmentConfig 配置信息
     * @throws Exception 异常信息
     */
    @Override
    public void init(SegmentConfig segmentConfig) throws Exception {
        Map<String, Object> args = segmentConfig.getArgs();
        if (args.containsKey("length")) {
            this.length = Integer.parseInt((String) args.get("length"));
        }
        if (args.containsKey("prefix")) {
            this.prefix = (String) args.get("prefix");
        }
        if (args.containsKey("leftZeroPadding")) {
            this.leftZeroPadding = "true".equals(args.get("prefix").toString());
        }

        if (!args.containsKey("numberSequenceConfig")) {
            throw new RuntimeException("numberSequenceConfig is need");
        }

        Map<String, Object> numberSequenceMap = (Map<String, Object>) args.get("numberSequenceConfig");
        SegmentConfig numberSequenceConfig = MapToBean.mapToBean(numberSequenceMap, SegmentConfig.class);
        this.numberSequenceGenerator = (NumberSequenceGenerator) GenerateSegmentService.getSegment(numberSequenceConfig);
        this.numberSequenceGenerator.init(numberSequenceConfig);
    }
}
