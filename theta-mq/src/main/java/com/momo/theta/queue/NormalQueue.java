package com.momo.theta.queue;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.momo.theta.queue.data.MqDataEvent;
import com.momo.theta.queue.data.ObjectEvent;
import com.momo.theta.service.MqProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 普通rocketMq队列
 */
@Slf4j
public class NormalQueue extends DisruptorQueue {


    public static final String fineName = "NORMAL.DATA";

    private final MqProducer producer;

    private String filePath;


    public NormalQueue(MqProducer producer, String filePath) {
        this.producer = producer;
        this.filePath = filePath + File.separator + NormalQueue.fineName;
    }

    @Override
    public void start() {
        init();
        Disruptor<MqDataEvent> disruptor = getDisruptor();
        Consumer[] consumers = new Consumer[20];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer(this.producer, filePath);
        }
        disruptor.handleEventsWithWorkerPool(consumers)
                .then((event, sequence, endOfBatch) -> event.clear());

        loadFromFile(filePath);
        FileStore.delete(filePath);
        disruptor.start();

    }

    /**
     * mq使用
     */
    @AllArgsConstructor
    private static class Consumer implements WorkHandler<MqDataEvent> {
        private MqProducer producer;
        private String filePath;

        @Override
        public void onEvent(MqDataEvent event) {
            if (SAVE_FILE_FLAG) {
                log.info("NormalQueue 消息转存本地文件 key={} ", event.getValue().getKeys());
                FileStore.write(filePath, event.getValue());
            } else {
                log.info("NormalQueue 消息发送mq key={}", event.getValue().getKeys());
                this.producer.sendMsg(event.getValue());
            }

        }
    }


    /**
     * 单机使用Disruptor
     * @param <T>
     */
    @AllArgsConstructor
    public abstract static class ADisruptorConsumer<T> implements EventHandler<ObjectEvent<T>>, WorkHandler<ObjectEvent<T>> {

        @Override
        public void onEvent(ObjectEvent<T> event) {
            this.consume(event.getObj());
        }

        @Override
        public void onEvent(ObjectEvent<T> event, long l, boolean b) throws Exception {
            this.onEvent(event);
        }

        public abstract void consume(T var1);
    }

}
