/**
 *
 */
package com.momo.theta.segment.model;

/**
 * 数据库序列Sequence实体类
 */
public class DbSequenceConfig {

    /**
     * 序列号配置唯一标示
     */
    private String id;
    /**
     * 当前序列号
     */
    private long current;
    /**
     * 最大可用序列号
     */
    private long maximum;

    public DbSequenceConfig() {
        super();
    }

    public DbSequenceConfig(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getMaximum() {
        return maximum;
    }

    public void setMaximum(long maximum) {
        this.maximum = maximum;
    }

}
