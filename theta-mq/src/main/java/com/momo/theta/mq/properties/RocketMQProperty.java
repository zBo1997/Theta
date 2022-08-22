package com.momo.theta.mq.properties;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.util.List;
import java.util.Objects;


@Slf4j
@Data
@Validated
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQProperty {

    public static final String DEFAULT_STATUS = "false";

    /**
     * 是否开启自动配置
     */
    private String enable = DEFAULT_STATUS;

    /**
     * mq的nameserver地址
     */
    @NotEmpty
    private String namesrvAddr;

    private Producer producer;
    private Consumer consumer;

    @Data
    public static class Producer {

        /**
         * 是否开启自动配置生产者
         */
        private String enable = DEFAULT_STATUS;

        /**
         * 默认group名称
         */
        private String groupName = "default";
        /**
         * 消息最大长度 默认1024*4(4M)
         */
        private int maxMessageSize = 4096;
        /**
         * 发送消息超时时间,默认3000
         */
        private int sendMsgTimeout = 3000;
        /**
         * 发送消息失败重试次数，默认3
         */
        private int retryTimesWhenSendFailed = 3;

        /**
         * 消息存储文件路径
         */
        private String filePath = System.getProperty("user.home") + File.separator + "queue";


    }

    @Data
    public static class Consumer {
        private String enable = DEFAULT_STATUS;

        /**
         * 消费者线程数量
         */
        private int consumeThreadMin = 1;
        private int consumeThreadMax = 16;
        /**
         * 设置一次消费消息的条数，默认为1条
         */
        private int consumeMessageBatchMaxSize = 1;
        /**
         * 设置每次从brokcer拉取消息数量，默认为32
         */
        private int pullBatchSize = 32;
        private List<Topic> topics;
    }

    @Data
    public static class Topic {
        private String enable = DEFAULT_STATUS;
        private String topicName;
        private String tag;
        private String consumerGroup;
        /**
         * 消费模式
         * concurrently-并发消费
         * orderly-顺序消费
         */
        private String listenerType = "concurrently";
        /**
         * 消费消息处理类型，可以是spring容器中的id，也可以是包路径
         */
        private String msgHandler;

        /**
         * 消费者线程数量
         */
        private Integer consumeThreadMin;
        private Integer consumeThreadMax;

        /**
         * 设置一次消费消息的条数，默认为1条
         */
        private Integer consumeMessageBatchMaxSize;
        /**
         * 设置每次从brokcer拉取消息数量，默认为32
         */
        private Integer pullBatchSize;

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            return (obj instanceof Topic
                    && Objects.equals(((Topic) obj).getEnable(), this.enable)
                    && Objects.equals(((Topic) obj).getTopicName(), this.topicName)
                    && Objects.equals(((Topic) obj).getTag(), this.tag)
                    && Objects.equals(((Topic) obj).getConsumerGroup(), this.consumerGroup)
                    && Objects.equals(((Topic) obj).getListenerType(), this.listenerType)
                    && Objects.equals(((Topic) obj).getMsgHandler(), this.msgHandler)
                    && Objects.equals(((Topic) obj).getMsgHandler(), this.consumeThreadMin)
                    && Objects.equals(((Topic) obj).getMsgHandler(), this.consumeThreadMax)
                    && Objects.equals(((Topic) obj).getMsgHandler(), this.consumeMessageBatchMaxSize)
                    && Objects.equals(((Topic) obj).getMsgHandler(), this.pullBatchSize));
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.enable, this.topicName, this.tag, this.consumerGroup, this.listenerType, this.msgHandler, this.consumeThreadMin, this.consumeThreadMax, this.consumeMessageBatchMaxSize, this.pullBatchSize);
        }

    }

}
