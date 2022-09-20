package com.momo.theta.service;

import org.apache.rocketmq.common.message.MessageExt;

import java.io.IOException;
import java.util.List;

/**
 * rocketMq消息处理器
 */
public interface RocketMQMsgHandler {


    /**
     * 批量处理消息，如有必要，请重载该消息
     *
     * @param messageExtList 待处理消息列表
     */
    void handle(List<MessageExt> messageExtList) throws IOException;

}
