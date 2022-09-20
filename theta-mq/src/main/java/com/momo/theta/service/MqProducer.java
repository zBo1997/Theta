package com.momo.theta.service;

import com.momo.theta.queue.data.MqData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Mq生成者
 */
@Data
@Slf4j
public class MqProducer {

    DefaultMQProducer mqProducer;


    private MessageQueueSelector messageQueueSelector = (list, message, keys) -> {
        int idx = keys.hashCode();
        if (idx < 0) {
            idx *= -1;
        }
        int index = idx % list.size();
        return list.get(index);
    };


    /**
     * 发送消息
     *
     * @param data mq数据
     */
    public boolean sendMsg(MqData data) {
        try {
            Message message = new Message(data.getTopic(), data.getTags(), data.getKeys(), data.getFlag(), data.getBody().getBytes(StandardCharsets.UTF_8), true);
            if (data.getDelayTimeLevel() > 0) {
                message.setDelayTimeLevel(data.getDelayTimeLevel());
            }
            SendResult result;
            if (data.isOrderly()) {
                result = mqProducer.send(message, messageQueueSelector, message.getKeys(), 1000L);
            } else {
                result = mqProducer.send(message);
            }
            if (Objects.isNull(result) || !SendStatus.SEND_OK.equals(result.getSendStatus())) {
                log.error("mq send fail data={},result={}", message, result);
                return false;
            }
            log.info("mq send success keys={}, result={}", message.getKeys(), result);
            return true;
        } catch (InterruptedException e) {
            log.error("mq data={}", data, e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("mq send fail data={}", data, e);
        }
        return false;
    }


}
