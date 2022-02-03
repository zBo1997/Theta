package com.momo.theta.segment.segments;

import com.momo.theta.segment.api.generator.VariableSegmentGenerator;
import com.momo.theta.segment.config.SegmentConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableSequenceSegmentGenerator implements VariableSegmentGenerator {

    /**
     * 变量匹配模板
     */
    private String pattern;

    /**
     * 是否转换大写
     */
    private String upperCase;

    /**
     * 是否转换小写
     */
    private String isLowerCase;

    private List<String> appendList = new ArrayList<String>();// 追加常量值
    private List<String> patternList = new ArrayList<String>();// 追加变量值

    @Override
    public String getStringSegment(Map<String, String> args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < appendList.size(); i++) {
            sb.append(appendList.get(i));
            if (i < patternList.size()) {
                String append = args.get(patternList.get(i));
                if (append == null) {
                    throw new RuntimeException("there is no variable key " + patternList.get(i) + " in args map" + args);
                }
                sb.append(append);
            }
        }
        String result = sb.toString();
        if (Boolean.TRUE.toString().equals(upperCase)) {
            result = result.toUpperCase();
        } else if (Boolean.TRUE.toString().equals(isLowerCase)) {
            result = result.toLowerCase();
        }
        return result;
    }


    @Override
    public String getSequence() {
        throw new RuntimeException("VariableSequenceSegmentGenerator should use getStringSegment with variable args");
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


    @Override
    public void init(SegmentConfig segmentConfig) throws Exception {

        Map<String, Object> args = segmentConfig.getArgs();
        if (!args.containsKey("pattern")) {
            throw new RuntimeException("pattern is need");
        }

        this.upperCase = (String) args.get("upperCase");
        this.isLowerCase = (String) args.get("lowerCase");

        this.pattern = (String) args.get("pattern");

        Pattern p = Pattern.compile("\\#\\{\\w+\\.?\\w+\\}");
        Matcher matcher = p.matcher(pattern);
        int start, end = 0;
        while (matcher.find()) {
            String arg = matcher.group();
            start = matcher.start();
            String append = pattern.substring(end, start);
            end = matcher.end();
            appendList.add(append);
            patternList.add(arg.substring(2, arg.length() - 1));
        }
        if (end != pattern.length()) {
            appendList.add(pattern.substring(end));
        }
    }

    @Override
    public String getType() {
        return "variableSequenceSegment";
    }
}
