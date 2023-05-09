package com.momo.theta.service.impl;

import com.momo.theta.service.RocketMQMsgHandler;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 一个默认的消处理器，
 */
@Slf4j
public class DefaultRocketMQMsgHandler implements RocketMQMsgHandler {

  public DefaultRocketMQMsgHandler() {
    log.info("加载DefaultMsgHandler 。。。。。。。。。。。。。。。。。。。");
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
