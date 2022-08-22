package com.momo.theta.mq.queue.data;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * MqData封装的数据
 */
@Data
public class MqData {

    /**
     * 是否有序消息
     * true-有序消息
     * false-普通消息
     */
    private boolean orderly;

    /**
     * mq topic
     */
    private String topic;

    /**
     * mq tags
     */
    private String tags;

    /**
     * mq keys，当orderly=true时，使用keys作为分区标识
     */
    private String keys;

    /**
     * mq flag
     */
    private int flag;

    /**
     * 消息体
     */
    private String body;

    /**
     * 延时级别
     */
    private int delayTimeLevel;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
