package com.momo.theta.mq.constants;

import lombok.Getter;

@Getter
public enum ListenerTypeEnum {
    /**
     * 顺序消费枚举
     */
    CONCURRENTLY("concurrently", "普通消息"),

    /**
     * 顺序消费枚举
     */
    ORDERLY("orderly", "顺序消息");

    /**
     * code
     */
    private String code;

    /**
     * 消息
     */
    private String msg;

    ListenerTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
