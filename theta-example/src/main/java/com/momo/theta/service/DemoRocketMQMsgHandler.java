package com.momo.theta.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DemoRocketMQMsgHandler implements RocketMQMsgHandler {

    public DemoRocketMQMsgHandler() {
        log.info("加载DemoRocketMQMsgHandler 。。。。。。。。。。。。。。。。。。。");
    }

    public void handle(MessageExt ext) {
        log.info("DemoRocketMQMsgHandler 收到消息>>>>>>>>>> brokerName:{},topic:{},queueId:{},msgId:{},keys:{},msg:{} ", ext.getBrokerName(), ext.getTopic(), ext.getQueueId(), ext.getMsgId(), ext.getKeys(), new String(ext.getBody()));
    }

    @Override
    public void handle(List<MessageExt> messageExtList) {
        if (Objects.isNull(messageExtList) || messageExtList.isEmpty()) {
            return;
        }
        messageExtList.forEach(this::handle);
    }
}
