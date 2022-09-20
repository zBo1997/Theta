package com.momo.theta.config;


import com.momo.theta.constants.ListenerTypeEnum;
import com.momo.theta.constants.StatusEnum;
import com.momo.theta.properties.RocketMQProperty;
import com.momo.theta.service.RocketMQMsgHandler;
import com.momo.theta.service.impl.DefaultRocketMQMsgHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
@Configuration
@EnableConfigurationProperties({RocketMQProperty.class})
@ConditionalOnProperty(prefix = "rocketmq", value = "enable", havingValue = "true")
public class RocketMQConsumerConfig implements AutoCloseable {

    private RocketMQProperty property;

    private ApplicationContext applicationContext;

    private static final String DEFAULT_ROCKET_MQ_MSG_HANDLER = "defaultRocketMQMsgHandler";

    private static final Map<String, DefaultMQPushConsumer> topicConsumer = new HashMap<>();

    private ScheduledExecutorService scheduledExecutorService;

    public RocketMQConsumerConfig(RocketMQProperty property, ApplicationContext applicationContext) {
        this.property = property;
        this.applicationContext = applicationContext;
    }

    /**
     * 初始化所有的topic consumer
     */
    @PostConstruct
    public synchronized void initConsumer() throws MQClientException {


        //关停所有消费者
        if (!topicConsumer.isEmpty()) {
            topicConsumer.keySet().forEach(this::closeTopicConsumer);
            topicConsumer.clear();
        }

        //判断消费者组是否有效
        if (Objects.isNull(this.property.getConsumer()) || StatusEnum.DISABLE.getCode().equals(this.property.getConsumer().getEnable())) {
            log.warn("consumer com.momo.theta.redis.config is null or enable status is false, skip init consumers");
            return;
        }

        //遍历topics，启动响应的消费者
        List<RocketMQProperty.Topic> topics = this.property.getConsumer().getTopics();
        for (RocketMQProperty.Topic topicObj : topics) {
            //判断当前topic是否是有效状态
            if (StatusEnum.DISABLE.getCode().equals(topicObj.getEnable())) {
                log.warn("topic {}'s  enable status is false, skip init  consumer", topicObj.getTopicName());
                continue;
            }
            //启动topic消费者
            initTopicConsumer(topicObj);
        }

        //监控
        startMonitor();

    }


    /**
     * 关停消费者
     *
     * @param topicName topic名称
     */
    public void closeTopicConsumer(String topicName) {
        DefaultMQPushConsumer consumer = topicConsumer.remove(topicName);
        if (Objects.isNull(consumer)) {
            return;
        }
        //关停消费者
        log.info("try to shutdown topicName={} consumer", topicName);
        consumer.shutdown();
        log.info("topicName= {} consumer is shutdown ", topicName);

    }

    public void initTopicConsumer(RocketMQProperty.Topic topicObj) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        RocketMQMsgHandler msgHandler = getRocketMQMsgHandler(topicObj.getMsgHandler(), topicObj.getTopicName());
        //顺序消息
        if (ListenerTypeEnum.ORDERLY.getCode().equals(topicObj.getListenerType())) {
            consumer.registerMessageListener((MessageListenerOrderly) (messageExtList, context) -> {
                log.info("consumerGroup={} handle topic ={} , {} 's message ", topicObj.getConsumerGroup(), topicObj.getTopicName(), messageExtList.size());
                try {
                    msgHandler.handle(messageExtList);
                } catch (IOException e) {
                    log.error("message handle fail", e);
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
                // 标记该消息已经被成功消费
                return ConsumeOrderlyStatus.SUCCESS;
            });
        }

        //普通消息
        if (ListenerTypeEnum.CONCURRENTLY.getCode().equals(topicObj.getListenerType())) {
            consumer.registerMessageListener((MessageListenerConcurrently) (messageExtList, context) -> {
                log.info("consumerGroup={} handle topic ={} , {} 's message ", topicObj.getConsumerGroup(), topicObj.getTopicName(), messageExtList.size());
                try {
                    msgHandler.handle(messageExtList);
                } catch (IOException e) {
                    log.error("message handle fail", e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                // 标记该消息已经被成功消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
        }

        consumer.setConsumerGroup(topicObj.getConsumerGroup());
        consumer.setNamesrvAddr(this.property.getNamesrvAddr());
        consumer.setConsumeThreadMin(Objects.isNull(topicObj.getConsumeThreadMin()) ? this.property.getConsumer().getConsumeThreadMin() : topicObj.getConsumeThreadMin());
        consumer.setConsumeThreadMax(Objects.isNull(topicObj.getConsumeThreadMax()) ? this.property.getConsumer().getConsumeThreadMax() : topicObj.getConsumeThreadMax());
        consumer.setConsumeMessageBatchMaxSize(Objects.isNull(topicObj.getConsumeMessageBatchMaxSize()) ? this.property.getConsumer().getConsumeMessageBatchMaxSize() : topicObj.getConsumeMessageBatchMaxSize());
        consumer.setPullBatchSize(Objects.isNull(topicObj.getPullBatchSize()) ? this.property.getConsumer().getPullBatchSize() : topicObj.getPullBatchSize());
        consumer.setVipChannelEnabled(false);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        try {
            log.info("consumerGroup={}，topic={} , {} get msg from broker ，handle {} 's msg ", consumer.getConsumerGroup(), topicObj.getTopicName(), consumer.getPullBatchSize(), consumer.getConsumeMessageBatchMaxSize());
            consumer.subscribe(topicObj.getTopicName(), topicObj.getTag());
            consumer.start();
            topicConsumer.put(topicObj.getTopicName(), consumer);
            log.info("consumer is start on namesrvAddr:{},groupName:{},topic:{},tag:{}", property.getNamesrvAddr(), topicObj.getConsumerGroup(), topicObj.getTopicName(), topicObj.getTag());
        } catch (Exception e) {
            log.error("consumer is error,groupName:{},topic:{},tag:{}", topicObj.getConsumerGroup(), topicObj.getTopicName(), topicObj.getTag(), e);
            throw e;
        }
    }


    /**
     * 根据 配置文件指定的消息处理类获取处理类
     *
     * @param msgHandler
     * @param topicName
     * @return
     */
    private RocketMQMsgHandler getRocketMQMsgHandler(String msgHandler, String topicName) {
        Map<String, RocketMQMsgHandler> msgHandlerMap = applicationContext.getBeansOfType(RocketMQMsgHandler.class);
        RocketMQMsgHandler mqMsgHandler = null;
        try {
            if (StringUtils.isEmpty(msgHandler)) {
                log.warn("topic:{} use default handler", topicName);
                return msgHandlerMap.get(DEFAULT_ROCKET_MQ_MSG_HANDLER);
            }
            mqMsgHandler = msgHandlerMap.get(msgHandler);
            if (Objects.isNull(mqMsgHandler)) {
                Optional<RocketMQMsgHandler> handlerOpt = msgHandlerMap.values().stream().filter(x -> x.getClass().getName().equals(msgHandler)).findFirst();
                if (handlerOpt.isPresent()) {
                    mqMsgHandler = handlerOpt.get();
                }
            }
        } catch (Exception e) {
            log.error("can not find RocketMQMsgHandler:{} with error{}", msgHandler, e.getMessage(), e);
        }
        return mqMsgHandler;
    }

    private void startMonitor() {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(() -> log.info("当前运行 consumer 共 {} 个，分别是：{}", topicConsumer.size(), topicConsumer.keySet()), 60, 30, TimeUnit.SECONDS);
    }

    @Bean(DEFAULT_ROCKET_MQ_MSG_HANDLER)
    public DefaultRocketMQMsgHandler getDefaultRocketMQMsgHandler() {
        return new DefaultRocketMQMsgHandler();
    }

    @Override
    public void close() throws Exception {
        log.info("尝试关闭scheduledExecutorService线程池");
        scheduledExecutorService.shutdownNow();
        //关停所有消费者
        if (!topicConsumer.isEmpty()) {
            topicConsumer.keySet().forEach(this::closeTopicConsumer);
            topicConsumer.clear();
        }
    }
}
