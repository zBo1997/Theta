package com.momo.theta.service;

import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

/**
 * 一个默认的消处理器，
 */
@Slf4j
@Service
public class CouRocketMQMsgHandler implements RocketMQMsgHandler {

  public CouRocketMQMsgHandler() {
    log.info("加载CouRocketMQMsgHandler 。。。。。。。。。。。。。。。。。。。");
  }


  public void handle(MessageExt ext) {
    log.info("DefaultRocketMQMsgHandler brokerName:{},topic:{},queueId:{},msgId:{},keys:{},msg:{} ",
        ext.getBrokerName(), ext.getTopic(), ext.getQueueId(), ext.getMsgId(), ext.getKeys(),
        new String(ext.getBody()));
  }


  @Override
  public void handle(List<MessageExt> messageExtList) {
    if (Objects.isNull(messageExtList) || messageExtList.isEmpty()) {
      return;
    }
    messageExtList.forEach(this::handle);
  }
}
