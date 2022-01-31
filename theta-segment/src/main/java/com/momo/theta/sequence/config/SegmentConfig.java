package com.momo.theta.sequence.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SegmentConfig {
    /**
     * 主键配置Id
     */
    private String id;

    /**
     *分片类型
     */
    private String type;

    /**
     * 参数
     */
    private Map<String, Object> args;

    /**
     * 嵌套配置参数
     */
    private SegmentConfig[] segmentConfigs;

    /**
     * 初始话参数
     */
    private Map<String, Object> initArgs = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    public SegmentConfig[] getSegmentConfigs() {
        return segmentConfigs;
    }

    public void setSegmentConfigs(SegmentConfig[] segmentConfigs) {
        this.segmentConfigs = segmentConfigs;
    }

    public void addInitArg(String name, Object value) {
        initArgs.put(name, value);
    }

    public Object getInitArg(String name) {
        return initArgs.get(name);
    }

    public Map<String, Object> getInitArgs() {
        return initArgs;
    }

    public void setInitArgs(Map<String, Object> initArgs) {
        this.initArgs = initArgs;
    }

    @Override
    public String toString() {
        return "DbSequenceConfig{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", args=" + args +
                ", segmentConfigs=" + Arrays.toString(segmentConfigs) +
                '}';
    }
}
