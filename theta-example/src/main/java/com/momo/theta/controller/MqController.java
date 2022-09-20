package com.momo.theta.controller;

import com.momo.theta.queue.DisruptorQueue;
import com.momo.theta.queue.data.MqData;
import com.momo.theta.service.MsgQueue;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("mq")
public class MqController {


    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Autowired
    private MsgQueue msgQueue;

    @GetMapping("queueNormalDelay")
    public void queueNormalDelay() {

        for (int i = 0; i < 100; i++) {
            String msgStr = " num= " + i;
            MqData mqData = new MqData();
            mqData.setOrderly(false);
            mqData.setTopic("concurrently-topic");
            mqData.setTags("*");
            mqData.setKeys(i + "");
            mqData.setBody(msgStr);
            mqData.setDelayTimeLevel(3);
            boolean publish = msgQueue.publish(mqData);

        }
    }

    @GetMapping("queueNormal")
    public void queueNormal() {
        int count = DisruptorQueue.DEFAULT_SIZE;
        for (int i = 0; i < count + 1; i++) {
            String msgStr = "normal queue num= " + i;
            MqData mqData = new MqData();
            mqData.setOrderly(false);
            mqData.setTopic("concurrently-topic");
            mqData.setTags("*");
            mqData.setKeys(i + "");
            mqData.setBody(msgStr);
            boolean publish = msgQueue.publish(mqData);

        }
    }

    @GetMapping("queueOrder")
    public void queueOrder() {
        int count = DisruptorQueue.DEFAULT_SIZE;
        for (int i = 0; i < count + 1; i++) {
            String msgStr = "order queue num= " + i;
            MqData mqData = new MqData();
            mqData.setOrderly(true);
            mqData.setTopic("orderly-topic");
            mqData.setTags("*");
            mqData.setKeys(i + "");
            mqData.setBody(msgStr);
            mqData.setDelayTimeLevel(-1);
            boolean publish = msgQueue.publish(mqData);

        }
    }

    /**
     * 队列
     *
     * @throws MQBrokerException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws MQClientException
     */
    @GetMapping("mqOrder")
    public void mqOrder() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {

        for (int i = 0; i < 100; i++) {
            String msgStr = " num= " + i;
            Message msg = new Message("orderly-topic", "*", msgStr.getBytes(StandardCharsets.UTF_8));
            defaultMQProducer.send(msg, (list, message, qIndex) -> {
                Integer queueIndex = (Integer) qIndex % list.size();
                MessageQueue queue = list.get(queueIndex);
                return queue;
            }, i);
        }
    }

    /**
     * 队列
     *
     * @throws MQBrokerException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws MQClientException
     */
    @GetMapping("mqNormal")
    public void mqNormal() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        for (int i = 0; i < 100; i++) {
            String msgStr = " num= " + i;
            Message msg = new Message("concurrently-topic", "*", msgStr.getBytes(StandardCharsets.UTF_8));
            defaultMQProducer.send(msg);
        }
    }

    /**
     * 顺序消费测试
     *
     * @throws InterruptedException
     */
    @GetMapping("orderly")
    public void sendOrderlyMsg() throws InterruptedException {
        new Thread(new ThreadGroup("order"),
                () -> {
                    try {
                        String name = Thread.currentThread().getName();
                        Integer threadName = Integer.valueOf(name);
                        Integer index = threadName % 16;
                        log.info("线程 1 开发发送消息");
                        for (int i = 0; i < 100; i++) {
                            String msgStr = "queue= " + index + " num= " + i;
                            Message msg = new Message("orderly-topic", "*", msgStr.getBytes(StandardCharsets.UTF_8));
                            defaultMQProducer.send(msg, (list, message, qIndex) -> {
                                Integer queueIndex = (Integer) qIndex;
                                MessageQueue queue = list.get(queueIndex);
                                return queue;
                            }, index);
                        }
                        log.info("线程 1 发送消息结束");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                "1"
        ).start();
        new Thread(new ThreadGroup("order"),
                () -> {
                    try {
                        String name = Thread.currentThread().getName();
                        Integer threadName = Integer.valueOf(name);
                        Integer index = threadName % 16;
                        log.info("线程 2 开发发送消息");
                        for (int i = 0; i < 100; i++) {
                            String msgStr = "queue= " + index + " num= " + i;
                            Message msg = new Message("orderly-topic", "*", msgStr.getBytes(StandardCharsets.UTF_8));
                            defaultMQProducer.send(msg, (list, message, qIndex) -> {
                                Integer queueIndex = (Integer) qIndex;
                                MessageQueue queue = list.get(queueIndex);
                                return queue;
                            }, index);
                        }
                        log.info("线程 2 发送消息结束");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                "2"
        ).start();

        TimeUnit.SECONDS.sleep(5);

    }

    /**
     * 并发消费测试
     *
     * @throws InterruptedException
     */
    @GetMapping("concurrently")
    public void sendConcurrentlyMsg() throws InterruptedException {


        new Thread(new ThreadGroup("concurrent"),
                () -> {
                    try {
                        String name = Thread.currentThread().getName();
                        Integer threadName = Integer.valueOf(name);
                        Integer index = threadName % 16;
                        log.info("线程 1 开发发送消息");
                        for (int i = 0; i < 100; i++) {
                            String msgStr = "queue= " + index + " num= " + i;
                            Message msg = new Message("concurrently-topic", "*", msgStr.getBytes(StandardCharsets.UTF_8));
                            msg.setDelayTimeLevel(1);
                            defaultMQProducer.send(msg);
                        }
                        log.info("线程 1 发送消息结束");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                "1"
        ).start();
        new Thread(new ThreadGroup("concurrent"),
                () -> {
                    try {
                        String name = Thread.currentThread().getName();
                        Integer threadName = Integer.valueOf(name);
                        Integer index = threadName % 16;
                        log.info("线程 2 开发发送消息");
                        for (int i = 0; i < 100; i++) {
                            String msgStr = "queue= " + index + " num= " + i;
                            Message msg = new Message("concurrently-topic", "*", msgStr.getBytes(StandardCharsets.UTF_8));
                            defaultMQProducer.send(msg);
                        }
                        log.info("线程 2 发送消息结束");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                "2"
        ).start();
        TimeUnit.SECONDS.sleep(5);

    }

}
