package com.momo.theta.config;

import com.momo.theta.properties.RocketMQProperty;
import com.momo.theta.service.MqProducer;
import com.momo.theta.service.MsgQueue;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties({RocketMQProperty.class})
@ConditionalOnProperty(prefix = "rocketmq", value = "enable", havingValue = "true")
public class RocketMQProducerConfig {

    private RocketMQProperty property;

    public RocketMQProducerConfig(RocketMQProperty property) {
        this.property = property;
    }


    /**
     * 注入默认生产者
     *
     * @return
     * @throws MQClientException
     */
    @Bean
    @ConditionalOnProperty(prefix = "rocketmq.producer", value = "enable", havingValue = "true")
    public DefaultMQProducer getRocketMQProducer() throws MQClientException {


        DefaultMQProducer producer = new DefaultMQProducer(property.getProducer().getGroupName());
        producer.setNamesrvAddr(property.getNamesrvAddr());
        producer.setMaxMessageSize(property.getProducer().getMaxMessageSize());
        producer.setSendMsgTimeout(property.getProducer().getSendMsgTimeout());
        producer.setRetryTimesWhenSendFailed(property.getProducer().getRetryTimesWhenSendFailed());
        producer.setRetryTimesWhenSendAsyncFailed(property.getProducer().getRetryTimesWhenSendFailed());
        producer.setRetryAnotherBrokerWhenNotStoreOK(true);

        try {
            producer.start();
            log.info("producer is start at :{} , groupName:{}", property.getNamesrvAddr(), property.getProducer().getGroupName());
        } catch (MQClientException e) {
            log.error("producer is error {}", e.getMessage(), e);
            throw e;
        }
        return producer;
    }


    @Bean
    @ConditionalOnProperty(prefix = "rocketmq.producer", value = "enable", havingValue = "true")
    public MqProducer getThetaMqProducer(DefaultMQProducer defaultMQProducer) {
        MqProducer mqProducer = new MqProducer();
        mqProducer.setMqProducer(defaultMQProducer);
        return mqProducer;
    }


    @Bean
    @ConditionalOnProperty(prefix = "rocketmq.producer.queue", value = "enable", havingValue = "true")
    public MsgQueue configThetaQueue(MqProducer mqProducer) {
        MsgQueue msgQueue = new MsgQueue(mqProducer);
        msgQueue.setFilePath(property.getProducer().getFilePath());
        msgQueue.init();
        return msgQueue;

    }

}
